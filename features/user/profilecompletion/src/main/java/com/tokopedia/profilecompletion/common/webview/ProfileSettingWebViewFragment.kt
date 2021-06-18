package com.tokopedia.profilecompletion.common.webview

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity.Companion.VALUE_QUERY_PARAM
import com.tokopedia.webview.BaseWebViewFragment


class ProfileSettingWebViewFragment: BaseWebViewFragment() {

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isUrlAppLinkSuccessChangeEmail(url)) {
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

    private fun isUrlAppLinkSuccessChangeEmail(url: String): Boolean {
        return url.isNotEmpty() && url == VALUE_QUERY_PARAM
    }

    companion object {
        fun instance(bundle: Bundle): Fragment {
            val fragment = ProfileSettingWebViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}