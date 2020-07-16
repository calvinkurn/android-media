package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.ProductAttachmentViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.imagepreview.ImagePreviewActivity

class TopchatOldProductAttachmentViewHolder(
        itemView: View?,
        private val listener: ProductAttachmentListener
) : ProductAttachmentViewHolder(itemView, listener) {

    private var thumbnail: ImageView? = itemView?.findViewById(com.tokopedia.chat_common.R.id.attach_product_chat_image)

    override fun bind(product: ProductAttachmentViewModel?) {
        if (product == null) return
        super.bind(product)
        bindImageClick(product)
    }

    private fun bindImageClick(product: ProductAttachmentViewModel) {
        thumbnail?.setOnClickListener {
            listener.trackClickProductThumbnail(product)
            it.context.startActivity(
                    ImagePreviewActivity.getCallingIntent(
                            it.context,
                            ArrayList(product.images),
                            null,
                            0
                    )
            )
        }
    }
}