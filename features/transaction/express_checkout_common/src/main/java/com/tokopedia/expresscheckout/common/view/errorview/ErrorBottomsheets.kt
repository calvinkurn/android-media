package com.tokopedia.expresscheckout.common.view.errorview

import android.view.View
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.expresscheckout.common.R

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class ErrorBottomsheets : BottomSheets() {

    lateinit var tvMessage: TextView
    lateinit var btnAction: ButtonCompat
    lateinit var btnRetry: ButtonCompat

    companion object {
        val RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT = "RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT"
        val RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT = "RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT"
    }

    lateinit var actionListener: ErrorBottomsheetsActionListener
    var title = ""
    var message = ""
    var action = ""
    var enableRetry = false

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_bottomsheet_error
    }

    override fun initView(view: View) {
        btnAction = view.findViewById<View>(R.id.btn_action) as ButtonCompat
        btnRetry = view.findViewById<View>(R.id.btn_retry) as ButtonCompat
        tvMessage = view.findViewById<View>(R.id.tv_message) as TextView
        tvMessage.text = message
        btnAction.text = action
        btnAction.setOnClickListener { actionListener.onActionButtonClicked() }
        if (enableRetry) {
            btnRetry.visibility = View.VISIBLE
            btnRetry.setOnClickListener {
                (actionListener as ErrorBottomsheetsActionListenerWithRetry).onRetryClicked()
            }
        } else {
            btnRetry.visibility = View.GONE
        }
    }

    fun setData(title: String, message: String, action: String, enableRetry: Boolean) {
        this.title = title
        this.message = message
        this.action = action
        this.enableRetry = enableRetry
    }

    override fun title(): String {
        return title
    }
}