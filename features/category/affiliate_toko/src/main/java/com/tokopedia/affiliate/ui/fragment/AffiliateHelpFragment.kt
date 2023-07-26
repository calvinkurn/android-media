package com.tokopedia.affiliate.ui.fragment

import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.webview.BaseSessionWebViewFragment

class AffiliateHelpFragment : BaseSessionWebViewFragment() {

    companion object {
        fun getFragmentInstance(url: String): Fragment {
            return newInstance(url)
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        when {
            url.contains(ApplinkConst.HOME) -> {
                (activity as? AffiliateActivity)?.getBottomNav()?.showBottomNav()
            }
            else -> {
                (activity as? AffiliateActivity)?.getBottomNav()?.hideBottomNav()
            }
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }
}
