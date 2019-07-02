package com.tokopedia.power_merchant.subscribe.view.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showErrorToaster
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.*
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment
import kotlinx.android.synthetic.main.fragment_power_merchant_terms.*
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsFragment: BaseWebViewFragment(), PmTermsContract.View {

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

    override fun getUrl(): String {
        return TERMS_AND_CONDITION_URL
    }

    override fun getUserIdForHeader(): String? {
        return userSession.userId
    }

    override fun getAccessToken(): String? {
        return userSession.accessToken
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
        view?.showErrorToaster(ErrorHandler.getErrorMessage(context, throwable))
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
                mainView.showErrorToaster(getString(R.string.pm_terms_error_no_agreed))
            } else {
                if (action == ACTION_ACTIVATE) {
                    presenter.activatePowerMerchant()
                } else if (action == ACTION_AUTO_EXTEND) {
                    presenter.autoExtendPowerMerchant()
                }
            }
        }
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
}
