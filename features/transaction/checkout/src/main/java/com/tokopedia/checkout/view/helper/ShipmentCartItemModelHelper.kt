package com.tokopedia.checkout.view.helper

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.checkout.R
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.webview.TkpdWebView


object ShipmentCartItemModelHelper {

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
        val webView = view.findViewById<TkpdWebView>(R.id.proteksi_webview)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            showKnob = true
            showCloseIcon = false
            isHideable = true
            isDragable = true
            customPeekHeight = (getScreenHeight() / 3).toDp()
            setTitle(title)
            setChild(view)
        }.show(fragmentManager, null)
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}