package com.tokopedia.privacycenter.ui.privacypolicywebview

import android.os.Bundle
import android.view.View
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst
import com.tokopedia.webview.BaseWebViewFragment

class PrivacyPolicyWebViewFragment : BaseWebViewFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionId = arguments?.getString(PrivacyPolicyConst.SECTION_ID).orEmpty()
        val titleArg = arguments?.getString(PrivacyPolicyConst.KEY_TITLE) ?: PrivacyPolicyConst.DEFAULT_TITLE

        val actionbar = activity?.actionBar
        actionbar?.apply {
            title = titleArg
        }

        webView.loadUrl("${PrivacyPolicyConst.WEBVIEW_URL}$sectionId")
    }

    companion object {
        fun instance(bundle: Bundle?): PrivacyPolicyWebViewFragment {
            return PrivacyPolicyWebViewFragment().apply {
                arguments = bundle
            }
        }
    }
}
