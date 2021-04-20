package com.tokopedia.power_merchant.subscribe.view_old.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.local.model.TickerUiModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantFreeShippingTracker
import com.tokopedia.power_merchant.subscribe.view.activity.SubscriptionActivityInterface
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.ContentSliderBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PMNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.model.ContentSliderUiModel
import com.tokopedia.power_merchant.subscribe.view_old.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view_old.activity.PowerMerchantTermsActivity
import com.tokopedia.power_merchant.subscribe.view_old.bottomsheets.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view_old.constant.PowerMerchantUrl.URL_FREE_SHIPPING_INTERIM_PAGE
import com.tokopedia.power_merchant.subscribe.view_old.model.PMSettingAndShopInfoUiModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMStatusAndSettingUiModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantFreeShippingStatus
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.HideLoading
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.ShowLoading
import com.tokopedia.power_merchant.subscribe.view_old.viewmodel.PmSubscribeViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.*
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.view.*
import javax.inject.Inject

class PowerMerchantSubscribeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModel: PmSubscribeViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private var bottomSheetCancel: PowerMerchantCancelBottomSheet? = null

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build().inject(this)
        }
    }

    companion object {
        fun createInstance() = PowerMerchantSubscribeFragment()
        const val ACTIVATE_INTENT_CODE = 123
        const val AUTOEXTEND_INTENT_CODE = 321
        const val TURN_OFF_AUTOEXTEND_INTENT_CODE = 322
        const val FREE_SHIPPING_INTENT_CODE = 323
        const val MINIMUM_SCORE_ACTIVATE_REGULAR = 75
        const val MINIMUM_SCORE_ACTIVATE_IDLE = 65

        private const val APPLINK_PARAMS_KYC = "${KYCConstant.PARAM_PROJECT_ID}=${KYCConstant.MERCHANT_KYC_PROJECT_ID}"
        const val APPLINK_POWER_MERCHANT_KYC = "${ApplinkConst.KYC_NO_PARAM}?$APPLINK_PARAMS_KYC"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_power_merchant_subscribe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActivatePowerMerchant()
        observeGetFreeShippingDetail()
        observeGetPmStatusInfo()
        observeViewState()

        setupCallToActionBtn()
        setupFreeShippingError()

        viewModel.getPmStatusInfo()
    }

    private fun setupCallToActionBtn() {
        btnCallToAction.apply {
            val backgroundColor = if (context?.isDarkMode() == true) {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50)
            } else {
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
            setBackgroundColor(backgroundColor)
        }
    }

    private fun setupFreeShippingError() {
        freeShippingError.apply {
            refreshBtn?.setOnClickListener {
                if (!progressState) {
                    progressState = true
                    viewModel.getFreeShippingStatus()
                }
            }
        }
    }

    private fun cancelMembership() {
        redirectToPMCancellationQuestionnairePage()
    }

    private fun onSuccessCancelMembership() {
        showToasterCancellationSuccess()
        refreshData()
    }

    private fun refreshData() {
        viewModel.getPmStatusInfo()
    }

    private fun showLoading() {
        mainContainer.hide()
        btnCallToAction.hide()
        progressBar.show()
    }

    private fun hideLoading() {
        mainContainer.show()
        progressBar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
    }

    override fun onResume() {
        super.onResume()
        trackOpenScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVATE_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.onActivatePmSuccess()
        } else if (requestCode == AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.onActivatePmSuccess()
        } else if (requestCode == TURN_OFF_AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            onSuccessCancelMembership()
        } else if (requestCode == FREE_SHIPPING_INTENT_CODE) {
            refreshData()
        }
    }

    private fun trackOpenScreen() {
        powerMerchantTracking.eventOpenScreen(screenName)
    }

    private fun showEmptyState(throwable: Throwable) {
        NetworkErrorHelper.showEmptyState(context, view, ErrorHandler.getErrorMessage(context, throwable)) {
            refreshData()
        }
    }

    private fun observeActivatePowerMerchant() {
        observe(viewModel.onActivatePmSuccess) {
            if (it is Success) {
                showRegistrationSuccessBottomSheet()
                refreshData()
            }
        }
    }

    private fun observeGetFreeShippingDetail() {
        observe(viewModel.getPmFreeShippingStatusResult) {
            when (it) {
                is Success -> {
                    val freeShipping = it.data

                    if (freeShipping.isTransitionPeriod && !freeShipping.isActive) {
                        hideFreeShippingWidget()
                    } else {
                        trackFreeShippingImpression(freeShipping)
                        showFreeShippingWidget(freeShipping)
                    }
                }
                is Fail -> showFreeShippingError()
            }
        }
    }

    private fun showFreeShippingWidget(freeShipping: PowerMerchantFreeShippingStatus) {
        freeShippingError.hide()
        freeShippingLayout.apply {
            onClickListener = {
                openFreeShippingPage()
                trackFreeShippingClick(freeShipping)
            }
            tracker = powerMerchantTracking
            show(freeShipping)
        }
    }

    private fun hideFreeShippingWidget() {
        freeShippingError.hide()
        freeShippingLayout.hide()
    }

    private fun showFreeShippingError() {
        freeShippingLayout.hide()
        freeShippingError.apply {
            progressState = false
            show()
        }
    }

    private fun trackFreeShippingImpression(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendImpressionFreeShipping(
                userSessionInterface,
                freeShipping
        )
    }

    private fun trackFreeShippingClick(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendClickFreeShipping(
                userSessionInterface,
                freeShipping
        )
    }

    private fun observeGetPmStatusInfo() {
        observe(viewModel.getPmStatusInfoResult) {
            when (it) {
                is Success -> onSuccessGetPmInfo(it.data)
                is Fail -> showEmptyState(it.throwable)
            }
        }
    }

    private fun observeViewState() {
        observe(viewModel.viewState) {
            when (it) {
                is ShowLoading -> showLoading()
                is HideLoading -> hideLoading()
            }
        }
    }

    private fun setupCancelBottomSheet(powerMerchantStatus: PowerMerchantStatus) {
        val shopStatusModel = powerMerchantStatus.goldGetPmOsStatus.result.data

        bottomSheetCancel = PowerMerchantCancelBottomSheet.newInstance(
                shopStatusModel.powerMerchant.expiredTime,
                powerMerchantStatus.freeShippingEnabled
        )
        bottomSheetCancel?.setListener(object : PowerMerchantCancelBottomSheet.BottomSheetCancelListener {
            override fun onClickCancelButton() {
                onClickCancelMembership()
                bottomSheetCancel?.dismiss()
            }

            override fun onClickBackButton() {
                trackClickBackCancelMembership()
            }
        })
    }

    private fun trackClickBackCancelMembership() {
        powerMerchantTracking.eventClickBackCancelMembership()
    }

    private fun onClickCancelMembership() {
        trackClickCancelMembershipPopUp()
        cancelMembership()
    }

    private fun redirectToPMCancellationQuestionnairePage() {
        context?.let {
            val intent = PMCancellationQuestionnaireActivity.newInstance(it)
            startActivityForResult(intent, TURN_OFF_AUTOEXTEND_INTENT_CODE)
        }
    }

    private fun showToasterCancellationSuccess() {
        view?.let {
            val message = getString(R.string.pm_cancellation_success)
            val actionLabel = getString(R.string.power_merchant_ok_label)
            Toaster.make(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionLabel)
        }
    }

    private fun openFreeShippingPage() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW,
                URL_FREE_SHIPPING_INTERIM_PAGE)
        startActivityForResult(intent, FREE_SHIPPING_INTENT_CODE)
    }

    private fun showBottomSheetCancel() {
        bottomSheetCancel?.show(childFragmentManager)
    }

    private fun onSuccessGetPmInfo(data: PMStatusAndSettingUiModel) {
        val periodType = data.pmSettingAndShopInfo.pmSetting.periodeType
        if (periodType != PeriodType.COMMUNICATION_PERIOD) {
            showPmRevamp(data.pmSettingAndShopInfo)
            return
        }

        if (data.pmStatus == null) {
            showEmptyState(RuntimeException("Power merchant status must not be null"))
            return
        }

        val powerMerchantStatus = data.pmStatus
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        setupCancelBottomSheet(powerMerchantStatus)

        if (shopStatus.isPowerMerchantInactive()) {
            showRegistrationView(powerMerchantStatus)
        } else {
            showMembershipView(powerMerchantStatus)
            viewModel.getFreeShippingStatus()
        }

        showCTAButton(powerMerchantStatus)
        showTicker(data.pmSettingAndShopInfo)
    }

    private fun showPmRevamp(pmSettingAndShopInfo: PMSettingAndShopInfoUiModel) {
        (activity as? SubscriptionActivityInterface)?.switchToPmRevampPage(pmSettingAndShopInfo.pmSetting)
    }

    private fun showMembershipView(powerMerchantStatus: PowerMerchantStatus) {
        val shopScore = powerMerchantStatus.shopScore.data.value

        membershipLayout.show(powerMerchantStatus, powerMerchantTracking) {
            onClickRegister(shopScore)
        }
        featureLayout.show(powerMerchantStatus, powerMerchantTracking)
        freeShippingLayout.show()
        registrationLayout.hide()
        benefitLayout.hide()
    }

    private fun showRegistrationView(powerMerchantStatus: PowerMerchantStatus) {
        registrationLayout.show(powerMerchantStatus, powerMerchantTracking)
        featureLayout.show(powerMerchantStatus, powerMerchantTracking)
        benefitLayout.show(powerMerchantTracking)
        freeShippingLayout.hide()
        membershipLayout.hide()
    }

    private fun showCTAButton(powerMerchantStatus: PowerMerchantStatus) {
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data
        val shopScore = powerMerchantStatus.shopScore.data.value

        when {
            shopStatus.isPowerMerchantInactive() -> showRegisterBtn(shopScore)
            shopStatus.isPowerMerchantRegistered() -> showCancelMembershipBtn()
            else -> btnCallToAction.hide()
        }
    }

    private fun showRegisterBtn(shopScore: Int) {
        btnRegister.text = getString(R.string.power_merchant_register_now)

        btnCallToAction.setOnClickListener {
            trackClickRegister()
            onClickRegister(shopScore)
        }

        btnRegister.show()
        btnCallToAction.show()
        textCancelMembership.hide()
    }

    private fun trackClickRegister() {
        powerMerchantTracking.eventClickUpgradeShop()
    }

    private fun showCancelMembershipBtn() {
        val cancelMembershipTxt = getString(R.string.power_merchant_cancel_membership)
        textCancelMembership.text = MethodChecker.fromHtml(cancelMembershipTxt)

        btnCallToAction.setOnClickListener {
            trackClickCancelMembership()
            showBottomSheetCancel()
        }

        btnRegister.hide()
        btnCallToAction.show()
        textCancelMembership.show()
    }

    private fun trackClickCancelMembership() {
        powerMerchantTracking.eventClickCancelMembership()
    }

    private fun trackClickCancelMembershipPopUp() {
        powerMerchantTracking.eventClickCancelMembershipPopUp()
    }

    private fun onClickRegister(shopScore: Int) {
        goToTermsAndConditionPage(shopScore)
    }

    private fun goToTermsAndConditionPage(shopScore: Int) {
        context?.let {
            val intent = PowerMerchantTermsActivity.createIntent(it, shopScore)
            startActivityForResult(intent, ACTIVATE_INTENT_CODE)
        }
    }

    private fun showTicker(data: PMSettingAndShopInfoUiModel) {
        val pmSettings = data.pmSetting
        view?.tickerPmCommunicationPeriod?.run {
            visible()
            val tickersData = pmSettings.tickers.map { ticker ->
                val tickerType: Int = when (ticker.type) {
                    TickerUiModel.TYPE_ERROR -> Ticker.TYPE_ERROR
                    TickerUiModel.TYPE_WARNING -> Ticker.TYPE_WARNING
                    else -> Ticker.TYPE_ANNOUNCEMENT
                }
                return@map TickerData(ticker.title, ticker.text, tickerType, true, ticker)
            }

            if (tickersData.isEmpty()) {
                gone()
                return@run
            }

            val adapter = TickerPagerAdapter(context, tickersData)
            addPagerView(adapter, tickersData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    if (itemData is TickerUiModel) {
                        if (itemData.isInterruptPopup) {
                            if (data.shopInfo.isNewSeller) {
                                showNewSellerInterruptPopup()
                            } else {
                                showExistingSellerInterruptPopup()
                            }
                            return
                        }
                    }
                    RouteManager.route(context, linkUrl.toString())
                }
            })
        }
    }

    private fun showNewSellerInterruptPopup() {
        val bottomSheet = ContentSliderBottomSheet.createInstance(childFragmentManager)
        if (bottomSheet.isAdded) return
        val slideItems = listOf(ContentSliderUiModel(
                title = getString(R.string.pm_new_pm_inf_title),
                description = getString(R.string.pm_new_pm_inf_description),
                imgUrl = PMConstant.Images.PM_NEW_REQUIREMENT
        ))
        val emptyTitle = ""
        bottomSheet.setContent(emptyTitle, slideItems)
        bottomSheet.setOnPrimaryCtaClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.setOnSecondaryCtaClickListener {
            openInterruptPage()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun showExistingSellerInterruptPopup() {
        val bottomSheet = ContentSliderBottomSheet.createInstance(childFragmentManager)
        if (bottomSheet.isAdded) return

        val title = getString(R.string.pm_power_merchant_slider_title)
        val slideItems = listOf(
                ContentSliderUiModel(
                        title = getString(R.string.pm_power_merchant_new_term_title),
                        description = getString(R.string.pm_power_merchant_new_term_description, PMConstant.TRANSITION_PERIOD_START_DATE),
                        imgUrl = PMConstant.Images.PM_NEW_REQUIREMENT
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_integrated_with_reputation_title),
                        description = getString(R.string.pm_integrated_with_reputation_description, PMConstant.TRANSITION_PERIOD_START_DATE),
                        imgUrl = PMConstant.Images.PM_INTEGRATED_WITH_REPUTATION
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_new_benefits_title),
                        description = getString(R.string.pm_new_benefits_description),
                        imgUrl = PMConstant.Images.PM_NEW_BENEFITS
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_new_benefits_title),
                        description = getString(R.string.pm_new_schema_description, PMConstant.TRANSITION_PERIOD_START_DATE),
                        imgUrl = PMConstant.Images.PM_NEW_SCHEMA
                )
        )
        bottomSheet.setContent(title, slideItems)
        bottomSheet.setOnPrimaryCtaClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.setOnSecondaryCtaClickListener {
            openInterruptPage()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun showRegistrationSuccessBottomSheet() {
        val notifTitle = getString(R.string.pm_registration_success_title)
        val notifDescription = getString(R.string.pm_registration_success_description_end_game)
        val notifImage = PMConstant.Images.PM_REGISTRATION_SUCCESS
        showNotificationBottomSheet(notifTitle, notifDescription, notifImage) {
            openInterruptPage()
        }
    }

    private fun openInterruptPage() {
        RouteManager.route(context, PMConstant.Urls.SHOP_SCORE_INTERRUPT_PAGE)
    }

    private fun showNotificationBottomSheet(title: String, description: String, imgUrl: String,
                                            callback: (() -> Unit)? = null) {
        val notifBottomSheet = PMNotificationBottomSheet.createInstance(title, description, imgUrl)
        if (!notifBottomSheet.isAdded) {
            val ctaText = getString(R.string.pm_learn_new_pm)
            notifBottomSheet.setPrimaryButtonClickListener(ctaText) {
                callback?.invoke()
            }
            Handler().post {
                notifBottomSheet.show(childFragmentManager)
            }
        }
    }
}