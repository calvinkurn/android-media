package com.tokopedia.chatbot.view.adapter.viewholder.listener

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class ConnectionDividerViewHolder(itemView: View) : AbstractViewHolder<ConnectionDividerViewModel>(itemView) {

    private var dividerMessage: TextView = itemView.findViewById(R.id.chatbot_tv_divider_message)
    private var dividerButton: TextView = itemView.findViewById(R.id.chatbot_tv_divider_button)

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.chatbot_connection_divider_layout
    }

    override fun bind(element: ConnectionDividerViewModel) {
        dividerMessage.text = element.dividerMessage ?: ""
        if (element.isShowButton) dividerButton.show() else dividerButton.hide()
        dividerButton.setOnClickListener {
            val mContext = itemView.context
            val dialog = Dialog(mContext as Activity, Dialog.Type.PROMINANCE)
            dialog.setTitle(mContext.getString(R.string.cb_bot_leave_the_queue))
            dialog.setDesc(mContext.getString(R.string.cb_bot_leave_the_queue_desc_two))
            dialog.setBtnOk(mContext.getString(R.string.cb_bot_ok_text))
            dialog.setBtnCancel(mContext.getString(R.string.cb_bot_cancel_text))
            dialog.setOnOkClickListener {
                element.leaveQueue?.invoke()
                dialog.dismiss()
            }
            dialog.setOnCancelClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
        }

    }
}