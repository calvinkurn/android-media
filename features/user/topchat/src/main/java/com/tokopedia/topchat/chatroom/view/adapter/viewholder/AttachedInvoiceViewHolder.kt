package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R

class AttachedInvoiceViewHolder(itemView: View?) : BaseChatViewHolder<AttachInvoiceSentViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_attach_invoice
    }
}