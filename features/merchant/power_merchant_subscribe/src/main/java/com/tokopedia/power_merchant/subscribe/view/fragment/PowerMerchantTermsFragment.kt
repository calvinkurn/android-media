package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.contract.PmTermsContract
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
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

    private var isTermsAgreed: Boolean = false

    companion object {
        fun createInstance(): Fragment {
            return PowerMerchantTermsFragment()
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
        checkboxLayout.setOnClickListener {
            isTermsAgreed = !isTermsAgreed
            checkbox.isChecked = isTermsAgreed
            activateBtn.isEnabled = isTermsAgreed
        }
        activateBtn.setOnClickListener {
            presenter.activatePowerMerchant()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_power_merchant_terms
    }

    override fun getUrl(): String {
        return "https://www.tokopedia.com/blog/panduan-keamanan-tokopedia/"
    }

    override fun getUserIdForHeader(): String? {
        return userSession.userId
    }

    override fun getAccessToken(): String? {
        return userSession.accessToken
    }

    override fun showLoading() {
        mainView.showLoading()
    }

    override fun hideLoading() {
        mainView.hideLoading()
    }

    override fun onSuccessActivate(success: Boolean) {
        Toast.makeText(context, "succces", Toast.LENGTH_LONG).show()
    }

    override fun onErrorActivate(throwable: Throwable) {
        Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
    }
}
