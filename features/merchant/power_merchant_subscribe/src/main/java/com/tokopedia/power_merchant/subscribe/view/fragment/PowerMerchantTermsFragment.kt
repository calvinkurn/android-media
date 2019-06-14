package com.tokopedia.power_merchant.subscribe.view.fragment

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsFragment: BaseWebViewFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

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
}
