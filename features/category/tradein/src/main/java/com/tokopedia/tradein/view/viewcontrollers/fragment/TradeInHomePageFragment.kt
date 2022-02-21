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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
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
import com.tokopedia.tradein.TradeInUtils
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.view.viewcontrollers.activity.TradeInPromoActivity
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInExchangeMethodBS
import com.tokopedia.tradein.viewmodel.TradeInHomePageFragmentVM
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class TradeInHomePageFragment : BaseViewModelFragment<TradeInHomePageFragmentVM>(),
    ChooseAddressWidget.ChooseAddressWidgetListener,
    TradeInEducationalPageFragment.OnDoTradeInClick,
    TradeInExchangeMethodBS.OnLogisticSelected{

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInHomePageFragmentVM
    private lateinit var tradeInHomePageVM: TradeInHomePageVM
    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var userAddressData: LocalCacheModel? = null
    private var isAnyLogisticAvailable = false

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
                findViewById<ConstraintLayout>(com.tokopedia.tradein.R.id.parent_collapse).let { layout ->
                    if (layout.isVisible) {
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
                findViewById<Typography>(R.id.slashed_price).let { price ->
                    price.text =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, true)
                    price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                findViewById<Typography>(R.id.product_price).text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, true)
                findViewById<Typography>(R.id.product_name).text = it.productName
                if (!it.productImage.isNullOrEmpty()) {
                    (findViewById<ImageUnify>(R.id.product_image)).setImageUrl(it.productImage!!)
                }
                if (!it.shopBadge.isNullOrEmpty()) {
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
            viewModel.getTradeInPDPData()?.apply {
                userAddressData?.let { address ->
                    viewModel.getTradeInDetail(it, productPrice, address)
                }
            }
        })
        tradeInHomePageVM.is3PLSelected.observe(viewLifecycleOwner, Observer {
            if(viewModel.logisticData.isNotEmpty()){
                setPriceWithLogistic(it, viewModel.logisticData)
            }
        })
        viewModel.tradeInDetailLiveData.observe(viewLifecycleOwner, Observer {
            onTradeInDetailSuccess(it)
        })
        viewModel.getProgBarVisibility().observe(viewLifecycleOwner, Observer {
            if (it)
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
                    refreshPage()
                }
            }
        })
    }

    private fun setUpLaku6Data(laku6DeviceModel: Laku6DeviceModel?) {
        view?.apply {
            laku6DeviceModel?.let {
                findViewById<Typography>(R.id.session_id).text =
                    getString(com.tokopedia.tradein.R.string.tradein_session_id, it.sessionId)
            }
        }
    }

    private fun onTradeInDetailSuccess(tradeInDetailModel: TradeInDetailModel) {
        tradeInDetailModel.getTradeInDetail.let { tradeInDetail ->
            setUpLogisticOptions(tradeInDetail.logisticOptions, tradeInDetail.logisticMessage)
            view?.apply {
                (findViewById<ImageUnify>(R.id.banner_image)).setImageUrl(tradeInDetail.bannerURL)
                findViewById<Typography>(R.id.model_text).text = tradeInDetail.deviceAttribute.model
                findViewById<Typography>(R.id.imei_text).text = tradeInDetail.deviceAttribute.imei.firstOrNull() ?: "-"
                tradeInDetail.activePromo.let { code ->
                    findViewById<Typography>(R.id.promo_title).text = code.title
                    findViewById<Typography>(R.id.promo_detail).text = code.subtitle
                    findViewById<View>(R.id.tradein_promo_view).setOnClickListener {
                        startActivity(
                            TradeInPromoActivity.getIntent(
                                context,
                                code.code
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setUpLogisticOptions(
        logisticOptions: ArrayList<TradeInDetailModel.GetTradeInDetail.LogisticOption>,
        logisticMessage: String
    ) {
        isAnyLogisticAvailable = false
        for (logistic in logisticOptions) {
            if (logistic.isAvailable) {
                isAnyLogisticAvailable = true
                break
            }
        }
        for(logistic in logisticOptions){
            if(logistic.isPreferred)
                tradeInHomePageVM.updateLogistics(logistic.is3PL)
        }
        view?.apply {
            if (isAnyLogisticAvailable) {
                findViewById<View>(R.id.exchange_layout).setOnClickListener {
                    val bottomSheet = TradeInExchangeMethodBS.newInstance(
                        logisticOptions,
                        tradeInHomePageVM.is3PLSelected.value ?: false,
                        logisticMessage
                    )
                    bottomSheet.setOnLogisticSelected(this@TradeInHomePageFragment)
                    bottomSheet.show(childFragmentManager, "")
                }
            } else {
                tradeInHomePageVM.updateLogistics(false)
            }
        }
    }

    private fun setPriceWithLogistic(
        is3Pl: Boolean,
        logisticData: ArrayList<TradeInDetailModel.GetTradeInDetail.LogisticOption>
    ) {
        view?.apply {
            if (isAnyLogisticAvailable) {
                findViewById<Typography>(R.id.unavailable_exchange_text).hide()
                (findViewById<IconUnify>(R.id.iv_chevron)).setImage(null, newLightEnable = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                findViewById<Typography>(R.id.exchange_price_text).show()
                findViewById<Typography>(R.id.exchange_text).show()
                for (logistic in logisticData) {
                    if (is3Pl == logistic.is3PL) {
                        setUpPriceView(logistic, true)
                    }
                }
            } else {
                findViewById<Typography>(R.id.unavailable_exchange_text).show()
                (findViewById<IconUnify>(R.id.iv_chevron)).setImage(null, newLightEnable = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                findViewById<Typography>(R.id.exchange_price_text).hide()
                findViewById<Typography>(R.id.exchange_text).hide()
                setUpPriceView(logisticData.first(), false)
                (findViewById<UnifyButton>(R.id.btn_continue)).setEnabled(false)
            }
        }
    }

    private fun setUpPriceView(logistic: TradeInDetailModel.GetTradeInDetail.LogisticOption, showTimer : Boolean = false) {
        view?.apply {
            findViewById<Typography>(R.id.discounted_price).text = logistic.finalPriceFmt
            findViewById<Typography>(R.id.estimated_total).text = logistic.finalPriceFmt
            findViewById<Typography>(R.id.exchange_text).text = logistic.title
            if (logistic.isDiagnosed)
                findViewById<Typography>(R.id.exchange_price_text).text =
                    logistic.diagnosticPriceFmt
            else
                findViewById<Typography>(R.id.exchange_price_text).text = logistic.estimatedPriceFmt

            findViewById<Typography>(R.id.phone_detail_estimated_price).text =
                logistic.diagnosticPriceFmt.ifEmpty { "-" }
            findViewById<Typography>(R.id.estimated_price).text = getString(
                com.tokopedia.tradein.R.string.tradein_minus_asterick,
                logistic.diagnosticPriceFmt
            )
            findViewById<Label>(R.id.label_discount).text = logistic.discountPercentageFmt
            findViewById<TimerUnifySingle>(R.id.tradein_count_down).let { countDownView ->
                if (showTimer || logistic.expiryTime.isEmpty()) {
                    TradeInUtils.parseData(logistic.expiryTime)?.let { parsedDate ->
                        countDownView?.show()
                        findViewById<Typography>(R.id.tradein_timer_text).show()
                        findViewById<Typography>(R.id.tradein_product_info_text).hide()
                        val parsedCalendar: Calendar = Calendar.getInstance()
                        parsedCalendar.time = parsedDate
                        countDownView?.targetDate = parsedCalendar
                        countDownView?.onFinish = {
                            refreshPage()
                        }
                    }
                } else {
                    findViewById<Typography>(R.id.tradein_timer_text).hide()
                    findViewById<Typography>(R.id.tradein_product_info_text).show()
                    countDownView?.hide()
                }
            }
        }
    }

    private fun showToast(message: String, actionText: String, listener: View.OnClickListener) {
        Toaster.build(
            requireView(),
            message,
            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionText, listener
        ).show()
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
        fun getFragmentInstance(cacheId: String): Fragment {
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
                    refreshPage()
                }
            }
        }
    }

    fun refreshPage() {
        viewModel.startProgressBar()
        tradeInHomePageVM.getDeviceModel()
    }

    override fun onLogisticSelected(is3Pl: Boolean) {
        tradeInHomePageVM.updateLogistics(is3Pl)
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