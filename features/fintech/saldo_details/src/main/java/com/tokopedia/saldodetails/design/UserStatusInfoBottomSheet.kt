package com.tokopedia.saldodetails.design

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.Html
import android.widget.TextView

import com.tokopedia.design.bottomsheet.BottomSheetView

class UserStatusInfoBottomSheet(context: Context) : BottomSheetView(context) {

    private var titleTV: TextView? = null
    private var bodyTV: TextView? = null
    private var actionButtonTV: TextView? = null

    override fun getLayout(): Int {
        return com.tokopedia.saldodetails.R.layout.user_info_bottom_sheet
    }

    override fun init(context: Context) {
        if (context is Activity)
            layoutInflater = context.layoutInflater
        else
            layoutInflater = ((context as ContextWrapper)
                    .baseContext as Activity).layoutInflater

        bottomSheetView = layoutInflater.inflate(layout, null)
        setContentView(bottomSheetView)


        titleTV = bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.title_text_view)
        bodyTV = bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.body_text_view)
        actionButtonTV = bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.action_button_text_view)

        bottomSheetView.findViewById<TextView>(com.tokopedia.saldodetails.R.id.action_button_text_view).
                setOnClickListener{ dismiss() }
    }

    fun setTitle(title: String) {
        if (titleTV == null) {
            return
        }
        titleTV!!.text = Html.fromHtml(title)
    }

    fun setBody(body: String) {
        if (bodyTV == null) {
            return
        }

        bodyTV!!.text = Html.fromHtml(body)
    }

    fun setButtonText(text: String) {
        if (actionButtonTV == null) {
            return
        }

        actionButtonTV!!.text = Html.fromHtml(text)
    }
}
