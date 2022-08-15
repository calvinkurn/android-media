package com.tokopedia.checkout.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL

class UpsellWebViewFragment: BaseSessionWebViewFragment() {

    companion object {
        fun newInstance(url: String): UpsellWebViewFragment {
            val fragment = UpsellWebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (url.startsWith(ApplinkConst.CHECKOUT)) {
            val uri = Uri.parse(url)
            val isPlusSelected = uri.getBooleanQueryParameter("is_plus_selected", false)
            activity?.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("is_plus_selected", isPlusSelected)
            })
            activity?.finish()
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }
}