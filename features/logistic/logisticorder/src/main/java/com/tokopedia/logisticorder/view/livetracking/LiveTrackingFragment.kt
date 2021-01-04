package com.tokopedia.logisticorder.view.livetracking

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL

class LiveTrackingFragment : BaseWebViewFragment() {

    private var trackingUrl: String = ""

    override fun getUrl(): String {
        return arguments?.getString(KEY_URL) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackingUrl = it.getString(EXTRA_TRACKING_URL, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView.loadUrl(trackingUrl)
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (url.isNotEmpty() && url.contains("tel:")) {
            webview?.loadUrl(trackingUrl)
            val callIntent = Intent(ACTION_DIAL)
            callIntent.data = Uri.parse(url)
            startActivity(callIntent)
        }
        return super.shouldOverrideUrlLoading(webView, url)
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