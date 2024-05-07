package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.webview.KEY_PULL_TO_REFRESH
import com.tokopedia.webview.KEY_URL
import com.tokopedia.webview.logWebReceivedError

class OrderCancellationCsatFragment : BaseSessionWebViewFragment() {
    override fun onWebPageReceivedError(
        failingUrl: String?,
        errorCode: Int,
        description: String?,
        webUrl: String?
    ) {
        progressBar.visibility = View.GONE
        activity.logWebReceivedError(webView, failingUrl, errorCode.toString(), description)
    }

    companion object {
        fun newInstance(
            url: String,
            needLogin: Boolean,
            overrideUrl: Boolean,
            pullToRefresh: Boolean
        ): OrderCancellationCsatFragment {
            val fragment = OrderCancellationCsatFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            args.putBoolean(KEY_NEED_LOGIN, needLogin)
            args.putBoolean(KEY_ALLOW_OVERRIDE, overrideUrl)
            args.putBoolean(KEY_PULL_TO_REFRESH, pullToRefresh)
            fragment.arguments = args
            return fragment
        }
    }
}
