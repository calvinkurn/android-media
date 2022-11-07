package com.tokopedia.epharmacy.ui.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSessionWebViewFragment

class EPharmacyTnCFragment : BaseSessionWebViewFragment() {

    companion object {
        fun getFragmentInstance(url : String): Fragment {
            return newInstance(url)
        }
    }
}