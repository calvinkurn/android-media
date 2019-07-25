package com.tokopedia.topchat.chatroom.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.PreviewViewModel

class AttachmentPreviewAdapter(
        private val attachmentPreviewListener: AttachmentPreviewListener,
        private val attachmentPreviewFactory: AttachmentPreviewFactory
) : RecyclerView.Adapter<AttachmentPreviewViewHolder<PreviewViewModel>>(),
        AttachmentPreviewViewHolder.AttachmentItemPreviewListener {

    interface AttachmentPreviewListener {
        fun clearAttachmentPreview()
    }

    private var attachments = arrayListOf<PreviewViewModel>()

    override fun getItemViewType(position: Int): Int {
        return attachments[position].type(attachmentPreviewFactory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentPreviewViewHolder<PreviewViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return attachmentPreviewFactory.create(viewType, view, this) as AttachmentPreviewViewHolder<PreviewViewModel>
    }

    override fun getItemCount(): Int = attachments.size

    override fun onBindViewHolder(holder: AttachmentPreviewViewHolder<PreviewViewModel>, position: Int) {
        holder.bind(attachments[position], position)
    }

    fun updateAttachments(attachmentPreview: ArrayList<PreviewViewModel>) {
        attachments = attachmentPreview
        notifyDataSetChanged()
    }

    override fun closeItem(model: PreviewViewModel, position: Int) {
        attachments.remove(model)
        notifyItemRemoved(position)
        if (noAttachmentPreview()) {
            attachmentPreviewListener.clearAttachmentPreview()
        }
    }

    private fun noAttachmentPreview(): Boolean = attachments.isEmpty()
}