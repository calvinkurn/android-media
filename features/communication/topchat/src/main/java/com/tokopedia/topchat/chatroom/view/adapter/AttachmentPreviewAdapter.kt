package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel

class AttachmentPreviewAdapter(
        private val attachmentPreviewListener: AttachmentPreviewListener,
        private val attachmentPreviewFactory: AttachmentPreviewFactory
) : RecyclerView.Adapter<AttachmentPreviewViewHolder<SendablePreview>>(),
        AttachmentPreviewViewHolder.AttachmentItemPreviewListener {

    interface AttachmentPreviewListener {
        fun clearAttachmentPreview()
        fun hideProductPreviewLayout()
        fun notifyPreviewRemoved(model: SendablePreview)
        fun reloadCurrentAttachment()
    }

    private var attachments = arrayListOf<SendablePreview>()

    fun updateDeferredAttachment(mapProducts: ArrayMap<String, Attachment>) {
        for ((index, attachment) in attachments.withIndex()) {
            if (attachment is TopchatProductAttachmentPreviewUiModel) {
                val actualProduct = mapProducts[attachment.productId] ?: continue
                attachment.updateData(actualProduct.parsedAttributes)
                if (actualProduct is ErrorAttachment) {
                    attachment.syncError()
                } else {
                    attachment.finishLoading()
                }
                notifyItemChanged(index, Payload.REBIND)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return attachments[position].type(attachmentPreviewFactory)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): AttachmentPreviewViewHolder<SendablePreview> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return attachmentPreviewFactory.create(viewType, view, this) as AttachmentPreviewViewHolder<SendablePreview>
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

    override fun onBindViewHolder(holder: AttachmentPreviewViewHolder<SendablePreview>, position: Int) {
        holder.bind(attachments[position])
    }

    override fun onBindViewHolder(
        holder: AttachmentPreviewViewHolder<SendablePreview>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(attachments[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
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
        return attachments.getOrNull(0) is TopchatProductAttachmentPreviewUiModel
    }

    override fun closeItem(model: SendablePreview) {
        val modelPosition = attachments.indexOf(model)
        if (modelPosition == RecyclerView.NO_POSITION) return
        if (model is DeferredAttachment && (model.isLoading || model.isError)) {
            attachments.clear()
            notifyDataSetChanged()
        } else {
            attachments.removeAt(modelPosition)
            notifyItemRemoved(modelPosition)
        }
        if (noAttachmentPreview()) {
            attachmentPreviewListener.clearAttachmentPreview()
        }
        attachmentPreviewListener.notifyPreviewRemoved(model)
    }

    override fun retryLoadCurrentAttachment() {
        attachmentPreviewListener.reloadCurrentAttachment()
    }

    private fun noAttachmentPreview(): Boolean = attachments.isEmpty()
}