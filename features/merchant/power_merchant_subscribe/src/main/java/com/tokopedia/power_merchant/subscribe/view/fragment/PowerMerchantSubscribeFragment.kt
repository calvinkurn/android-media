package com.tokopedia.power_merchant.subscribe.view.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.ACTION_ACTIVATE
import com.tokopedia.power_merchant.subscribe.ACTION_KYC
import com.tokopedia.power_merchant.subscribe.ACTION_SHOP_SCORE
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view.activity.PowerMerchantTermsActivity
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheets.PowerMerchantNotificationBottomSheet.CTAMode
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.ANDROID_PM_F1_ENABLED
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.KYCConstant.MERCHANT_KYC_PROJECT_ID
import com.tokopedia.user_identification_common.KYCConstant.PARAM_PROJECT_ID
import kotlinx.android.synthetic.main.fragment_power_merchant_subscribe.*
import javax.inject.Inject

class PowerMerchantSubscribeFragment : BaseDaggerFragment(), PmSubscribeContract.View {
    @Inject
    lateinit var presenter: PmSubscribeContract.Presenter
    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    @Inject
    lateinit var powerMerchantTracking:PowerMerchantTracking

    private var bottomSheetCancel: PowerMerchantCancelBottomSheet? = null

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
            activity?.let {
                val appComponent = (it.application as BaseMainApplication).baseAppComponent
                DaggerPowerMerchantSubscribeComponent.builder()
                        .baseAppComponent(appComponent)
                        .build().inject(this)
            }
        presenter.attachView(this)
    }

    companion object {
        fun createInstance() = PowerMerchantSubscribeFragment()
        const val ACTIVATE_INTENT_CODE = 123
        const val AUTOEXTEND_INTENT_CODE = 321
        const val TURN_OFF_AUTOEXTEND_INTENT_CODE = 322
        const val MINIMUM_SCORE_ACTIVATE_REGULAR = 75
        const val MINIMUM_SCORE_ACTIVATE_IDLE = 65

        private const val APPLINK_PARAMS_KYC = "${PARAM_PROJECT_ID}=${MERCHANT_KYC_PROJECT_ID}"
        const val APPLINK_POWER_MERCHANT_KYC = "${ApplinkConst.KYC_NO_PARAM}?$APPLINK_PARAMS_KYC"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_power_merchant_subscribe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (remoteConfig.getBoolean(ANDROID_PM_F1_ENABLED, false)) {
            presenter.getPmStatusInfo(userSessionInterface.shopId)
        } else {
            activity?.let {
                if (userSessionInterface.isGoldMerchant) {
                    RouteManager.route(it, ApplinkConstInternalMarketplace.GOLD_MERCHANT_MEMBERSHIP)
                } else {
                    RouteManager.route(it, ApplinkConstInternalMarketplace.GOLD_MERCHANT_SUBSCRIBE_DASHBOARD)
                }
                it.finish()
            }
        }
    }

    override fun cancelMembership() {
        redirectToPMCancellationQuestionnairePage()
    }

    override fun onSuccessCancelMembership() {
        showToasterCancellationSuccess()
        refreshData()
    }

    override fun refreshData() {
        presenter.getPmStatusInfo(userSessionInterface.shopId)
    }

    override fun onErrorCancelMembership(throwable: Throwable) {
        showToasterError(throwable)
    }

    override fun showLoading() {
        mainContainer.hide()
        btnCallToAction.hide()
        progressBar.show()
    }

    override fun hideLoading() {
        mainContainer.show()
        progressBar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVATE_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            showBottomSheetSuccess()
            refreshData()
        } else if (requestCode == AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            showBottomSheetSuccess()
            refreshData()
        } else if (requestCode == TURN_OFF_AUTOEXTEND_INTENT_CODE && resultCode == Activity.RESULT_OK){
            refreshData()
        }
    }

    override fun showEmptyState(throwable: Throwable) {
        NetworkErrorHelper.showEmptyState(context, view, ErrorHandler.getErrorMessage(context, throwable)) {
            refreshData()
        }
    }

    private fun setupCancelBottomSheet(shopStatusModel: ShopStatusModel) {
        bottomSheetCancel = PowerMerchantCancelBottomSheet.newInstance(
            shopStatusModel.isAutoExtend(),
            shopStatusModel.powerMerchant.expiredTime
        )
        bottomSheetCancel?.setListener(object : PowerMerchantCancelBottomSheet.BottomSheetCancelListener {
            override fun onClickCancelButton() {
                cancelMembership()
            }
        })
    }

    private fun redirectToPMCancellationQuestionnairePage() {
        context?.let {
            val intent = PMCancellationQuestionnaireActivity.newInstance(it)
            startActivityForResult(intent, TURN_OFF_AUTOEXTEND_INTENT_CODE)
        }
    }

    private fun showToasterError(throwable: Throwable) {
        view?.let {
            val message = ErrorHandler.getErrorMessage(activity, throwable)
            val actionLabel = getString(R.string.error_cancellation_tryagain)
            val onClickActionLabel = View.OnClickListener { cancelMembership() }
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionLabel, onClickActionLabel)
        }
    }

    private fun showToasterCancellationSuccess() {
        view?.let {
            val message = getString(R.string.pm_cancellation_success)
            val actionLabel = getString(R.string.power_merchant_ok_label)
            Toaster.make(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionLabel)
        }
    }

    private fun showDialogKyc() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.pm_label_kyc_verification_header))
                setDescription(it.getString(R.string.pm_label_kyc_verification_desc_1))
                setPrimaryCTAText(it.getString(R.string.power_merchant_kyc_verification))
                setSecondaryCTAText(it.getString(R.string.pm_label_button_close))
                setPrimaryCTAClickListener {
                    openTermsAndConditionKYC()
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    private fun showBottomSheetSuccess() {
        val bottomSheet = PowerMerchantNotificationBottomSheet.createInstance(
            getString(R.string.power_merchant_success_title),
            getString(R.string.power_merchant_success_description),
            R.drawable.ic_pm_registration_success,
            CTAMode.SINGLE
        )
        bottomSheet.setPrimaryButtonText(getString(R.string.pm_label_bs_success_button))
        bottomSheet.setPrimaryButtonClickListener { bottomSheet.dismiss() }
        bottomSheet.show(childFragmentManager)
    }

    private fun showBottomSheetCancel() {
        bottomSheetCancel?.show(childFragmentManager)
    }

    override fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus) {
        val shopStatusModel = powerMerchantStatus.goldGetPmOsStatus.result.data
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        setupCancelBottomSheet(shopStatusModel)

        if(shopStatus.isPowerMerchantInactive()) {
            showRegistrationView(powerMerchantStatus)
        } else {
            showMembershipView(powerMerchantStatus)
        }

        showCTAButton(powerMerchantStatus)
    }

    private fun showMembershipView(powerMerchantStatus: PowerMerchantStatus) {
        membershipLayout.show(powerMerchantStatus) {
            onClickRegister(powerMerchantStatus)
        }
        featureLayout.show(powerMerchantStatus)
        registrationLayout.hide()
        benefitLayout.hide()
    }

    private fun showRegistrationView(powerMerchantStatus: PowerMerchantStatus) {
        registrationLayout.show(powerMerchantStatus, powerMerchantTracking)
        featureLayout.show(powerMerchantStatus)
        benefitLayout.show(powerMerchantTracking)
        membershipLayout.hide()
    }

    private fun showCTAButton(powerMerchantStatus: PowerMerchantStatus) {
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data

        when {
            shopStatus.isPowerMerchantInactive() -> showRegisterBtn(powerMerchantStatus)
            shopStatus.isPowerMerchantActive() -> showCancelMembershipBtn()
            else -> btnCallToAction.hide()
        }
    }

    private fun showRegisterBtn(powerMerchantStatus: PowerMerchantStatus) {
        btnRegister.text = getString(R.string.power_merchant_register_now)

        btnCallToAction.setOnClickListener {
            onClickRegister(powerMerchantStatus)
        }

        btnRegister.show()
        btnCallToAction.show()
        textCancelMembership.hide()
    }

    private fun showCancelMembershipBtn() {
        val cancelMembershipTxt = getString(R.string.power_merchant_cancel_membership)
        textCancelMembership.text = MethodChecker.fromHtml(cancelMembershipTxt)

        btnCallToAction.setOnClickListener {
            powerMerchantTracking.eventCancelMembershipPm()
            showBottomSheetCancel()
        }

        btnRegister.hide()
        btnCallToAction.show()
        textCancelMembership.show()
    }

    private fun onClickRegister(powerMerchantStatus: PowerMerchantStatus) {
        val kycUserProject = powerMerchantStatus.kycUserProjectInfoPojo
        val kycVerified = kycUserProject.kycProjectInfo.isKycVerified

        if (kycVerified) {
            goToTermsAndConditionPage(powerMerchantStatus)
        } else {
            showDialogKyc()
        }

        powerMerchantTracking.eventUpgradeShopPm()
    }

    private fun goToTermsAndConditionPage(powerMerchantStatus: PowerMerchantStatus) {
        context?.let {
            val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data
            val shopScore = powerMerchantStatus.shopScore.data.value

            val action = if (shopScore < shopStatus.getMinimumShopScore()) {
                ACTION_SHOP_SCORE
            } else {
                ACTION_ACTIVATE
            }

            val intent = PowerMerchantTermsActivity.createIntent(it, action)
            startActivityForResult(intent, ACTIVATE_INTENT_CODE)
        }
    }

    private fun openTermsAndConditionKYC() {
        context?.let {
            val intent = PowerMerchantTermsActivity.createIntent(it, ACTION_KYC)
            startActivity(intent)
        }
    }
}
