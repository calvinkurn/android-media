package com.tokopedia.digital_product_detail.presentation.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.EXTRA_CHECK_BALANCE_ACCESS_TOKEN
import com.tokopedia.digital_product_detail.databinding.FragmentRechargeCheckBalanceWebViewBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.BaseSessionWebViewFragment

class RechargeCheckBalanceWebViewFragment : BaseSessionWebViewFragment() {

    private var binding by autoClearedNullable<FragmentRechargeCheckBalanceWebViewBinding>()
    private var webUrl = ""

    override fun getScreenName(): String {
        return RechargeCheckBalanceWebViewFragment::class.java.simpleName
    }

    override fun getUrl(): String {
        return arguments?.getString(EXTRA_APPLINK_WEBVIEW) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBundle()
        webView.loadUrl(webUrl)
    }

    private fun getDataFromBundle() {
        arguments?.run {
            val applink = getString(EXTRA_APPLINK_WEBVIEW) ?: ""
            webUrl = getWebUrl(applink)
        }
    }

    private fun getWebUrl(applink: String): String {
        return applink.split(APPLINK_PARAM_URL)[Int.ONE]
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (url.isNotEmpty() && url.contains(ApplinkConst.INDOSAT_CHECK_BALANCE)) {
            val uri = Uri.parse(url)
            val accessToken = uri.getQueryParameter(QUERY_PARAM_ACCESS_TOKEN)
            val intent = Intent().apply {
                putExtra(EXTRA_CHECK_BALANCE_ACCESS_TOKEN, accessToken)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    companion object {
        private const val EXTRA_APPLINK_WEBVIEW = "EXTRA_APPLINK_WEBVIEW"
        private const val APPLINK_PARAM_URL = "url="
        private const val QUERY_PARAM_ACCESS_TOKEN = "access_token"

        fun createInstance(applink: String): RechargeCheckBalanceWebViewFragment {
            val fragment = RechargeCheckBalanceWebViewFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_APPLINK_WEBVIEW, applink)
            fragment.arguments = bundle
            return fragment
        }
    }
}
