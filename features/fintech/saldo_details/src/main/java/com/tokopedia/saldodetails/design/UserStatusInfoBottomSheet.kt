package com.tokopedia.saldodetails.design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

class UserStatusInfoBottomSheet : BottomSheetUnify() {

    private var sheetTitle: CharSequence? = null
    private var bodyText: CharSequence? = null
    private var buttonText: CharSequence? = null
    private var bodyTV: Typography? = null
    private var actionButtonTV: UnifyButton? = null
    private val childLayoutRes = com.tokopedia.saldodetails.R.layout.user_info_bottom_sheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bodyTV = view.findViewById(com.tokopedia.saldodetails.R.id.body_text_view) as Typography
        actionButtonTV = view.findViewById(com.tokopedia.saldodetails.R.id.action_button_text_view) as UnifyButton
        bodyTV!!.text = bodyText
        actionButtonTV!!.text = buttonText
    }


    fun setBottomSheetTitle(title: String) {
        sheetTitle = title.parseAsHtml()
    }

    fun setBody(body: String) {
        bodyText = body.parseAsHtml()
    }

    fun setButtonText(text: String) {
        buttonText = text.parseAsHtml()
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setTitle(sheetTitle.toString())
    }

    companion object {
        const val TAG = "UserStatusBottomSheet"
    }

}
