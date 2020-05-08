package com.tokopedia.power_merchant.subscribe.view.fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.ACTION_ACTIVATE
import com.tokopedia.power_merchant.subscribe.ACTION_AUTO_EXTEND
import com.tokopedia.power_merchant.subscribe.ACTION_KEY
import com.tokopedia.power_merchant.subscribe.ACTION_KYC
import com.tokopedia.power_merchant.subscribe.ACTION_SHOP_SCORE
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.TERMS_AND_CONDITION_URL
import com.tokopedia.power_merchant.subscribe.URL_GAINS_SCORE_POINT
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.APPLINK_POWER_MERCHANT_KYC
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.dialog_score_verification.*
import kotlinx.android.synthetic.main.fragment_power_merchant_terms.*
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsFragment : BaseWebViewFragment(), PmTermsContract.View {

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var presenter: PmTermsContract.Presenter
    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking
    private var isTermsAgreed: Boolean = false
    private var action: String = ""

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
        presenter.attachView(this)
        initVar()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_power_merchant_terms
    }

    override fun onLoadFinished() {
        super.onLoadFinished()
        footer?.visible()
    }

    override fun showLoading() {
        mainView.showLoading()
    }

    override fun hideLoading() {
        mainView.hideLoading()
    }

    override fun onSuccessActivate() {
        resultOkAndFinish()
    }

    override fun onSuccessAutoExtend() {
        resultOkAndFinish()
    }

    override fun onError(throwable: Throwable?) {
        view?.let {
            Toaster.make(it, ErrorHandler.getErrorMessage(context, throwable), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun setWebView(): Int {
        return R.id.webviewPm
    }

    override fun setProgressBar(): Int {
        return R.id.progressbarPm
    }

    private fun initVar() {
        setAction(arguments?.getString(ACTION_KEY) ?: "")
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
        when(action) {
            ACTION_ACTIVATE,
            ACTION_AUTO_EXTEND -> presenter.activatePowerMerchant()
            ACTION_SHOP_SCORE -> setupDialogScore()?.show()
            ACTION_KYC -> openKycPage()
        }
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

    private fun setAction(action: String) {
        this.action = action
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
}
