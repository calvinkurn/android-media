package com.tokopedia.managepassword

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.managepassword.common.ManagePasswordConstant.IS_SUCCESS_RESET
import com.tokopedia.managepassword.common.ManagePasswordConstant.KEY_IS_CONTAINS_LOGIN_APPLINK
import com.tokopedia.managepassword.common.ManagePasswordConstant.KEY_MANAGE_PASSWORD
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL

class ManagePasswordWebViewFragment : BaseWebViewFragment() {

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = arguments?.getString(KEY_URL) as String
            if (url.isEmpty()) activity?.finish()
        }

        saveStateReset(false)
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isContainsLoginApplink(url)) {
            setResultForLoginFLow(url)
            return true
        } else if (isContainsHomeApplink(url)) {
            saveStateReset(true)
        }

        return super.shouldOverrideUrlLoading(webview, url)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun webViewClientShouldInterceptRequest(view: WebView?, request: WebResourceRequest?) {
        request?.url?.let {
            if (isContainsLoginApplink(it.toString())) {
                setResultForLoginFLow(it.toString())
                return
            } else if (isContainsHomeApplink(it.toString())) {
                saveStateReset(true)
            }
        }

        super.webViewClientShouldInterceptRequest(view, request)
    }

    private fun isContainsLoginApplink(url: String): Boolean {
        return url.startsWith(ApplinkConst.LOGIN) || url.contains(ApplinkConst.LOGIN)
    }

    private fun isContainsHomeApplink(url: String): Boolean {
        return url.startsWith(ApplinkConst.HOME) || url.contains(ApplinkConst.HOME)
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

    private fun saveStateReset(state: Boolean) {
        context?.let {
            sharedPrefs = it.getSharedPreferences(KEY_MANAGE_PASSWORD, Context.MODE_PRIVATE)
            sharedPrefs.run {
                edit().putBoolean(IS_SUCCESS_RESET, state).apply()
            }
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