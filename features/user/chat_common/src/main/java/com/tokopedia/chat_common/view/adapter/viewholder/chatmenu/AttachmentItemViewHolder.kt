package com.tokopedia.chat_common.view.adapter.viewholder.chatmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.unifycomponents.setImage

class AttachmentItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    interface AttachmentViewHolderListener {
        fun closeMenu()
    }

    private var icon: ImageView? = itemView.findViewById(R.id.ivIcon)
    private var title: TextView? = itemView.findViewById(R.id.tvTitle)

    fun bind(
            attachmentMenu: AttachmentMenu,
            listener: AttachmentMenu.AttachmentMenuListener?,
            viewHolderListener: AttachmentViewHolderListener?
    ) {
        icon?.setImage(attachmentMenu.icon, 0f)
        title?.text = attachmentMenu.title
        if (listener == null) return
        itemView.setOnClickListener {
            attachmentMenu.onClick(listener)
            viewHolderListener?.closeMenu()
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): AttachmentItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attachment_chat_common, parent, false)
            return AttachmentItemViewHolder(view)
        }
    }
}