package com.tokopedia.topads.common

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig

class TopAdsWebViewFragment: BaseSessionWebViewFragment(){
    companion object {
        private const val WEBVIEWLINK_START = "tokopedia://webview"
        private const val QUERY_KEY_URL = "url"
        @JvmStatic
        fun newInstance(url: String) = TopAdsWebViewFragment().apply {
            arguments = Bundle().also {
                it.putString(ARGS_URL, url)
            }
        }
    }

    override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
        activity?.run {
            if (url != null && application is TopAdsWebViewRouter){
                var tempUrl: String = url
                if (url.startsWith(WEBVIEWLINK_START)){
                    val uri = Uri.parse(url)
                    tempUrl = uri.getQueryParameter(QUERY_KEY_URL) ?: ""
                    tempUrl = Uri.decode(tempUrl)
                }

                if ((application as TopAdsWebViewRouter).isSupportedDelegateDeepLink(tempUrl)){
                    (application as TopAdsWebViewRouter).actionNavigateByApplinksUrl(this, tempUrl, Bundle())
                    return true
                }
            }
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }
}