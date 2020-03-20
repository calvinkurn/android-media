package com.tokopedia.thankyou_native.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.thankyou_native.R

class PaymentMethodsBottomSheet(val context: Context) : CloseableBottomSheetDialog.CloseClickedListener {
    lateinit var howToPayStr: String
    lateinit var dialog: CloseableBottomSheetDialog
    private val mimeType = "text/html"
    private val encoding = "utf-8"

    fun show(howToPayStr: String) {
        this.howToPayStr = howToPayStr
        val view = createBottomSheetView()
        if (!::dialog.isInitialized)
            dialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(context, this)
        dialog.setCustomContentView(view, context.resources.getString(R.string.thank_payment_method_bottom_sheet), true)
        dialog.show()
    }

    private fun createBottomSheetView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.thank_payment_method_bottom_sheet, null)
        if (::howToPayStr.isInitialized) {
            view.findViewById<WebView>(R.id.webView).settings.javaScriptEnabled = true
            view.findViewById<WebView>(R.id.webView).loadData(howToPayStr,
                    mimeType, encoding)
        }
        return view
    }

    override fun onCloseDialog() {
    }

}