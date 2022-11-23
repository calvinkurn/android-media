package com.tokopedia.privacycenter.main.section.privacypolicy.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_HTML_CONTENT
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_TITLE
import com.tokopedia.webview.BaseWebViewFragment

class PrivacyPolicyWebViewFragment : BaseWebViewFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val htmlContent = arguments?.getString(KEY_HTML_CONTENT).orEmpty()
        val titleArgs = arguments?.getString(KEY_TITLE) ?: DEFAULT_TITLE

        val actionbar = activity?.actionBar
        actionbar?.apply {
            title = titleArgs
        }

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
