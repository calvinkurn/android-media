package com.tokopedia.topchat.chatroom.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.ProductPreview

class AttachmentPreviewAdapter(
        private val attachmentPreviewListener: AttachmentPreviewListener
) : RecyclerView.Adapter<ProductPreviewViewHolder>(), ProductPreviewViewHolder.ItemListener {

    interface AttachmentPreviewListener {
        fun clearAttachmentPreview()
    }

    private var attachments = arrayListOf<ProductPreview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPreviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_preview, parent, false)
        return ProductPreviewViewHolder(view, this)
    }

    override fun getItemCount(): Int = attachments.size

    override fun onBindViewHolder(holder: ProductPreviewViewHolder, position: Int) {
        holder.bind(attachments[position], position)
    }

    fun updateAttachments(attachmentPreview: ArrayList<ProductPreview>) {
        attachments = attachmentPreview
        notifyDataSetChanged()
    }

    override fun closeItem(productPreview: ProductPreview, position: Int) {
        attachments.remove(productPreview)
        notifyItemRemoved(position)
        if (noProductPreview()) {
            attachmentPreviewListener.clearAttachmentPreview()
        }
    }

    private fun noProductPreview(): Boolean = attachments.isEmpty()
}