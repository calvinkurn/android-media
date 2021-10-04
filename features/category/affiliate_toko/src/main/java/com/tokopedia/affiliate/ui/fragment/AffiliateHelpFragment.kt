package com.tokopedia.affiliate.ui.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSessionWebViewFragment

class AffiliateHelpFragment : BaseSessionWebViewFragment() {

    companion object {
        fun getFragmentInstance(url : String): Fragment {
            return newInstance(url)
        }
    }

}