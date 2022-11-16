package com.tokopedia.privacycenter.main.section.privacypolicy.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_HTML_CONTENT
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_TITLE
import com.tokopedia.webview.BaseWebViewFragment

class PrivacyPolicyWebViewFragment : BaseWebViewFragment() {

    private var htmlContent: String = ""
    private var title: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        htmlContent = arguments?.getString(KEY_HTML_CONTENT).orEmpty()
        title = arguments?.getString(KEY_TITLE) ?: DEFAULT_TITLE

        val actionbar = activity?.actionBar
        actionbar?.apply {
            title = this.title
        }

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
