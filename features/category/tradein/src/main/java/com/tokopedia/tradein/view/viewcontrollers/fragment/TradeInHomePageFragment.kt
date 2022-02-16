package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.view.viewcontrollers.activity.TradeInPromoActivity
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInExchangeMethodBS
import com.tokopedia.tradein.viewmodel.TradeInHomePageFragmentVM
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class TradeInHomePageFragment : BaseViewModelFragment<TradeInHomePageFragmentVM>(), ChooseAddressWidget.ChooseAddressWidgetListener, TradeInEducationalPageFragment.OnDoTradeInClick{

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInHomePageFragmentVM
    private lateinit var tradeInHomePageVM: TradeInHomePageVM
    private var chooseAddressWidget : ChooseAddressWidget? = null
    private var userAddressData: LocalCacheModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tradein_initial_price_parent_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelProvider)
            tradeInHomePageVM = viewModelProvider.get(TradeInHomePageVM::class.java)
        }
        addObservers()
        setUpView()
        tradeInHomePageVM.getDeviceModel()
    }

    private fun setUpView() {
        view?.apply {
            context?.let {
                userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
            }
            arguments?.getString(CACHE_ID, "")?.let {
                setUpPdpData(it)
            }
            findViewById<NavToolbar>(R.id.initial_price_navToolbar)?.run {
                viewLifecycleOwner.lifecycle.addObserver(this)
                setIcon(
                    IconBuilder()
                        .addIcon(IconList.ID_INFORMATION) {
                            setUpEducationalFragment()
                        }
                )
            }
            chooseAddressWidget = findViewById(R.id.tradein_choose_address_widget)
            chooseAddressWidget?.bindChooseAddress(this@TradeInHomePageFragment)
            findViewById<View>(R.id.collapse_view).setOnClickListener {
                findViewById<ConstraintLayout>(com.tokopedia.tradein.R.id.parent_collapse).let { layout->
                    if(layout.isVisible){
                        layout.hide()
                    } else
                        layout.show()
                }
            }
        }
    }

    private fun setUpEducationalFragment() {
        view?.findViewById<View>(R.id.educational_frame_content_layout)?.show()
        view?.findViewById<View>(R.id.initial_price_navToolbar)?.hide()
        val newFragment = TradeInEducationalPageFragment.getFragmentInstance()
        (newFragment as TradeInEducationalPageFragment).setUpTradeInClick(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.educational_frame_content_layout, newFragment, newFragment.tag)
            .commit()
    }

    override fun onClick() {
        view?.findViewById<View>(R.id.educational_frame_content_layout)?.hide()
        view?.findViewById<View>(R.id.initial_price_navToolbar)?.show()
    }

    override fun onBackClick() {
        view?.findViewById<View>(R.id.educational_frame_content_layout)?.hide()
        view?.findViewById<View>(R.id.initial_price_navToolbar)?.show()
    }


    private fun setUpPdpData(id: String) {
        view?.apply {
            viewModel.getPDPData(context, id)?.let {
                findViewById<Typography>(R.id.slashed_price).let{ price->
                    price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, true)
                    price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                findViewById<Typography>(R.id.product_price).text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, true)
                findViewById<Typography>(R.id.product_name).text = it.productName
                if(!it.productImage.isNullOrEmpty()){
                    (findViewById<ImageUnify>(R.id.product_image)).setImageUrl(it.productImage!!)
                }
                if(!it.shopBadge.isNullOrEmpty()){
                    (findViewById<ImageUnify>(R.id.image_shop_badge)).setImageUrl(it.shopBadge!!)
                }
                findViewById<Typography>(R.id.shop_name).text = it.shopName
                findViewById<Typography>(R.id.shop_location).text = it.shopLocation
            }
        }
    }

    private fun addObservers() {
        tradeInHomePageVM.laku6DeviceModel.observe(viewLifecycleOwner, Observer {
            setUpLaku6Data(it)
            viewModel.getTradeInDetail(it)
        })
        viewModel.tradeInDetailLiveData.observe(viewLifecycleOwner, Observer {
            onTradeInDetailSuccess()
        })
        viewModel.getProgBarVisibility().observe(viewLifecycleOwner, Observer {
            if(it)
                view?.findViewById<View>(R.id.tradein_loading_layout)?.show()
            else
                view?.findViewById<View>(R.id.tradein_loading_layout)?.hide()
        })
        viewModel.getErrorMessage().observe(viewLifecycleOwner, Observer {
            view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        setType(GlobalError.NO_CONNECTION)
                    }
                    is IllegalStateException -> {
                        setType(GlobalError.PAGE_FULL)
                    }
                    else -> {
                        setType(GlobalError.SERVER_ERROR)
                        errorDescription.text = it.message
                    }
                }
                view?.findViewById<View>(R.id.tradein_error_layout)?.show()
                setActionClickListener {
                    view?.findViewById<View>(R.id.tradein_error_layout)?.hide()
                    viewModel.startProgressBar()
                    tradeInHomePageVM.getDeviceModel()
                }
            }
        })
    }

    private fun setUpLaku6Data(laku6DeviceModel: Laku6DeviceModel?) {
        view?.apply {
            laku6DeviceModel?.let {
                findViewById<Typography>(R.id.model_text).text = it.model
                findViewById<Typography>(R.id.session_id).text = getString(com.tokopedia.tradein.R.string.tradein_session_id, it.sessionId)
            }
        }
    }

    private fun onTradeInDetailSuccess() {
        view?.apply {
            findViewById<View>(R.id.exchange_layout).setOnClickListener {
                val bottomSheet = TradeInExchangeMethodBS.newInstance(arrayListOf(), tradeInHomePageVM.is3PLSelected, "")
                bottomSheet.show(childFragmentManager, "")
            }
            findViewById<View>(R.id.tradein_promo_view).setOnClickListener {
                startActivity(TradeInPromoActivity.getIntent(context, "LAKU6EMANGASIX"))
            }
        }
    }

    private fun showToast(message: String, actionText: String, listener: View.OnClickListener) {
        Toaster.build(requireView(),
            message,
            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionText, listener).show()
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
        DaggerTradeInComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomePageFragmentVM> {
        return TradeInHomePageFragmentVM::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomePageFragmentVM
    }

    companion object {
        const val CACHE_ID = "Trade in cache id"
        fun getFragmentInstance(cacheId : String): Fragment {
            val bundle = Bundle().apply {
                putString(CACHE_ID, cacheId)
            }
            return TradeInHomePageFragment().apply {
                arguments = bundle
            }
        }
    }

    private fun updateChooseAddressWidget() {
        chooseAddressWidget?.updateWidget()
    }

    private fun checkAddressUpdate() {
        context?.let {
            if (userAddressData != null) {
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(it, userAddressData!!)) {
                    userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
//                    showLoadingWithRefresh()
                }
            }
        }
    }
    override fun onLocalizingAddressUpdatedFromWidget() {
        updateChooseAddressWidget()
        checkAddressUpdate()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {

    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressWidget?.hide()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {

    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return TradeinConstants.TRADE_IN_HOST_SOURCE
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return TradeinConstants.TRADE_IN_HOST_TRACKING_SOURCE
    }

    override fun onLocalizingAddressLoginSuccess() {

    }
}