package com.tokopedia.profilecompletion.common.webview

import android.app.Activity
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity.Companion.KEY_IS_FROM_APP
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity.Companion.KEY_QUERY_PARAM
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity.Companion.VALUE_QUERY_PARAM
import com.tokopedia.webview.BaseWebViewFragment


class ProfileSettingWebViewFragment: BaseWebViewFragment() {

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isContainsUrlEmail(url) && !isContainsParamIsFromApp(url)) {
            onChangeEmailSuccess()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    private fun onChangeEmailSuccess() {
        activity?.apply {
            this.setResult(Activity.RESULT_OK)
            this.finish()
        }
    }

    private fun isContainsParamIsFromApp(url: String): Boolean {
        val sanitizer = UrlQuerySanitizer(url)
        if (!sanitizer.hasParameter(KEY_IS_FROM_APP)) return false
        val paramIsFromApp: String = sanitizer.getValue(KEY_IS_FROM_APP)
        return paramIsFromApp.isNotEmpty() && paramIsFromApp == "true"
    }

    private fun isContainsUrlEmail(url: String): Boolean {
        val sanitizer = UrlQuerySanitizer(url)
        if (!sanitizer.hasParameter(KEY_QUERY_PARAM)) return false
        val paramLd: String = sanitizer.getValue(KEY_QUERY_PARAM)
        return paramLd.isNotEmpty() && paramLd.contains(VALUE_QUERY_PARAM)
    }

    companion object {
        fun instance(bundle: Bundle): Fragment {
            val fragment = ProfileSettingWebViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}