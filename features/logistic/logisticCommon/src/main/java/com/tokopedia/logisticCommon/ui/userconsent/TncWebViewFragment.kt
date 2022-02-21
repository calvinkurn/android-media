package com.tokopedia.logisticCommon.ui.userconsent

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL
import android.webkit.WebViewClient


class TncWebViewFragment : BaseWebViewFragment() {
    private var tncUrl: String = "https://www.tokopedia.com/privacy#data-pengguna"
    private var flag: Boolean = false

    override fun getUrl(): String {
        return arguments?.getString(KEY_URL) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView.loadUrl(tncUrl)
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        webview?.loadUrl(tncUrl)
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

    companion object {

        fun createInstance(): TncWebViewFragment {
            return TncWebViewFragment()
        }
    }

}