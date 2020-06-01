package com.tokopedia.topads.dashboard.view.fragment

import android.os.Bundle
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL


class TopAdsPaymentCreditFragment : BaseWebViewFragment() {

    override fun getUrl(): String {
        return arguments?.getString(KEY_URL) ?: ""

    }

    override fun getScreenName(): String? = null

    companion object {
        fun createInstance(bundle: Bundle?): TopAdsPaymentCreditFragment {
            val fragment = TopAdsPaymentCreditFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}