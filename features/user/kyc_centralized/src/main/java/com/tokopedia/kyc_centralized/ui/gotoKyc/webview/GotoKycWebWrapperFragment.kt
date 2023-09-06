package com.tokopedia.kyc_centralized.ui.gotoKyc.webview

import android.os.Bundle
import android.webkit.WebView
import com.gojek.OneKycSdk
import com.gojek.kyc.web.OneKycWebWrapper
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.webview.BaseSessionWebViewFragment
import javax.inject.Inject

class GotoKycWebWrapperFragment : BaseSessionWebViewFragment() {

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    override fun addJavascriptInterface(webView: WebView) {
        oneKycSdk.init()
        val wrapper = OneKycWebWrapper(oneKycSdk, requireActivity(), webView)
        webView.addJavascriptInterface(wrapper, OneKycWebWrapper.PLATFORM_CODE)
    }

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val KEY_URL = "url"

        fun newInstance(url: String): GotoKycWebWrapperFragment {
            val fragment = GotoKycWebWrapperFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }
}
