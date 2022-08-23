package com.tokopedia.changephonenumber.features.webview

import android.app.Activity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.changephonenumber.tracker.ChangePhoneNumberTracker
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL

class ChangePhoneNumberWebViewFragment : BaseWebViewFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = arguments?.getString(KEY_URL) as String
            if (url.isEmpty()) activity?.finish()
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isContainAppLinkSuccess(url)) {
            onSuccess()
            return true
        }

        return super.shouldOverrideUrlLoading(webview, url)
    }

    override fun webViewClientShouldInterceptRequest(view: WebView?, request: WebResourceRequest?) {
        request?.url?.let {
            if (isContainAppLinkSuccess(it.toString())) {
                onSuccess()
                return
            }
        }

        super.webViewClientShouldInterceptRequest(view, request)
    }

    private fun isContainAppLinkSuccess(url: String): Boolean {
        return url.contains(APP_LINK_SUCCESS)
    }

    override fun onFragmentBackPressed(): Boolean {
        ChangePhoneNumberTracker.trackClickOnBtnBackChangePhone()
        return super.onFragmentBackPressed()
    }

    private fun onSuccess() {
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    companion object {
        const val APP_LINK_SUCCESS = "tkpd-internal://update-phone-success"
        fun instance(bundle: Bundle): Fragment {
            return ChangePhoneNumberWebViewFragment().apply {
                arguments = bundle
            }
        }
    }
}