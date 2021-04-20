package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.webview.TkpdWebView

class PurchaseProtectionInfoBottomsheet(private val url: String) {

    private var webView: TkpdWebView? = null
    private var progressBar: LoaderUnify? = null

    fun show(fragment: OrderSummaryPageFragment) {
        fragment.fragmentManager?.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                showKnob = true

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_purchase_protection_info_web_view, null)
                setupChild(child)

                customPeekHeight = (getScreenHeight() / 2).toDp()

                setChild(child)
                show(it, null)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupChild(child: View) {
        progressBar = child.findViewById(R.id.progress_bar)
        webView = child.findViewById(R.id.web_view)
        val webSettings = webView?.settings
        webSettings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
            setAppCacheEnabled(true)
        }
        webView?.webViewClient = PurchaseProtectionInfoWebViewClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }

        webView?.loadUrl(url)
    }

    inner class PurchaseProtectionInfoWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.gone()
        }
    }

}