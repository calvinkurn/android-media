package com.tokopedia.kyc_centralized.ui.gotoKyc

import android.app.Activity
import android.os.Bundle import android.webkit.WebView
import com.gojek.OneKycSdk
import com.gojek.kyc.web.OneKycWebWrapper
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.webview.BaseSessionWebViewFragment
import javax.inject.Inject

class GotoKycWebWrapperFragment : BaseSessionWebViewFragment() {

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    fun newInstance(url: String): GotoKycWebWrapperFragment {
        val fragment = GotoKycWebWrapperFragment()
        val args = Bundle()
        args.putString(KEY_URL, url)
        fragment.arguments = args
        return fragment
    }

    fun newInstance(url: String,
                    needLogin: Boolean,
                    overrideUrl: Boolean,
                    pullToRefresh: Boolean): GotoKycWebWrapperFragment {
        val fragment = GotoKycWebWrapperFragment()
        val args = Bundle()
        args.putString(com.tokopedia.webview.KEY_URL, url)
        args.putBoolean(com.tokopedia.webview.KEY_NEED_LOGIN, needLogin)
        args.putBoolean(com.tokopedia.webview.KEY_ALLOW_OVERRIDE, overrideUrl)
        args.putBoolean(com.tokopedia.webview.KEY_PULL_TO_REFRESH, pullToRefresh)
        fragment.arguments = args
        return fragment
    }

    override fun addJavascriptInterface(webView: WebView?, activity: Activity?) {
        oneKycSdk.init()
        val wrapper = OneKycWebWrapper(oneKycSdk, activity!!, webView!!)
        webView.addJavascriptInterface(wrapper, OneKycWebWrapper.PLATFORM_CODE)
    }

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        const val KEY_URL = "url"
        const val KEY_ALLOW_OVERRIDE = "allow_override"
        const val KEY_NEED_LOGIN = "need_login"
        const val KEY_PULL_TO_REFRESH = "pull_to_refresh"
    }
}
