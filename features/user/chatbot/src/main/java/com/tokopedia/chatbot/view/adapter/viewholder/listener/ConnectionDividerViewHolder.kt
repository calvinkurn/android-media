package com.tokopedia.chatbot.view.adapter.viewholder.listener

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.ConnectionDividerUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class ConnectionDividerViewHolder(itemView: View) : AbstractViewHolder<ConnectionDividerUiModel>(itemView) {

    private var dividerMessage: TextView = itemView.findViewById(R.id.chatbot_tv_divider_message)
    private var dividerButton: TextView = itemView.findViewById(R.id.chatbot_tv_divider_button)

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.chatbot_connection_divider_layout
    }

    override fun bind(element: ConnectionDividerUiModel) {
        dividerMessage.text = element.dividerMessage ?: ""
        if (element.isShowButton) dividerButton.show() else dividerButton.hide()
        dividerButton.setOnClickListener {
            element.leaveQueue?.invoke()
        }
    }
}
