package com.tokopedia.checkout.view.helper

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.checkout.R
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.webview.TkpdWebView
import timber.log.Timber


object ShipmentCartItemModelHelper {
    private var webView: TkpdWebView? = null
    var failedLoading = false

    @JvmStatic
    fun getFirstProductId(models: List<ShipmentCartItemModel>): Int {
        models.firstOrNull()?.cartItemModels?.firstOrNull()?.let {
            return it.productId
        } ?: return 0
    }

    @JvmStatic
    fun openWebviewInBS(context: Context, url: String, title: String) {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
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
                val webUrl = view.url
                Timber.w("P1#WEBVIEW_ERROR#'%s';error_code=%s;desc='%s';web_url='%s'",
                        request.url, error.errorCode, error.description, webUrl)
                failedLoading = true
            }
        }
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            showKnob = true
            showCloseIcon = false
            isHideable = true
            isDragable = true
            customPeekHeight = (getScreenHeight() / 2).toDp()
            setTitle(title)
            setChild(view)
        }.show(fragmentManager, null)
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}