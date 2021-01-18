package com.tokopedia.checkout.view.helper

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*
import android.widget.ViewFlipper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp

object CartProtectionInfoBottomSheetHelper {

    private var webView: NestedScrollWebview? = null
    var failedLoading = false

    @JvmStatic
    fun openWebviewInBottomSheet(fragment: ShipmentFragment, context: Context, url: String, title: String) {
        val view = View.inflate(context, R.layout.checkout_protection_more, null)
        webView = view.findViewById(R.id.proteksi_webview)
        val viewflipper = view.findViewById<ViewFlipper>(R.id.container_webview)
        webView?.settings?.javaScriptEnabled = true
        webView?.loadUrl(url)
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!failedLoading) {
                    viewflipper.displayedChild = 1
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                failedLoading = true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                handler?.cancel()
                failedLoading = true
            }
        }
        val bottomSheetUnify = BottomSheetUnify()
        fragment.fragmentManager?.let {
            bottomSheetUnify.apply {
                showKnob = true
                showCloseIcon = false
                isHideable = true
                isDragable = true
                customPeekHeight = (getScreenHeight() / 2).toDp()
                setTitle(title)
                setChild(view)
            }.show(it, null)
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}