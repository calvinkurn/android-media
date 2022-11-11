package com.tokopedia.privacycenter.main.section.privacypolicy.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_HTML_CONTENT
import com.tokopedia.webview.BaseWebViewFragment

class PrivacyPolicyWebViewFragment : BaseWebViewFragment() {

    private var htmlContent: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        htmlContent = arguments?.getString(KEY_HTML_CONTENT).orEmpty()
        renderHtml()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun renderHtml() {
        webView.loadPartialWebView(htmlContent)
    }

    companion object {
        fun instance(bundle: Bundle?): PrivacyPolicyWebViewFragment {
            return PrivacyPolicyWebViewFragment().apply {
                arguments = bundle
            }
        }
    }
}
