package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview

abstract class AttachmentPreviewViewHolder<in T: SendablePreview>(
        itemView: View,
        protected val attachmentItemPreviewListener: AttachmentItemPreviewListener
) : RecyclerView.ViewHolder(itemView) {

    interface AttachmentItemPreviewListener {
        fun closeItem(model: SendablePreview)
        fun retryLoadCurrentAttachment()
    }

    protected val closeButton: ImageView? = getButtonView(itemView)

    abstract fun getButtonView(itemView: View): ImageView?

    open fun bind(model: T) {
        closeButton?.setOnClickListener {
            attachmentItemPreviewListener.closeItem(model)
        }
    }

    open fun bind(model: T, payloads: List<Any>) {

    }
}