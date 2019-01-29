package com.tokopedia.expresscheckout.view.errorview

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.expresscheckout.R
import kotlinx.android.synthetic.main.fragment_bottomsheet_error.*

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class ErrorBottomsheets : BottomSheets() {

    companion object {
        val RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT = "RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT"
        val RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT = "RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT"
    }

    lateinit var actionListener: ErrorBottomsheetsActionListener
    var title: String = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_bottomsheet_error
    }

    override fun initView(view: View?) {

    }

    fun setData(title: String, message: String, action: String, enableRetry: Boolean) {
        this.title = title
        tv_message.text = message
        btn_action.text = action

        if (enableRetry) {
            btn_retry.visibility = View.VISIBLE
            btn_retry.setOnClickListener { actionListener.onRetryClicked() }
        } else {
            btn_retry.visibility = View.GONE
        }

        btn_action.setOnClickListener { actionListener.onActionButtonClicked() }
    }

    override fun title(): String {
        return title
    }
}