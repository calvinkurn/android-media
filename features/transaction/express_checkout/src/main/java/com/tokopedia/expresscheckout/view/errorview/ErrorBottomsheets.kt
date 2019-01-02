package com.tokopedia.expresscheckout.view.errorview

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.expresscheckout.R
import kotlinx.android.synthetic.main.fragment_bottomsheet_error.*

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class ErrorBottomsheets : BottomSheets() {

    lateinit var actionListener: ErrorBottomsheetsActionListener
    var title: String = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_bottomsheet_error
    }

    override fun initView(view: View?) {

    }

    fun setError(title: String, message: String, action: String) {
        this.title = title
        tv_message.text = message
        btn_action.text = action

        btn_action.setOnClickListener { actionListener.onActionButtonClicked() }
    }

    override fun title(): String {
        return title
    }
}