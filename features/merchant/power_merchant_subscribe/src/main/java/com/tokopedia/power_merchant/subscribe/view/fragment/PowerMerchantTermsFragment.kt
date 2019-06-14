package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsFragment: BaseWebViewFragment() {

    private var userSession: UserSessionInterface? = null

    companion object {
        fun createInstance(): Fragment {
            return PowerMerchantTermsFragment()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_power_merchant_terms, container, false)
    }

    override fun getUrl(): String {
        return "https://www.tokopedia.com/blog/panduan-keamanan-tokopedia/"
    }

    override fun getUserIdForHeader(): String? {
        return UserSession(context).userId
    }

    override fun getAccessToken(): String? {
        return UserSession(context).accessToken
    }
}