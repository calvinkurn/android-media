package com.tokopedia.profilecompletion.common.webview

import android.app.Activity
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.net.UrlQuerySanitizer.ValueSanitizer
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity.Companion.KEY_QUERY_PARAM
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity.Companion.VALUE_QUERY_PARAM
import com.tokopedia.webview.BaseWebViewFragment


class ProfileSettingWebViewFragment: BaseWebViewFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isContainsUrlEmail(url)) {
            onChangeEmailSuccess()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    override fun webViewClientShouldInterceptRequest(view: WebView?, request: WebResourceRequest?) {
        if (isContainsUrlEmail(url)) {
            onChangeEmailSuccess()
            return
        }
        super.webViewClientShouldInterceptRequest(view, request)
    }

    private fun onChangeEmailSuccess() {
        activity?.apply {
            this.setResult(Activity.RESULT_OK)
            this.finish()
        }
    }

    private fun isContainsUrlEmail(url: String): Boolean {
        val sanitizer = UrlQuerySanitizer(url)
        val queryLd: String = sanitizer.getValue(KEY_QUERY_PARAM)
        return (queryLd.isNotEmpty() && queryLd.contains(VALUE_QUERY_PARAM))
    }

    companion object {
        fun instance(bundle: Bundle): Fragment {
            val fragment = ProfileSettingWebViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}