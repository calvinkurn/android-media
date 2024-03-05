package com.tokopedia.product.detail.webview

import android.os.Bundle
import android.view.View
import com.tokopedia.webview.BaseWebViewFragment

class ProductWebViewFragment : BaseWebViewFragment() {
    companion object {

        const val ARG_URL = "args_url"

        fun getInstance(arguments: Bundle) = ProductWebViewFragment().apply {
            setArguments(arguments)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val url = arguments?.getString(ARG_URL) ?: ""
        webView?.loadUrl(url)
    }
}
