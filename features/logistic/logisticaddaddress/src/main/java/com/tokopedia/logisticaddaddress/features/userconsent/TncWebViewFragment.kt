package com.tokopedia.logisticaddaddress.features.userconsent

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL

class TncWebViewFragment : BaseWebViewFragment() {

    companion object {
        private const val TNC_URL = "https://www.tokopedia.com/privacy#data-pengguna"
        fun createInstance(): TncWebViewFragment {
            return TncWebViewFragment()
        }
    }
    private var flag: Boolean = false

    override fun getUrl(): String {
        return arguments?.getString(KEY_URL) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView.loadUrl(TNC_URL)
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        webview?.loadUrl(TNC_URL)
        webview?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                flag = if (!flag) {
                    view.loadUrl(url)
                    true
                } else {
                    false
                }
            }
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }
}
