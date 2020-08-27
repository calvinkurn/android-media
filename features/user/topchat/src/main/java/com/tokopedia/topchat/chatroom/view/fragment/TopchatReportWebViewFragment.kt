package com.tokopedia.topchat.chatroom.view.fragment

import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.webview.KEY_URL

class TopchatReportWebViewFragment : BaseSessionWebViewFragment() {

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        val override = super.shouldOverrideUrlLoading(webview, url)
        if (!override) {
            // TODO: Impl later
            view?.let {
                Snackbar.make(it, "Overrided $override, $url", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Cancel") {

                        }.show()
            }
        }
        return override
    }

    companion object {
        fun newInstance(url: String,
                        needLogin: Boolean,
                        overrideUrl: Boolean): TopchatReportWebViewFragment {
            val fragment = TopchatReportWebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            args.putBoolean(KEY_NEED_LOGIN, needLogin)
            args.putBoolean(KEY_ALLOW_OVERRIDE, overrideUrl)
            fragment.arguments = args
            return fragment
        }
    }
}