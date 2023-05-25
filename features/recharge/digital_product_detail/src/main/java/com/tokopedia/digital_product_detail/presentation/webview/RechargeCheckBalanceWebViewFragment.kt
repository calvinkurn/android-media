package com.tokopedia.digital_product_detail.presentation.webview

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.digital_product_detail.databinding.FragmentRechargeCheckBalanceWebViewBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.BaseSessionWebViewFragment

class RechargeCheckBalanceWebViewFragment: BaseSessionWebViewFragment() {

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
            activity?.setResult(Activity.RESULT_OK)
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
