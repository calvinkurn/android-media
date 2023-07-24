package com.tokopedia.tokochat.common.view.chatroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.attachment.TokoChatAttachmentMenuViewHolder
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatAttachmentMenuListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatAttachmentMenuUiModel

class AttachmentMenuAdapter : RecyclerView.Adapter<TokoChatAttachmentMenuViewHolder>() {

    val menus = arrayListOf<TokoChatAttachmentMenuUiModel>()
    var listener: TokoChatAttachmentMenuListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoChatAttachmentMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.tokochat_item_attachment_menu, parent, false
        )
        return TokoChatAttachmentMenuViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: TokoChatAttachmentMenuViewHolder, position: Int) {
        holder.bind(menus[position])
    }
}
