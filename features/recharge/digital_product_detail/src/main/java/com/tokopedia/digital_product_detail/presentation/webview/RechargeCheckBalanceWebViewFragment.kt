package com.tokopedia.digital_product_detail.presentation.webview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.EXTRA_CHECK_BALANCE_ACCESS_TOKEN
import com.tokopedia.digital_product_detail.databinding.FragmentRechargeCheckBalanceWebViewBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.BaseSessionWebViewFragment

class RechargeCheckBalanceWebViewFragment : BaseSessionWebViewFragment() {

    private var binding by autoClearedNullable<FragmentRechargeCheckBalanceWebViewBinding>()
    private var webUrl = ""

    override fun getScreenName(): String {
        return RechargeCheckBalanceWebViewFragment::class.java.simpleName
    }

    override fun getUrl(): String {
        return arguments?.getString(EXTRA_WEB_URL) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBundle()
        webView.loadUrl(webUrl)
    }

    private fun getDataFromBundle() {
        arguments?.run {
            webUrl = getString(EXTRA_WEB_URL) ?: ""
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (url.isNotEmpty() && url.contains(ApplinkConst.INDOSAT_CHECK_BALANCE)) {
            // TODO: [Misael] get access token
            val accessToken = ""
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
        private const val EXTRA_WEB_URL = "EXTRA_WEB_URL"

        fun createInstance(webUrl: String): RechargeCheckBalanceWebViewFragment {
            val fragment = RechargeCheckBalanceWebViewFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_WEB_URL, webUrl)
            fragment.arguments = bundle
            return fragment
        }
    }
}
