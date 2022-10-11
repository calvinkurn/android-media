package com.tokopedia.checkout.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.webview.KEY_PULL_TO_REFRESH
import com.tokopedia.webview.KEY_URL

class UpsellWebViewFragment : BaseSessionWebViewFragment() {

    companion object {
        fun newInstance(url: String): UpsellWebViewFragment {
            val fragment = UpsellWebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            url: String,
            needLogin: Boolean,
            overrideUrl: Boolean,
            pullToRefresh: Boolean
        ): UpsellWebViewFragment {
            val fragment = UpsellWebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            args.putBoolean(KEY_NEED_LOGIN, needLogin)
            args.putBoolean(KEY_ALLOW_OVERRIDE, overrideUrl)
            args.putBoolean(KEY_PULL_TO_REFRESH, pullToRefresh)
            fragment.arguments = args
            return fragment
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (url.startsWith(ApplinkConst.CHECKOUT)) {
            val uri = Uri.parse(url)
            val isPlusSelected =
                uri.getBooleanQueryParameter(CartConstant.CHECKOUT_IS_PLUS_SELECTED, false)
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(CartConstant.CHECKOUT_IS_PLUS_SELECTED, isPlusSelected)
                }
            )
            activity?.finish()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }
}
