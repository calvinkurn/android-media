package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.tokopedia.topchat.chatroom.view.viewmodel.PreviewViewModel

abstract class AttachmentPreviewViewHolder<in T: PreviewViewModel>(
        itemView: View,
        private val attachmentItemPreviewListener: AttachmentItemPreviewListener
) : RecyclerView.ViewHolder(itemView) {

    interface AttachmentItemPreviewListener {
        fun closeItem(model: PreviewViewModel, position: Int)
    }

    private val closeButton: ImageView? = getButtonView(itemView)

    abstract fun getButtonView(itemView: View): ImageView?

    open fun bind(model: T, position: Int) {
        closeButton?.setOnClickListener {
            attachmentItemPreviewListener.closeItem(model, position)
        }
    }
}