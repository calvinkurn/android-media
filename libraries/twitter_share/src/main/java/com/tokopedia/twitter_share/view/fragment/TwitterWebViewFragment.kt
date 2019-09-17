package com.tokopedia.twitter_share.view.fragment

import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.twitter_share.TwitterManager
import com.tokopedia.twitter_share.view.activity.TwitterWebViewActivity
import com.tokopedia.twitter_share.view.activity.TwitterWebViewActivityListener
import com.tokopedia.webview.BaseSessionWebViewFragment

class TwitterWebViewFragment : BaseSessionWebViewFragment() {

    companion object {

        @JvmStatic
        fun newInstance(url: String): TwitterWebViewFragment {
            val fragment = TwitterWebViewFragment()
            val args = Bundle()
            args.putString(ARGS_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
        return isCallbackUrl(url.orEmpty())
    }

    private fun isCallbackUrl(url: String): Boolean {
        val isUrlTwitterCallback = url.contains(TwitterManager.OAUTH_VERIFIER) && url.contains(TwitterManager.OAUTH_TOKEN)
        if (isUrlTwitterCallback) {
            (activity as TwitterWebViewActivityListener?)?.onGetCallbackUrl(url)
        }
        return isUrlTwitterCallback
    }
}