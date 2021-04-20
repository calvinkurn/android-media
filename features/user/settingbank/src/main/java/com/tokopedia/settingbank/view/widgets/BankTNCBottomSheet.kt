package com.tokopedia.settingbank.view.widgets

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.TemplateData

class BankTNCBottomSheet(val context: Activity) : CloseableBottomSheetDialog.CloseClickedListener {

    lateinit var templateData: TemplateData
    lateinit var tncDialog: CloseableBottomSheetDialog

    fun show(data: TemplateData) {
        this.templateData = data
        val view = createBottomSheetView()
        if (!::tncDialog.isInitialized)
            tncDialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(context, this)
        tncDialog.setCustomContentView(view,context.resources.getString(R.string.sbank_terms_and_condition) , true)
        tncDialog.show()
    }

    private fun createBottomSheetView() : View{
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_tnc, null)
        if (::templateData.isInitialized) {
            view.findViewById<WebView>(R.id.tncWebView).loadData( templateData.template,
                    "text/html", "utf-8")
        }
        return view
    }

    override fun onCloseDialog() {
    }

}
