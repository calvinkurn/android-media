package com.tokopedia.product.detail.webview

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.webview.TkpdWebView

class ProductWebViewBottomSheet : BottomSheetUnify() {
    companion object {
        const val TAG = "ProductWebViewBottomSheet"
        const val ARG_URL = "args_url"
        fun instance(
            url: String
        ) = ProductWebViewBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_URL, url)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        clearContentPadding = true

        val url = arguments?.getString(ARG_URL) ?: ""
        val view = View.inflate(context, R.layout.product_webview_container, null)
        val webView = view.findViewById<TkpdWebView>(R.id.pdp_web_view)
        webView.loadUrl(url)
        // User Agent Override ke: "Tokopedia Webview - Liteapp"

        setChild(view)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
