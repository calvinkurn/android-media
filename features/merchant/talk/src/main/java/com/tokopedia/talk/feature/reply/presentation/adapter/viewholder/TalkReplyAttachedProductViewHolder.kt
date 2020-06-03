package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import kotlinx.android.synthetic.main.item_talk_reply_attached_product_answer.view.*

class TalkReplyAttachedProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(attachedProduct: AttachedProduct, attachedProductCardListener: AttachedProductCardListener, isInViewHolder: Boolean) {
        if(attachedProduct.productId.isEmpty()) {
            itemView.apply {
                blankSpace.show()
                attachedProductCard.hide()
            }
            return
        }
        itemView.apply {
            blankSpace.hide()
            attachedProductCard.show()
            replyAttachedProductImage.loadImage(attachedProduct.thumbnail)
            replyAttachedProductName.text = attachedProduct.name
            replyAttachedProductPrice.text = attachedProduct.priceFormatted
            if(isInViewHolder) {
                setOnClickListener {
                    attachedProductCardListener.onClickAttachedProduct(attachedProduct.productId)
                }
                itemView.replyAttachedProductRemoveButton.hide()
                return
            }
            replyAttachedProductRemoveButton.setOnClickListener {
                attachedProductCardListener.onDeleteAttachedProduct(attachedProduct.productId)
            }
        }
    }

}