package com.tokopedia.settingbank.banklist.v2.view.widgets

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData

class BankTNCBottomSheet(val context: Context) : CloseableBottomSheetDialog.CloseClickedListener {

    override fun onCloseDialog() {
    }

    lateinit var templateData: TemplateData
    lateinit var tncDialog: CloseableBottomSheetDialog

    fun show(data: TemplateData) {
        this.templateData = data
        val view = createBottomSheetView()
        if (!::tncDialog.isInitialized)
            tncDialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(context, this)
        tncDialog.setCustomContentView(view,context.resources.getString(R.string.terms_and_condition) , true)
        tncDialog.show()
    }

    private fun createBottomSheetView() : View{
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_tnc, null)
        if (::templateData.isInitialized)
            view.findViewById<TextView>(R.id.tncDescription).text = Html.fromHtml(templateData.template)
        return view
    }

}
