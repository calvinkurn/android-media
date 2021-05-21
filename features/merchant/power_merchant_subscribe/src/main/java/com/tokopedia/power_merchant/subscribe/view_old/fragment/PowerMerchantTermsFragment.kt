package com.tokopedia.power_merchant.subscribe.view_old.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.TERMS_AND_CONDITION_URL
import com.tokopedia.power_merchant.subscribe.URL_GAINS_SCORE_POINT
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view_old.activity.PowerMerchantTermsActivity.Companion.EXTRA_SHOP_SCORE
import com.tokopedia.power_merchant.subscribe.view_old.bottomsheets.PowerMerchantNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view_old.bottomsheets.PowerMerchantNotificationBottomSheet.*
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantSubscribeFragment.Companion.APPLINK_POWER_MERCHANT_KYC
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantActivationResult
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantActivationResult.*
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.*
import com.tokopedia.power_merchant.subscribe.view_old.viewmodel.PmTermsViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.fragment_power_merchant_terms.*
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsFragment : BaseWebViewFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModel: PmTermsViewModel
    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private var isTermsAgreed: Boolean = false

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            bundle.putString(KEY_URL, TERMS_AND_CONDITION_URL)
            return PowerMerchantTermsFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_TERMS_AND_CONDITION_PAGE

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build().inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActivatePm()
        observeViewState()

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_power_merchant_terms
    }

    override fun onLoadFinished() {
        super.onLoadFinished()
        footer?.visible()
    }

    private fun showLoading() {
        mainView.showLoading()
    }

    private fun hideLoading() {
        mainView.hideLoading()
    }

    private fun onSuccessActivate(result: PowerMerchantActivationResult) {
        when(result)  {
            is ActivationSuccess -> resultOkAndFinish()
            is KycNotVerified -> showDialogKyc()
            is ShopScoreNotEligible -> showShopScoreBottomSheet()
            is GeneralError -> showErrorToast(result.message)
        }
    }

    private fun onErrorActivate(error: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, error)
        showErrorToast(message)
    }

    private fun showErrorToast(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun setWebView(): Int {
        return R.id.webviewPm
    }

    override fun setProgressBar(): Int {
        return R.id.progressbarPm
    }

    private fun observeActivatePm() {
        observe(viewModel.activatePmResult) {
            when(it) {
                is Success -> onSuccessActivate(it.data)
                is Fail -> onErrorActivate(it.throwable)
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

    private fun initView() {
        checkboxLayout.setOnClickListener {
            onCheckBoxClicked()
        }
        checkbox.setOnClickListener {
            onCheckBoxClicked()
        }
        activateBtn.setOnClickListener {
            powerMerchantTracking.eventUpgradeShopWebView()
            if (!isTermsAgreed) {
                mainView?.let {
                    Toaster.make(it, getString(R.string.pm_terms_error_no_agreed), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
            } else {
                onClickActivateButton()
            }
        }
    }

    private fun onClickActivateButton() {
        trackClickTermsAndConditionUpgradeBtn()
        viewModel.activatePowerMerchant()
    }

    private fun trackClickTermsAndConditionUpgradeBtn() {
        powerMerchantTracking.eventClickTermsAndConditionUpgradeBtn()
    }

    private fun openKycPage() {
        val intent = RouteManager.getIntent(activity, APPLINK_POWER_MERCHANT_KYC)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, ApplinkConstInternalGlobal.PARAM_SOURCE_KYC_SELLER)
        startActivity(intent)
        activity?.finish()
    }

    private fun onCheckBoxClicked() {
        trackClickTermsAndConditionTickBox()
        isTermsAgreed = !isTermsAgreed
        checkbox.isChecked = isTermsAgreed
    }

    private fun trackClickTermsAndConditionTickBox() {
        powerMerchantTracking.eventClickTermsAndConditionTickBox()
    }

    private fun resultOkAndFinish() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun showShopScoreBottomSheet() {
        val shopScore = arguments?.getInt(EXTRA_SHOP_SCORE).orZero()
        val bottomSheet = PowerMerchantNotificationBottomSheet.createInstance(
            getString(R.string.power_merchant_bottom_sheet_score_title),
            getString(R.string.power_merchant_bottom_sheet_score_description),
            R.drawable.ic_pm_score,
            CTAMode.DOUBLE
        )

        bottomSheet.setPrimaryButtonText(getString(R.string.power_merchant_see_tips))
        bottomSheet.setSecondaryButtonText(getString(R.string.pm_label_button_close))
        bottomSheet.setPrimaryButtonClickListener {
            trackClickSeeShopScoreTips(shopScore)
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_GAINS_SCORE_POINT)
            bottomSheet.dismiss()
        }
        bottomSheet.setSecondaryButtonClickListener {
            trackClickDismissShopScorePopUp(shopScore)
            activity?.finish()
            bottomSheet.dismiss()
        }
        bottomSheet.show(childFragmentManager)
        trackShowScoreDialog(shopScore)
    }

    private fun trackShowScoreDialog(shopScore: Int) {
        powerMerchantTracking.eventShowDialogScore(shopScore)
    }

    private fun trackClickSeeShopScoreTips(shopScore: Int) {
        powerMerchantTracking.eventClickSeeShopScoreTips(shopScore)
    }

    private fun trackClickDismissShopScorePopUp(shopScore: Int) {
        powerMerchantTracking.eventClickDismissShopScorePopUp(shopScore)
    }

    private fun showDialogKyc() {
        context?.let {
            trackShowKycDialog()
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.pm_label_kyc_verification_header))
                setDescription(it.getString(R.string.pm_label_kyc_verification_desc_1))
                setPrimaryCTAText(it.getString(R.string.power_merchant_kyc_verification))
                setSecondaryCTAText(it.getString(R.string.pm_label_button_close))
                setPrimaryCTAClickListener {
                    trackClickKycVerification()
                    openKycPage()
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    trackClickDismissKycPopUp()
                    activity?.finish()
                    dismiss()
                }
            }.show()
        }
    }

    private fun trackShowKycDialog() {
        powerMerchantTracking.eventShowDialogKyc()
    }

    private fun trackClickDismissKycPopUp() {
        powerMerchantTracking.eventClickDismissKycPopUp()
    }

    private fun trackClickKycVerification() {
        powerMerchantTracking.eventClickKycVerification()
    }
}
