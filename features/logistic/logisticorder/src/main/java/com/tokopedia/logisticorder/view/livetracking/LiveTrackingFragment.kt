package com.tokopedia.logisticorder.view.livetracking

import android.os.Bundle
import com.tokopedia.webview.BaseWebViewFragment

class LiveTrackingFragment : BaseWebViewFragment() {

    override fun getUrl(): String {
        return arguments?.getString(EXTRA_TRACKING_URL) ?: ""
    }

    companion object {

        const val EXTRA_TRACKING_URL = "tracking_url"

        fun createInstance(url: String): LiveTrackingFragment {
            return LiveTrackingFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_TRACKING_URL, url)
                }
            }
        }

    }
}