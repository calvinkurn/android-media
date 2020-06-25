package com.tokopedia.power_merchant.subscribe.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.constant.GMParamConstant.PM_SUBSCRIBE_SUCCESS
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.power_merchant.subscribe.*
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantFreeShippingTracker
import com.tokopedia.power_merchant.subscribe.view.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantTermsActivity
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantFreeShippingStatus
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.*
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialBenefitPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialMemberPmViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewholder.PartialTncViewHolder
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PmSubscribeViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KYCConstant.MERCHANT_KYC_PROJECT_ID
import com.tokopedia.user_identification_common.KYCConstant.PARAM_PROJECT_ID
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.*
import javax.inject.Inject

class PowerMerchantSubscribeFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModel: PmSubscribeViewModel
    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    @Inject
    lateinit var powerMerchantTracking:PowerMerchantTracking
    lateinit var partialMemberPmViewHolder: PartialMemberPmViewHolder
    lateinit var partialBenefitPmViewHolder: PartialBenefitPmViewHolder
    lateinit var partialTncViewHolder: PartialTncViewHolder
    lateinit var shopStatusModel: ShopStatusModel
    lateinit var getApprovalStatusPojo: KycUserProjectInfoPojo
    lateinit var bottomSheetCommon: MerchantCommonBottomSheet
    private var bottomSheetCancel: PowerMerchantCancelBottomSheet? = null

    private var shopScore: Int = 0
    private var minScore: Int = 0

    override fun getScreenName(): String = ""

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

        private const val APPLINK_PARAMS_KYC = "${PARAM_PROJECT_ID}=${MERCHANT_KYC_PROJECT_ID}"
        const val APPLINK_POWER_MERCHANT_KYC = "${ApplinkConst.KYC_NO_PARAM}?$APPLINK_PARAMS_KYC"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_power_merchant_subscribe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_view_pm.showLoading()
        hideButtonActivatedPm()
        renderInitialLayout()
        button_activate_root.setOnClickListener {
            powerMerchantTracking.eventUpgradeShopPm()
            openTermsAndCondition()
        }

        observeActivatePowerMerchant()
        observeGetFreeShippingDetail()
        observeGetPmStatusInfo()
        observeViewState()

        viewModel.getPmStatusInfo()
    }

    private fun renderInitialLayout() {
        ImageHandler.LoadImage(img_top_1, IMG_URL_PM_INTRO)
        initializePartialPart(view)
        partialBenefitPmViewHolder.renderPartialBenefit()
        partialTncViewHolder.renderPartialTnc()
    }

    private val onViewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.member_cancellation_button -> {
                powerMerchantTracking.eventCancelMembershipPm()
                showBottomSheetCancel()
            }
            else -> {

            }
        }
    }

    private fun cancelMembership() {
        bottomSheetCancel?.dismiss()
        redirectToPMCancellationQuestionnairePage()
    }

    private fun redirectToPMCancellationQuestionnairePage() {
        context?.let {
            val intent = PMCancellationQuestionnaireActivity.newInstance(it)
            startActivityForResult(intent, TURN_OFF_AUTOEXTEND_INTENT_CODE)
        }
    }

    private fun onSuccessCancelMembership() {
        showToasterCancellationSuccess()
        refreshData()
    }

    private fun refreshData() {
        root_view_pm.showLoading()
        hideButtonActivatedPm()
        viewModel.getPmStatusInfo()
    }

    private fun showToasterCancellationSuccess() {
        activity?.let {
            Toaster.make(view!!, getString(R.string.pm_cancellation_success), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
        }
    }

    private fun showBottomSheetSuccess(freeShipping: PowerMerchantFreeShippingStatus) {
        val freeShippingEligible = freeShipping.isEligible
        val chargePeriod = !freeShipping.isTransitionPeriod
        val showFreeShipping = freeShippingEligible && chargePeriod
        val imgUrl = IMG_URL_BS_SUCCESS

        if(showFreeShipping) {
            trackSuccessBottomSheetPopUp(freeShipping)
        }

        val headerString = getString(R.string.pm_label_bs_success_header)
        val descString = if(showFreeShipping) {
            getString(R.string.power_merchant_success_free_shipping_description)
        } else {
            getString(R.string.pm_label_bs_success_desc)
        }
        val btnString = if(showFreeShipping) {
            getString(R.string.power_merchant_free_shipping_learn_more)
        } else {
            getString(R.string.pm_label_bs_success_button)
        }

        val bottomSheetModel = MerchantCommonBottomSheet.BottomSheetModel(
            headerString, descString, imgUrl, btnString, PM_SUBSCRIBE_SUCCESS)

        bottomSheetCommon = MerchantCommonBottomSheet.newInstance(bottomSheetModel)
        bottomSheetCommon.setListener(object : MerchantCommonBottomSheet.BottomSheetListener {
            override fun onBottomSheetButtonClicked() {
                if(showFreeShipping) {
                    openFreeShippingPage()
                    trackSuccessBottomSheetClickLearnMore(freeShipping)
                }
                bottomSheetCommon.dismiss()
                refreshData()
            }
        })
        bottomSheetCommon.show(childFragmentManager, "power_merchant_success")
    }

    private fun openFreeShippingPage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
            PowerMerchantUrl.URL_FREE_SHIPPING_INTERIM_PAGE)
    }

    private fun trackSuccessBottomSheetPopUp(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendSuccessBottomSheetPopUp(
            userSessionInterface,
            freeShipping
        )
    }

    private fun trackSuccessBottomSheetClickLearnMore(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendSuccessBottomSheetClickLearnMore(
            userSessionInterface,
            freeShipping
        )

    }

    private fun setupBottomSheetCancel(freeShippingEnabled: Boolean) {
        bottomSheetCancel = PowerMerchantCancelBottomSheet.newInstance(
            shopStatusModel.isAutoExtend(),
            shopStatusModel.powerMerchant.expiredTime,
            freeShippingEnabled
        )
        bottomSheetCancel?.setListener(object : PowerMerchantCancelBottomSheet.BottomSheetCancelListener {
            override fun onclickButton() {
                cancelMembership()
            }
        })
    }

    private fun showBottomSheetCancel() {
        bottomSheetCancel?.show(childFragmentManager, "power_merchant_cancel")
    }

    private fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus) {
        shopStatusModel = powerMerchantStatus.goldGetPmOsStatus.result.data
        getApprovalStatusPojo = powerMerchantStatus.kycUserProjectInfoPojo
        shopScore = powerMerchantStatus.shopScore.data.value
        minScore = powerMerchantStatus.shopScore.badgeScore

        val isTransitionPeriod = shopStatusModel.isTransitionPeriod()
        val freeShippingEnabled = powerMerchantStatus.freeShippingEnabled

        if (isTransitionPeriod) {
            renderViewTransitionPeriod()
        } else {
            renderViewNonTransitionPeriod()
        }
        partialMemberPmViewHolder.renderPartialMember(shopStatusModel, isAutoExtend())

        hideLoading()

        if(shopStatusModel.isPowerMerchantInactive()) {
            freeShippingLayout.hide()
        } else {
            viewModel.getFreeShippingStatus()
        }

        setupBottomSheetCancel(freeShippingEnabled)
    }

    private fun showLoading() {
        root_view_pm.showLoading()
    }

    private fun hideLoading() {
        root_view_pm.hideLoading()
    }

    private fun renderViewNonTransitionPeriod() {
        var isPowerMerchant = shopStatusModel.isPowerMerchantIdle() or shopStatusModel.isPowerMerchantActive()
        ticker_blue_container.visibility = View.GONE
        if (isPowerMerchant) {
            if (isAutoExtend()) {
                ticker_yellow_container.visibility = View.GONE
                hideButtonActivatedPm()
                ticker_yellow_container.gone()
            } else {
                showButtonActivatePm()
                showExpiredDateTickerYellow()
            }
        } else {
            showButtonActivatePm()
            ticker_yellow_container.gone()
        }
    }

    private fun renderViewTransitionPeriod() {
        var isPowerMerchant = shopStatusModel.isPowerMerchantActive() or shopStatusModel.isPowerMerchantIdle()
        var isPending = shopStatusModel.isPowerMerchantPending()
        if (isPowerMerchant) {
            var isNotKyc = getApprovalStatusPojo.kycProjectInfo.status != KYCConstant.STATUS_VERIFIED
            if (isNotKyc) {
                if (isAutoExtend()) {
                    showTickerYellowTransitionPeriod()
                } else {
                    showButtonActivatePm()
                    ticker_yellow_container.visibility = View.GONE
                }
                button_activate_root.text = getString(R.string.pm_label_button_kyc_upload)
            } else {
                if (isAutoExtend()) {
                    showTickerYellowTransitionPeriod()
                    hideButtonActivatedPm()
                } else {
                    ticker_yellow_container.visibility = View.GONE
                    showButtonActivatePm()
                }
            }
            ticker_blue_container.visibility = View.VISIBLE
        } else if (isPending) { // Regular Merchant but Activated Pm on Transition Period
            if (isAutoExtend()) {
                hideButtonActivatedPm()
                showTickerYellowTransitionPeriod()
            } else {
                ticker_yellow_container.visibility = View.GONE
                showButtonActivatePm()
            }

            ticker_blue_container.visibility = View.GONE
        } else { //Regular Merchant
            showButtonActivatePm()
            ticker_yellow_container.visibility = View.GONE
            ticker_blue_container.visibility = View.GONE
        }
    }

    private fun showTickerYellowTransitionPeriod() {
        ticker_yellow_container.visibility = View.VISIBLE
        txt_ticker_yellow.text = MethodChecker.fromHtml(getString(R.string.pm_label_cancellation_duration))
    }

    private fun hideButtonActivatedPm() {
        ll_footer_submit.visibility = View.GONE
    }

    private fun showButtonActivatePm() {
        button_activate_root.text = getString(R.string.pm_upgrade_shop)
        ll_footer_submit.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
    }

    private fun isAutoExtend(): Boolean {
        return shopStatusModel.isAutoExtend()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVATE_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.onActivatePmSuccess()
        } else if (requestCode == AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.onActivatePmSuccess()
        } else if (requestCode == TURN_OFF_AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK){
            onSuccessCancelMembership()
        }
    }

    private fun openTermsAndCondition() {
        val intent = context?.let { PowerMerchantTermsActivity.createIntent(it) }
        startActivityForResult(intent, ACTIVATE_INTENT_CODE)
    }

    private fun showEmptyState(throwable: Throwable) {
        NetworkErrorHelper.showEmptyState(root_view_pm.context, root_view_pm, ErrorHandler.getErrorMessage(context, throwable)) {
            refreshData()
        }
        hideLoading()
    }

    private fun observeGetFreeShippingDetail() {
        observe(viewModel.getPmFreeShippingStatusResult) {
            when(it) {
                is Success -> {
                    val freeShipping = it.data

                    if(freeShipping.isTransitionPeriod && !freeShipping.isActive) {
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

    private fun trackFreeShippingImpression(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendImpressionFreeShipping(
            userSessionInterface,
            freeShipping
        )
    }

    private fun showFreeShippingWidget(freeShipping: PowerMerchantFreeShippingStatus) {
        freeShippingError.hide()
        freeShippingLayout.apply {
            onClickListener = {
                trackFreeShippingClick(freeShipping)
            }
            show(freeShipping)
        }
    }

    private fun trackFreeShippingClick(freeShipping: PowerMerchantFreeShippingStatus) {
        PowerMerchantFreeShippingTracker.sendClickFreeShipping(
            userSessionInterface,
            freeShipping
        )
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

    private fun observeGetPmStatusInfo() {
        observe(viewModel.getPmStatusInfoResult) {
            when(it) {
                is Success -> onSuccessGetPmInfo(it.data)
                is Fail -> showEmptyState(it.throwable)
            }
        }
    }

    private fun observeViewState() {
        observe(viewModel.viewState) {
            when(it) {
                is ShowLoading -> showLoading()
                is HideLoading -> hideLoading()
            }
        }
    }

    private fun observeActivatePowerMerchant() {
        observe(viewModel.onActivatePmSuccess) {
            if(it is Success) {
                showBottomSheetSuccess(it.data)
                refreshData()
            }
        }
    }

    private fun showExpiredDateTickerYellow() {
        ticker_yellow_container.visibility = View.VISIBLE
        val shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
                DateFormatUtils.FORMAT_D_MMMM_YYYY,
                shopStatusModel.powerMerchant.expiredTime)
        val spanText = SpannableString(getString(R.string.expired_label, shopCloseUntilString))
        spanText.setSpan(StyleSpan(Typeface.BOLD),
                86, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_ticker_yellow.text = spanText
    }

    private fun initializePartialPart(view: View?) {
        if (!::partialMemberPmViewHolder.isInitialized) {
            partialMemberPmViewHolder = PartialMemberPmViewHolder.build(base_partial_member, onViewClickListener)
        }
        if (!::partialTncViewHolder.isInitialized) {
            partialTncViewHolder = PartialTncViewHolder.build(base_partial_tnc, activity)
        }
        if (!::partialBenefitPmViewHolder.isInitialized) {
            partialBenefitPmViewHolder = PartialBenefitPmViewHolder.build(base_partial_benefit, activity,powerMerchantTracking)
        }
    }
}
