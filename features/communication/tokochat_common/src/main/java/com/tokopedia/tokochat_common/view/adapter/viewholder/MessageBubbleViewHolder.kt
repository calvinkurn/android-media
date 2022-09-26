package com.tokopedia.tokochat_common.view.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.view.uimodel.MessageBubbleUiModel

class MessageBubbleViewHolder(itemView: View): BaseViewHolder(itemView) {

    fun bind(item: MessageBubbleUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_tokochat_message_bubble
    }
}
