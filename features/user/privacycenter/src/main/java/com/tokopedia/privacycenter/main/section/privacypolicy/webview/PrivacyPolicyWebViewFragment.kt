package com.tokopedia.privacycenter.main.section.privacypolicy.webview

import android.os.Bundle
import android.view.View
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.DEFAULT_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.KEY_TITLE
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.SECTION_ID
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.WEBVIEW_URL
import com.tokopedia.webview.BaseWebViewFragment

class PrivacyPolicyWebViewFragment : BaseWebViewFragment() {

    private var sectionId: String = ""
    private var title: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sectionId = arguments?.getString(SECTION_ID).orEmpty()
        title = arguments?.getString(KEY_TITLE) ?: DEFAULT_TITLE

        val actionbar = activity?.actionBar
        actionbar?.apply {
            title = this.title
        }

        webView.loadUrl("$WEBVIEW_URL$sectionId")
    }

    companion object {
        fun instance(bundle: Bundle?): PrivacyPolicyWebViewFragment {
            return PrivacyPolicyWebViewFragment().apply {
                arguments = bundle
            }
        }
    }
}
