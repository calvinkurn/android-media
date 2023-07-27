package com.tokopedia.affiliate.ui.fragment

import android.webkit.WebView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL

class AffiliateHelpFragment : BaseSessionWebViewFragment() {

    companion object {
        fun getFragmentInstance(url: String, isPromo: Boolean = false): Fragment {
            return if (isPromo) {
                AffiliateHelpFragment().apply {
                    this.arguments = bundleOf(KEY_URL to url)
                }
            } else {
                newInstance(url)
            }
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (url.contains(ApplinkConst.HOME)) {
            (activity as? AffiliateActivity)?.finish()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }
}
