package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import kotlinx.android.synthetic.main.item_talk_reply_attached_product_answer.view.*

class TalkReplyAttachedProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(attachedProduct: AttachedProduct, attachedProductCardListener: AttachedProductCardListener, isInViewHolder: Boolean) {
        itemView.replyAttachedProductImage.loadImage(attachedProduct.thumbnail)
        itemView.replyAttachedProductName.text = attachedProduct.name
        itemView.replyAttachedProductPrice.text = attachedProduct.priceFormatted
        itemView.setOnClickListener {
            attachedProductCardListener.onClickAttachedProduct(attachedProduct.productId)
        }
        if(isInViewHolder) {
            itemView.replyAttachedProductRemoveButton.hide()
            return
        }
        itemView.replyAttachedProductRemoveButton.setOnClickListener {
            attachedProductCardListener.onDeleteAttachedProduct(attachedProduct.productId)
        }
    }

}