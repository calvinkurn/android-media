package com.tokopedia.pdpsimulation.common.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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
import com.tokopedia.pdpsimulation.common.utils.Utils
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


class PdpSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var pdpSimulationAnalytics: dagger.Lazy<PdpSimulationAnalytics>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
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
        payLaterViewModel.getProductDetail(productId)
        initListeners()

    }

    private fun observeViewModel() {
        payLaterViewModel.productDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> productDetailSuccess(it.data)
                is Fail -> productDetailFail()
            }
        })

        payLaterViewModel.payLaterOptionsDetailLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> Log.d("", "")
                is Fail -> Log.d("", "")
            }
        })
    }

    private fun productDetailFail() {
        payLaterViewModel.getPayLaterAvailableDetail(productPrice, productId)
        productInfoShimmer.gone()
        productDetail.gone()
    }

    /**
     * THis method called on product detail api success
     */
    private fun productDetailSuccess(data: GetProductV3) {
        payLaterViewModel.getPayLaterAvailableDetail(productPrice, productId)
        productInfoShimmer.gone()
        if (data.pictures?.size == 0 || data.productName.isNullOrEmpty() || data.price?.equals(0.0) == true)
            productDetail.gone()
        else
            setProductDetailView(data)

    }

    /**
     * This method called to set view for the product image,price and variant
     */
    private fun setProductDetailView(data: GetProductV3) {
        data.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                productDetail.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        data.productName.let {
            productDetail.productName.text = it
        }

        productDetail.productPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(productPrice, false)

        showProductVariant(data)

    }


    /**
     * THis method set data for the product variant
     * @param data  is the product detail data
     */
    private fun showProductVariant(data: GetProductV3) {
        data.variant?.let { variant ->
            if (variant.products.isNotEmpty() && variant.selections.isNotEmpty()) {
                var combination = -1
                for (i in variant.products.indices) {
                    if (productId == variant.products[i].productID) {
                        combination = variant.products[i].combination[0] ?: -1
                        break
                    }
                }
                if (combination != -1)
                    productDetail.productVariant.text =
                        variant.selections[0].options[combination]?.value ?: ""
            } else {
                productDetail.productVariant.gone()
            }
        }

    }

    private fun initListeners() {

        // This analytics is here as for now the kardu credit is disabled so tab is also disable
        /*sendAnalytics(PdpSimulationEvent.PayLater.SelectedPayLater)
        paylaterTabLayout.tabLayout.onTabSelected { tab ->
            if (paymentMode == PayLater) {
                sendAnalytics(PdpSimulationEvent.PayLater.TabChangeEvent(tab.getCustomText()))
            } else sendAnalytics(PdpSimulationEvent.CreditCard.TabChangeEvent(tab.getCustomText()))
            handleRegisterWidgetVisibility(tab.position)
        }
        payLaterViewPager.onPageSelected { position ->
            handleRegisterWidgetVisibility(position)
        }*/
    }


    override fun onResume() {
        super.onResume()
        /*if (payLaterViewModel.refreshData) {
            payLaterViewModel.getPayLaterAvailableDetail(productPrice)
            payLaterViewModel.refreshData = false
        }*/
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