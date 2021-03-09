package com.tokopedia.power_merchant.subscribe.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.local.model.TickerUiModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantFreeShippingTracker
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.DeactivationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.viewmodel.SubscriptionActivityViewModel
import com.tokopedia.power_merchant.subscribe.view_old.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view_old.activity.PowerMerchantTermsActivity
import com.tokopedia.power_merchant.subscribe.view_old.bottomsheets.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view_old.bottomsheets.PowerMerchantNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view_old.bottomsheets.PowerMerchantNotificationBottomSheet.CTAMode
import com.tokopedia.power_merchant.subscribe.view_old.constant.PowerMerchantUrl.URL_FREE_SHIPPING_INTERIM_PAGE
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
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.*
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.view.*
import javax.inject.Inject

class PowerMerchantSubscribeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: PmSubscribeViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SubscriptionActivityViewModel::class.java)
    }

    private var bottomSheetCancel: PowerMerchantCancelBottomSheet? = null

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
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

        setupFreeShippingError()

        viewModel.getPmStatusInfo()
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
        //redirectToPMCancellationQuestionnairePage()
        DeactivationBottomSheet.createInstance()
                .show(childFragmentManager)
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
                showBottomSheetSuccess(it.data)
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

    private fun showBottomSheetSuccess(freeShipping: PowerMerchantFreeShippingStatus) {
        val isFreeShippingEligible = freeShipping.isEligible
        val chargePeriod = !freeShipping.isTransitionPeriod
        val showFreeShipping = isFreeShippingEligible && chargePeriod

        if (showFreeShipping) {
            trackFreeShippingSuccessBottomSheet(freeShipping)
        } else {
            trackPowerMerchantSuccessBottomSheet()
        }

        val primaryBtnLabel = if (showFreeShipping) {
            getString(R.string.power_merchant_free_shipping_learn_more)
        } else {
            getString(R.string.pm_label_bs_success_button)
        }

        val description = if (showFreeShipping) {
            getString(R.string.power_merchant_success_free_shipping_description)
        } else {
            getString(R.string.power_merchant_success_description)
        }

        val bottomSheet = PowerMerchantNotificationBottomSheet.createInstance(
                getString(R.string.power_merchant_success_title),
                description,
                R.drawable.ic_pm_registration_success,
                CTAMode.SINGLE
        )
        bottomSheet.setPrimaryButtonText(primaryBtnLabel)
        bottomSheet.setPrimaryButtonClickListener {
            if (showFreeShipping) {
                openFreeShippingPage()
                trackSuccessBottomSheetClickLearnMore(freeShipping)
            } else {
                trackClickStartSuccessBottomSheet()
            }
            bottomSheet.dismiss()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun trackClickStartSuccessBottomSheet() {
        powerMerchantTracking.eventClickStartSuccessBottomSheet()
    }

    private fun openFreeShippingPage() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW,
                URL_FREE_SHIPPING_INTERIM_PAGE)
        startActivityForResult(intent, FREE_SHIPPING_INTENT_CODE)
    }

    private fun trackFreeShippingSuccessBottomSheet(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.eventFreeShippingSuccessBottomSheet(
                userSessionInterface,
                freeShipping
        )
    }

    private fun trackPowerMerchantSuccessBottomSheet() {
        powerMerchantTracking.eventPowerMerchantSuccessBottomSheet()
    }

    private fun trackSuccessBottomSheetClickLearnMore(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendSuccessBottomSheetClickLearnMore(
                userSessionInterface,
                freeShipping
        )
    }

    private fun showBottomSheetCancel() {
        bottomSheetCancel?.show(childFragmentManager)
    }

    private fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus) {
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        setupCancelBottomSheet(powerMerchantStatus)

        if (shopStatus.isPowerMerchantInactive()) {
            showRegistrationView(powerMerchantStatus)
        } else {
            showMembershipView(powerMerchantStatus)
            viewModel.getFreeShippingStatus()
        }

        showCTAButton(powerMerchantStatus)

        showTicker()
    }

    private fun showTicker() {
        sharedViewModel.powerMerchantSettingInfo.observe(viewLifecycleOwner, Observer {
            if (it is Success) {
                view?.tickerPmCommunicationPeriod?.run {

                    val tickersData = it.data.tickers.map { ticker ->
                        val tickerType: Int = when (ticker.type) {
                            TickerUiModel.TYPE_ERROR -> Ticker.TYPE_ERROR
                            TickerUiModel.TYPE_WARNING -> Ticker.TYPE_WARNING
                            else -> Ticker.TYPE_ANNOUNCEMENT
                        }
                        return@map TickerData(ticker.title, ticker.text, tickerType, true, it)
                    }

                    if (tickersData.isEmpty()) {
                        gone()
                        return@run
                    }

                    val adapter = TickerPagerAdapter(context, tickersData)
                    addPagerView(adapter, tickersData)
                    adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            RouteManager.route(context, linkUrl.toString())
                        }
                    })
                }
            }
        })
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
}
