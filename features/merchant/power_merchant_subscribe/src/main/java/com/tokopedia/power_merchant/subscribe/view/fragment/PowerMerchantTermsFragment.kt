package com.tokopedia.power_merchant.subscribe.view.fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.TERMS_AND_CONDITION_URL
import com.tokopedia.power_merchant.subscribe.URL_GAINS_SCORE_POINT
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.APPLINK_POWER_MERCHANT_KYC
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantActivationResult
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantActivationResult.*
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PmTermsViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.dialog_kyc_verification.*
import kotlinx.android.synthetic.main.dialog_score_verification.*
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

    override fun getScreenName(): String = ""

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
            is KycNotVerified -> setupDialogKyc()?.show()
            is ShopScoreNotEligible -> setupDialogScore()?.show()
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
        viewModel.activatePowerMerchant()
    }

    private fun openKycPage() {
        val intent = RouteManager.getIntent(activity, APPLINK_POWER_MERCHANT_KYC)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, ApplinkConstInternalGlobal.PARAM_SOURCE_KYC_SELLER)
        startActivity(intent)
        activity?.finish()
    }

    private fun onCheckBoxClicked() {
        isTermsAgreed = !isTermsAgreed
        checkbox.isChecked = isTermsAgreed
    }

    private fun resultOkAndFinish() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun setupDialogScore(): Dialog? {
        context?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_score_verification)

            dialog.btn_submit_score.setOnClickListener {
                powerMerchantTracking.eventIncreaseScorePopUp()
                RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_GAINS_SCORE_POINT)
            }
            dialog.btn_close_score.setOnClickListener {
                dialog.hide()
            }
            return dialog
        }
        return null
    }

    private fun setupDialogKyc(): Dialog? {
        context?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_kyc_verification)
            dialog.btn_submit_kyc.setOnClickListener {
                openKycPage()
                dialog.hide()
            }
            dialog.btn_close_kyc.setOnClickListener {
                dialog.hide()
            }

            return dialog
        }
        return null
    }
}
