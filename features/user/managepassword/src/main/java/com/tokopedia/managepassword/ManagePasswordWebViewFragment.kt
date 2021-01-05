package com.tokopedia.managepassword

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.managepassword.common.ManagePasswordConstant.KEY_IS_CONTAINS_LOGIN_APPLINK
import com.tokopedia.managepassword.common.ManagePasswordConstant.KEY_URL
import com.tokopedia.webview.BaseWebViewFragment

class ManagePasswordWebViewFragment : BaseWebViewFragment() {

    override fun getUrl(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = arguments?.getString(KEY_URL) as String
            if (url.isEmpty()) activity?.finish()
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isContainsLoginApplink(url)) {
            setResultForLoginFLow(url)
        }

        return super.shouldOverrideUrlLoading(webview, url)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun webViewClientShouldInterceptRequest(view: WebView?, request: WebResourceRequest?) {
        request?.url?.let {
            if (isContainsLoginApplink(it.toString())) {
                setResultForLoginFLow(it.toString())
            }
        }

        super.webViewClientShouldInterceptRequest(view, request)
    }

    private fun isContainsLoginApplink(url: String): Boolean {
        return url.startsWith(ApplinkConst.LOGIN) || url.contains(ApplinkConst.LOGIN)
    }

    private fun setResultForLoginFLow(url: String) {
        activity?.apply {
            val intent = Intent()
            intent.putExtra(KEY_IS_CONTAINS_LOGIN_APPLINK, true)
            intent.putExtra(KEY_URL, url)
            this.setResult(Activity.RESULT_OK, intent)
            this.finish()
        }
    }

    companion object {
        fun instance(bundle: Bundle): Fragment {
            val fragment = ManagePasswordWebViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}