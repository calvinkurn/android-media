package com.tokopedia.pdpsimulation.common.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.helper.*
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.common.presentation.adapter.PayLaterPagerAdapter
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.CreditCardSimulationFragment
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.CreditCardTncFragment
import com.tokopedia.pdpsimulation.creditcard.viewmodel.CreditCardViewModel
import com.tokopedia.pdpsimulation.paylater.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.paylater.presentation.detail.PayLaterOffersFragment
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.unifycomponents.getCustomText
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.fragment_pdp_simulation.*
import kotlinx.android.synthetic.main.product_detail.view.*
import javax.inject.Inject


class PdpSimulationFragment : BaseDaggerFragment(),
    PdpSimulationCallback,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var pdpSimulationAnalytics: dagger.Lazy<PdpSimulationAnalytics>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val creditCardViewModel: CreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(CreditCardViewModel::class.java)
    }

    private val bottomSheetNavigator: BottomSheetNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetNavigator(childFragmentManager)
    }

    private val productPrice: Long by lazy {
        val price = arguments?.getString(PRODUCT_PRICE)
        price.toDoubleOrZero().toLong()
    }
    private val productUrl: String by lazy {
        arguments?.getString(PARAM_PRODUCT_URL) ?: ""
    }

    private val productId: String by lazy {
        arguments?.getString(PARAM_PRODUCT_ID) ?: ""
    }

    private val isCreditCardModeAvailable: Boolean = false
    private var paymentMode: PaymentMode = PayLater
    private var pagerAdapter: PayLaterPagerAdapter? = null

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pdp_simulation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSimulationProductInfo()
        setUpModeSwitcher()
        renderTabAndViewPager()
        initListeners()

    }

    override fun getSimulationProductInfo() {
        payLaterViewModel.getProductDetail(productId = productId)
        payLaterViewPager.visible()
        when (paymentMode) {
            is PayLater -> {
                payLaterViewModel.getPayLaterAvailableDetail(productPrice)

            }
            is CreditCard -> {
                creditCardViewModel.getCreditCardSimulationData(productPrice)
            }
        }
    }

    private fun observeViewModel() {


        payLaterViewModel.productDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> productDetailSuccess(it.data)
                is Fail -> productDetailFail(it.throwable)
            }
        })
    }

    private fun productDetailFail(throwable: Throwable) {
        productInfoShimmer.gone()
        productDetail.gone()
    }

    private fun productDetailSuccess(data: GetProductV3) {
        productInfoShimmer.gone()
        if (data.pictures?.size == 0 || data.productName.isNullOrEmpty() || data.price?.equals(0.0) == true)
            productDetail.gone()
        else {
            data.pictures?.get(0)?.let { pictures ->
                pictures.urlThumbnail?.let { urlThumbnail ->
                    productDetail.productImage.loadImage(
                        urlThumbnail
                    )
                }
            }
            data.productName.let {
                productDetail.productName.text = it
            }
            data.price?.let {
                productDetail.productPrice.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false)
            }

            if(data.variant.products.isNotEmpty() && data.variant.selections.isNotEmpty())
            {
                var combination = -1
               for(i in data.variant.products.indices)
               {
                   if(productId == data.variant.products[i].productID){
                       combination = data.variant.products[i].combination[0]?:-1
                   }
               }
                if(combination != -1)
                    productDetail.productVariant.text = data.variant.selections[0].options[combination]?.value?:""
            }
            else
            {
                productDetail.productVariant.gone()
            }
        }
    }



    private fun isPayLaterSimulationPage(): Boolean {
        return (payLaterViewPager.currentItem == SIMULATION_TAB_INDEX &&
                paymentMode == PayLater)
    }


    private fun setUpModeSwitcher() {
        if (isCreditCardModeAvailable) {
            modeSwitcher.visible()
            modeSwitcher.setOnCheckedChangeListener(this)
        } else modeSwitcher.gone()
    }

    private fun initListeners() {
        paylaterTabLayout.tabLayout.onTabSelected { tab ->
            if (paymentMode == PayLater) {
                sendAnalytics(PdpSimulationEvent.PayLater.TabChangeEvent(tab.getCustomText()))
                sendAnalytics(PdpSimulationEvent.PayLater.SelectedPayLater)
            }
            else sendAnalytics(PdpSimulationEvent.CreditCard.TabChangeEvent(tab.getCustomText()))
            handleRegisterWidgetVisibility(tab.position)
        }
        payLaterViewPager.onPageSelected { position ->
            handleRegisterWidgetVisibility(position)
        }
    }

    private fun renderTabAndViewPager() {
        context?.let {
            setUpTabLayout()
            if (pagerAdapter == null)
                pagerAdapter = PayLaterPagerAdapter(it, childFragmentManager, 0)
            pagerAdapter?.setList(getFragments())
            payLaterViewPager.adapter = pagerAdapter
        }
    }

    private fun setUpTabLayout() {
        paylaterTabLayout?.run {
            setupWithViewPager(payLaterViewPager)
            getUnifyTabLayout().removeAllTabs()
            when (paymentMode) {
                is CreditCard -> {
                    addNewTab(context.getString(R.string.pdp_simulation_tab_title))
                    addNewTab(context.getString(R.string.pdp_simulation_credit_card_tnc_title))
                }
                else -> {
                    this.getUnifyTabLayout().gone()
                }
            }
        }
    }

    private fun getFragments(): List<Fragment> {
        return when (paymentMode) {
            is CreditCard -> {
                listOf<Fragment>(
                    CreditCardSimulationFragment.newInstance(this),
                    CreditCardTncFragment.newInstance(this)
                )
            }
            else -> {
                val bundle = Bundle()
                bundle.putLong(PRODUCT_PRICE, productPrice)
                listOf<Fragment>(PayLaterOffersFragment.newInstance(this, bundle))
            }
        }
    }

    private fun handleRegisterWidgetVisibility(position: Int) {
        if (position == SIMULATION_TAB_INDEX && !payLaterViewModel.isPayLaterProductActive)
            showRegisterWidget()
        else {
            payLaterBorder.gone()
        }
    }

    override fun showRegisterWidget() {
        if (isPayLaterSimulationPage()) {
            payLaterBorder.visible()
        } else {
            payLaterBorder.gone()
        }
    }




    override fun onCheckedChanged(modeButton: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            paymentMode = CreditCard
            getSimulationProductInfo()
        } else {
            paymentMode = PayLater
        }
        renderTabAndViewPager()
    }


    private fun hideDataGroup() {
        payLaterViewPager.gone()
    }

    override fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>) {
        bottomSheetNavigator.showBottomSheet(modelClass, bundle, this, productUrl)
    }

    override fun sendAnalytics(pdpSimulationEvent: PdpSimulationEvent) {
        pdpSimulationAnalytics.get().sendPdpSimulationEvent(pdpSimulationEvent)
    }

    override fun switchPaymentMode() {
        if (isCreditCardModeAvailable)
            modeSwitcher.isChecked = !modeSwitcher.isChecked
    }

    override fun showNoNetworkView() {
        hideDataGroup()
    }


    fun reloadProductDetail() {
        if(!productDetail.isVisible) {
            productInfoShimmer.visible()
            productDetail.visible()
            payLaterViewModel.getProductDetail(productId = productId)
        }

    }

    companion object {
        const val SIMULATION_TAB_INDEX = 0

        @JvmStatic
        fun newInstance(bundle: Bundle): PdpSimulationFragment {
            val fragment = PdpSimulationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}