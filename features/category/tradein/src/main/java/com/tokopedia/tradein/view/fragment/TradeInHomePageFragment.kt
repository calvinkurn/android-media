package com.tokopedia.tradein.view.fragment

import com.tokopedia.imageassets.ImageUrl

import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.TradeInUtils
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.view.activity.TradeInHomePageActivity
import com.tokopedia.tradein.view.activity.TradeInPromoActivity
import com.tokopedia.tradein.view.bottomsheet.*
import com.tokopedia.tradein.viewmodel.TradeInHomePageFragmentVM
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.view.DoubleTextView
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class TradeInHomePageFragment : BaseViewModelFragment<TradeInHomePageFragmentVM>(),
    ChooseAddressWidget.ChooseAddressWidgetListener,
    TradeInEducationalPageFragment.OnDoTradeInClick,
    TradeInExchangeMethodBS.OnLogisticSelected,
    TradeInImeiBS.ActionListener{

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics
    private lateinit var viewModel: TradeInHomePageFragmentVM
    private lateinit var tradeInHomePageVM: TradeInHomePageVM
    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var userAddressData: LocalCacheModel? = null
    private var isAnyLogisticAvailable = false
    private var isFraud = false

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
            userAddressData = context?.let {
                try {
                    ChooseAddressUtils.getLocalizingAddressData(it)
                } catch (e: Throwable) {
                    ChooseAddressConstant.emptyAddress
                }
            } ?: ChooseAddressConstant.emptyAddress
            findViewById<Typography>(R.id.typo_info_exchange).setOnClickListener {
                showTradeInInfoBottomsheet(childFragmentManager, context)
            }
            arguments?.getString(CACHE_ID, "")?.let {
                setUpPdpData(it)
            }

            chooseAddressWidget = findViewById(R.id.tradein_choose_address_widget)
            chooseAddressWidget?.bindChooseAddress(this@TradeInHomePageFragment)
            findViewById<View>(R.id.collapse_view).setOnClickListener {
                findViewById<ConstraintLayout>(com.tokopedia.tradein.R.id.parent_collapse).let { layout ->
                    if (layout.isVisible) {
                        layout.hide()
                    } else {
                        layout.show()
                        tradeInAnalytics.expandDropDown(tradeInHomePageVM.is3PLSelected.value ?: false, tradeInHomePageVM.imei, tradeInHomePageVM.isDiagnosed)
                    }
                }
            }
        }
    }

    override fun onClick() {
        tradeInAnalytics.clickEducationalPage()
        if (isFraud) {
            (activity as? TradeInHomePageActivity)?.onClick()
        } else {
            view?.findViewById<View>(R.id.educational_frame_content_layout)?.hide()
            view?.findViewById<View>(R.id.initial_price_navToolbar)?.show()
        }
    }

    override fun onBackClick() {
        if (isFraud) {
            (activity as? TradeInHomePageActivity)?.onBackClick()
        } else {
            view?.findViewById<View>(R.id.educational_frame_content_layout)?.hide()
            view?.findViewById<View>(R.id.initial_price_navToolbar)?.show()
        }
    }


    private fun setUpPdpData(id: String) {
        view?.apply {
            tradeInHomePageVM.getPDPData(TradeInPDPHelper.getDataFromPDP(context, id))?.let {
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
                    (findViewById<ImageUnify>(R.id.image_shop_badge)).show()
                    (findViewById<ImageUnify>(R.id.image_shop_badge)).setImageUrl(it.shopBadge!!)
                } else {
                    (findViewById<ImageUnify>(R.id.image_shop_badge)).hide()
                }
                findViewById<Typography>(R.id.shop_name).text = it.shopName
                findViewById<Typography>(R.id.shop_location).text = it.shopLocation
            }
        }
    }

    private fun addObservers() {
        tradeInHomePageVM.laku6DeviceModel.observe(viewLifecycleOwner, Observer {
            setUpLaku6Data(it)
            tradeInHomePageVM.data?.apply {
                userAddressData?.let { address ->
                    viewModel.getTradeInDetail(it, productPrice, address, tradeInHomePageVM.tradeInUniqueCode, tradeInHomePageVM.data?.shopID ?: "")
                }
            }
        })
        tradeInHomePageVM.is3PLSelected.observe(viewLifecycleOwner, Observer {
            if (viewModel.logisticData.isNotEmpty()) {
                setPriceWithLogistic(it, viewModel.logisticData)
            }
        })
        viewModel.tradeInDetailLiveData.observe(viewLifecycleOwner, Observer {
            if(it.getTradeInDetail.errMessage.isNotEmpty()){
                isFraud = it.getTradeInDetail.isFraud

                if(it.getTradeInDetail.errTitle.isNotEmpty()){
                    setErrorTokopedia(
                        Throwable(it.getTradeInDetail.errMessage),
                        isFraud,
                        it.getTradeInDetail.errTitle,
                        it.getTradeInDetail.errMessage.toString()
                    )
                } else {
                    tradeInAnalytics.errorScreen(it.getTradeInDetail.errMessage, viewModel.tradeInDetailLiveData.value?.getTradeInDetail?.deviceAttribute)
                    onTradeInDetailSuccess(it)
                    showToast(it.getTradeInDetail.errMessage, getString(R.string.tradein_ok), {
                        activity?.finish()
                    })
                }
            } else {
                onTradeInDetailSuccess(it)
            }
        })
        tradeInHomePageVM.getWarningMessage().observe(viewLifecycleOwner, Observer {
            setErrorTokopedia(Throwable(it), errorMessage = it)
        })
        viewModel.getProgBarVisibility().observe(viewLifecycleOwner, Observer {
            if (it)
                view?.findViewById<View>(R.id.tradein_loading_layout)?.show()
            else
                view?.findViewById<View>(R.id.tradein_loading_layout)?.hide()
        })
        tradeInHomePageVM.getErrorMessage().observe(viewLifecycleOwner, Observer {
            setErrorLaku6(it)
        })
        viewModel.getErrorMessage().observe(viewLifecycleOwner, Observer {
            setErrorTokopedia(it, errorMessage = it.localizedMessage ?: "")
        })
    }

    private fun setErrorLaku6(it: Throwable?) {
        view?.findViewById<NestedScrollView>(R.id.scroll_parent)?.hide()
        view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
            //LAKU6 errors
            setType(GlobalError.SERVER_ERROR)
            errorIllustration.hide()
            errorTitle.text = getString(com.tokopedia.tradein.R.string.tradein_cant_continue)
            errorDescription.text = it?.message
            errorAction.text = getString(R.string.tradein_pelajari_selengkapnya)
            setButtonFull(true)
            errorAction.hide()
            errorSecondaryAction.gone()
            view?.findViewById<View>(R.id.tradein_error_layout)?.show()
            view?.findViewById<DeferredImageView>(R.id.error_image_view)?.let {
                it.show()
                it.loadRemoteImageDrawable("", LAKU6_ERROR_IMAGE)
            }
        }
    }

    private fun setErrorTokopedia(it: Throwable?, isFraud : Boolean = false,
                                  errTitle : String = getString(com.tokopedia.tradein.R.string.tradein_cant_continue), errorMessage : String){
        tradeInAnalytics.errorScreen(errorMessage, viewModel.tradeInDetailLiveData.value?.getTradeInDetail?.deviceAttribute)
        view?.findViewById<NestedScrollView>(R.id.scroll_parent)?.hide()
        view?.findViewById<GlobalError>(R.id.home_global_error)?.run {

            errorAction.hide()
            //Tokopedia Backend errors
            when (it) {
                is UnknownHostException, is SocketTimeoutException -> {
                    setType(GlobalError.NO_CONNECTION)
                }
                is IllegalStateException -> {
                    setType(GlobalError.PAGE_FULL)
                }
                else -> {
                    setType(GlobalError.SERVER_ERROR)
                    errorDescription.text = it?.message
                    errorTitle.text = errTitle
                }
            }
            if (isFraud) {
                errorDescription.text = getString(R.string.tradein_fraud_description)
                errorIllustration.hide()
                view?.findViewById<DeferredImageView>(R.id.error_image_view)?.let {
                    it.show()
                    it.loadRemoteImageDrawable("", FRAUD_ERROR_IMAGE)
                }
            } else {
                errorIllustration.show()
                view?.findViewById<View>(R.id.error_image_view)?.hide()
            }
            view?.findViewById<View>(R.id.tradein_error_layout)?.show()
            errorAction.hide()
        }
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
        view?.findViewById<NestedScrollView>(R.id.scroll_parent)?.show()
        tradeInDetailModel.getTradeInDetail.let { tradeInDetail ->
            setUpLogisticOptions(tradeInDetail.logisticOptions, tradeInDetail.logisticMessage)
            view?.apply {
                (findViewById<ImageUnify>(R.id.banner_image)).setImageUrl(tradeInDetail.bannerURL)
                tradeInDetail.activePromo.let { code ->
                    findViewById<Typography>(R.id.promo_title).text = code.title
                    findViewById<Typography>(R.id.promo_detail).text = code.subtitle
                    findViewById<View>(R.id.tradein_promo_view).setOnClickListener {
                        tradeInAnalytics.clickPromoBanner(tradeInHomePageVM.is3PLSelected.value ?: false, tradeInHomePageVM.imei, tradeInHomePageVM.isDiagnosed, code.code)
                        startActivity(
                            TradeInPromoActivity.getIntent(
                                context,
                                code.code
                            )
                        )
                    }
                }

                findViewById<View>(R.id.tradein_promo_view).isVisible = tradeInDetail.activePromo.title.isNotEmpty()
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
        for (logistic in logisticOptions) {
            if (logistic.isPreferred)
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
                    bottomSheet.tradeInAnalytics = tradeInAnalytics
                    bottomSheet.setOnLogisticSelected(this@TradeInHomePageFragment)
                    bottomSheet.show(childFragmentManager, "")
                }
            } else {
                findViewById<View>(R.id.exchange_layout).setOnClickListener {}
            }
        }
    }

    private fun setPriceWithLogistic(
        is3Pl: Boolean,
        logisticData: ArrayList<TradeInDetailModel.GetTradeInDetail.LogisticOption>
    ) {
            if (isAnyLogisticAvailable) {
                setUpViewIfLogisticsAvailable(is3Pl, logisticData)
            } else {
                setUpViewIfLogisticsUnavailable(logisticData)
            }
    }

    private fun setUpViewIfLogisticsAvailable(
        is3Pl: Boolean,
        logisticData: ArrayList<TradeInDetailModel.GetTradeInDetail.LogisticOption>
    ) {
        view?.apply {
            findViewById<Typography>(R.id.unavailable_exchange_text).hide()
            (findViewById<IconUnify>(R.id.iv_chevron)).setImage(
                null,
                newLightEnable = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                )
            )
            findViewById<Typography>(R.id.exchange_price_text).show()
            findViewById<Typography>(R.id.exchange_text).show()
            for (logistic in logisticData) {
                if (is3Pl == logistic.is3PL) {
                    tradeInHomePageVM.tradeInPrice = logistic.diagnosticPriceFmt
                    tradeInHomePageVM.tradeInPriceDouble = logistic.diagnosticPrice
                    tradeInHomePageVM.finalPriceDouble = logistic.finalPrice
                    setUpPriceView(logistic, logistic.isDiagnosed)
                    if (logistic.isDiagnosed) {
                        findViewById<UnifyButton>(R.id.btn_continue).text =
                            getString(com.tokopedia.tradein.R.string.tradein_continue_payment)
                        findViewById<UnifyButton>(R.id.btn_continue).setOnClickListener {
                            tradeInAnalytics.clickTradeInStartPage(
                                is3PL = logistic.is3PL,
                                phoneType = tradeInHomePageVM.laku6DeviceModel.value?.model ?: "",
                                priceRange = logistic.estimatedPriceFmt,
                                imei = tradeInHomePageVM.imei,
                                isDiagnosed = logistic.isDiagnosed
                            )
                            startLaku6Testing()
                        }
                    } else {
                        findViewById<UnifyButton>(R.id.btn_continue).text =
                            getString(com.tokopedia.tradein.R.string.tradein_mulai_tes_hp)
                        findViewById<UnifyButton>(R.id.btn_continue).setOnClickListener {
                            tradeInAnalytics.clickTradeInStartPage(
                                is3PL = logistic.is3PL,
                                phoneType = tradeInHomePageVM.laku6DeviceModel.value?.model ?: "",
                                priceRange = logistic.estimatedPriceFmt,
                                imei = tradeInHomePageVM.imei,
                                isDiagnosed = logistic.isDiagnosed
                            )
                            if(viewModel.tradeInDetailLiveData.value?.getTradeInDetail?.deviceAttribute?.imei?.firstOrNull()?.isNotEmpty() == true)
                                startLaku6Testing()
                            else {
                                openImeiBottomSheet()
                            }
                        }
                    }

                }
            }
        }
    }

    private fun setUpViewIfLogisticsUnavailable(
        logisticData: ArrayList<TradeInDetailModel.GetTradeInDetail.LogisticOption>
    ) {
        view?.apply {
            findViewById<Typography>(R.id.unavailable_exchange_text).show()
            (findViewById<IconUnify>(R.id.iv_chevron)).setImage(
                null,
                newLightEnable = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_32
                )
            )
            findViewById<Typography>(R.id.exchange_price_text).hide()
            findViewById<Typography>(R.id.exchange_text).hide()
            findViewById<UnifyButton>(R.id.btn_continue).text =
                getString(com.tokopedia.tradein.R.string.tradein_similar_products)
            (findViewById<UnifyButton>(R.id.btn_continue)).setOnClickListener {
                tradeInAnalytics.viewCoverageAreaBottomSheet()
                val bottomSheet = TradeInOutsideCoverageBottomSheet
                    .newInstance(
                        tradeInHomePageVM.data?.productName
                            ?: ""
                    )
                bottomSheet.tradeInAnalytics = tradeInAnalytics
                bottomSheet.show(childFragmentManager, "")
            }
            logisticData.firstOrNull()?.let {
                setUpPriceView(it, it.isDiagnosed)
            }
            showToast(
                getString(com.tokopedia.tradein.R.string.tradein_no_delivery_area),
                getString(com.tokopedia.tradein.R.string.tradein_oke),
                View.OnClickListener {
                },
                Snackbar.LENGTH_INDEFINITE
            )
        }
    }

    private fun setUpPriceView(
        logistic: TradeInDetailModel.GetTradeInDetail.LogisticOption,
        showTimer: Boolean = false
    ) {
        tradeInHomePageVM.isDiagnosed = logistic.isDiagnosed
        tradeInAnalytics.openTradeInStartPage(logistic.is3PL, tradeInHomePageVM.imei, logistic.isDiagnosed)
        view?.apply {
            tradeInHomePageVM.campaginTagId = logistic.campaignTagId
            setUpDiagnosticReviewData(logistic.diagnosticReview)
            findViewById<IconUnify>(R.id.iv_info).setOnClickListener {
                val bottomSheet = TradeInDiagnosticReviewBS.newInstance(logistic.diagnosticReview)
                bottomSheet.show(childFragmentManager, "")
            }
            findViewById<Typography>(R.id.discounted_price).text = logistic.finalPriceFmt
            findViewById<Typography>(R.id.estimated_total).text = logistic.finalPriceFmt
            findViewById<Typography>(R.id.exchange_text).text = logistic.title
            if (logistic.isDiagnosed) {
                findViewById<Typography>(R.id.exchange_price_text).text =
                    logistic.diagnosticPriceFmt
                findViewById<Typography>(R.id.estimated_price_text).text = getString(com.tokopedia.tradein.R.string.tradein_phone_price)
                findViewById<Typography>(R.id.estimated_total_text).text = getString(com.tokopedia.tradein.R.string.tradein_final_total)
                findViewById<View>(R.id.help_device_text).hide()
            }
            else {
                findViewById<Typography>(R.id.exchange_price_text).text = logistic.estimatedPriceFmt
                findViewById<Typography>(R.id.estimated_price_text).text = getString(com.tokopedia.tradein.R.string.tradein_estimate_price)
                findViewById<Typography>(R.id.estimated_total_text).text = getString(com.tokopedia.tradein.R.string.tradein_estimated_total)
                findViewById<View>(R.id.help_device_text).show()
            }
            findViewById<Typography>(R.id.estimated_price).text = getString(
                com.tokopedia.tradein.R.string.tradein_minus,
                logistic.diagnosticPriceFmt
            )
            findViewById<Label>(R.id.label_discount).text = logistic.discountPercentageFmt
            findViewById<TimerUnifySingle>(R.id.tradein_count_down).let { countDownView ->
                if (showTimer && logistic.expiryTime.isNotEmpty()) {
                    findViewById<UnifyButton>(R.id.btn_continue).isEnabled = !TradeInUtils.isExpiryTimeOver(logistic.expiryTime)
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

    private fun setUpDiagnosticReviewData(diagnosticReview: ArrayList<TradeInDetailModel.GetTradeInDetail.LogisticOption.DiagnosticReview>) {
        val textSize = TradeinConstants.TEXT_SIZE_14F
        view?.findViewById<LinearLayout>(R.id.linear_layout_hp_kamu)?.removeAllViews()
        if(diagnosticReview.size>=TradeinConstants.TRADE_IN_DIAGNOSTIC_REVIEW_LIMIT) {
            for (i in 0..2) {
                val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                doubleTextView.apply {
                    setTopText(diagnosticReview[i].field)
                    setTopTextSize(textSize)
                    setTopTextColor(
                        MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                        )
                    )
                    setBottomTextSize(textSize)
                    setBottomTextColor(
                        MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                        )
                    )
                    setBottomTextStyle("bold")
                    setBottomText(diagnosticReview[i].value)
                    setBottomGravity(Gravity.END)
                    setBottomTextGravity(Gravity.END)
                }
                view?.findViewById<LinearLayout>(R.id.linear_layout_hp_kamu)
                    ?.addView(doubleTextView)
            }
        }
    }

    private fun openImeiBottomSheet() {
        val fragment = TradeInImeiBS.newInstance(tradeInHomePageVM.laku6DeviceModel.value)
        fragment.tradeInAnalytics = tradeInAnalytics
        fragment.setActionListener(this@TradeInHomePageFragment)
        fragment.show(childFragmentManager, tag)
    }

    override fun onImeiButtonClick(imei: String) {
        tradeInHomePageVM.imei = imei
        startLaku6Testing()
    }

    private fun startLaku6Testing() {
        tradeInHomePageVM.startLaku6Testing(viewModel.tradeInDetailLiveData.value?.getTradeInDetail?.deviceAttribute)
    }

    private fun showToast(
        message: String,
        actionText: String,
        listener: View.OnClickListener,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        Toaster.build(
            requireView(),
            message,
            duration, Toaster.TYPE_ERROR, actionText, listener
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
        const val LAKU6_ERROR_IMAGE = ImageUrl.LAKU6_ERROR_IMAGE
        const val FRAUD_ERROR_IMAGE = ImageUrl.FRAUD_ERROR_IMAGE

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
        tradeInAnalytics.clickSubmitChangeAddress(tradeInHomePageVM.is3PLSelected.value ?: false)
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

    private fun refreshPage() {
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

    override fun getEventLabelHostPage(): String {
        return (if(tradeInHomePageVM.is3PLSelected.value == true) "ditukar di indomaret" else "ditukar di alamatmu")
    }

    override fun needToTrackTokoNow(): Boolean {
        return true
    }

    override fun onClickChooseAddressTokoNowTracker() {
        tradeInAnalytics.clickAttemptChangeAddress(tradeInHomePageVM.is3PLSelected.value ?: false)
        super.onClickChooseAddressTokoNowTracker()
    }

    override fun onLocalizingAddressLoginSuccess() {

    }
}
