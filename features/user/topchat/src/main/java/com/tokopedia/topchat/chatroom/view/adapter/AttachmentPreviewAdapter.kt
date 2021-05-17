package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview

class AttachmentPreviewAdapter(
        private val attachmentPreviewListener: AttachmentPreviewListener,
        private val attachmentPreviewFactory: AttachmentPreviewFactory
) : RecyclerView.Adapter<AttachmentPreviewViewHolder<SendablePreview>>(),
        AttachmentPreviewViewHolder.AttachmentItemPreviewListener {

    interface AttachmentPreviewListener {
        fun clearAttachmentPreview()
        fun hideProductPreviewLayout()
    }

    private var attachments = arrayListOf<SendablePreview>()

    override fun getItemViewType(position: Int): Int {
        return attachments[position].type(attachmentPreviewFactory)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): AttachmentPreviewViewHolder<SendablePreview> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return attachmentPreviewFactory.create(viewType, view, this) as AttachmentPreviewViewHolder<SendablePreview>
    }

    override fun getItemCount(): Int = attachments.size

    override fun onBindViewHolder(holder: AttachmentPreviewViewHolder<SendablePreview>, position: Int) {
        holder.bind(attachments[position])
    }

    fun clear() {
        attachments.clear()
        notifyDataSetChanged()
    }

    fun updateAttachments(attachmentPreview: ArrayList<SendablePreview>) {
        attachments = attachmentPreview
        notifyDataSetChanged()
    }

    fun isShowingProduct(): Boolean {
        return attachments.getOrNull(0) is SendableProductPreview
    }

    override fun closeItem(model: SendablePreview) {
        val modelPosition = attachments.indexOf(model)

        if (modelPosition == RecyclerView.NO_POSITION) return

        attachments.removeAt(modelPosition)
        notifyItemRemoved(modelPosition)
        if (noAttachmentPreview()) {
            attachmentPreviewListener.clearAttachmentPreview()
        }
    }

    private fun noAttachmentPreview(): Boolean = attachments.isEmpty()
}