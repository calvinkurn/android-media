package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.data.model.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnAttachedProductCardClickedListener
import kotlinx.android.synthetic.main.item_talk_reply_attached_product_answer.view.*

class TalkReplyAttachedProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(attachedProduct: AttachedProduct, onAttachedProductCardClickedListener: OnAttachedProductCardClickedListener) {
        itemView.replyAttachedProductImage.loadImage(attachedProduct.thumbnail)
        itemView.replyAttachedProductName.text = attachedProduct.name
        itemView.replyAttachedProductName.text = attachedProduct.priceFormatted
        itemView.setOnClickListener {
            onAttachedProductCardClickedListener.onClickAttachedProduct(attachedProduct.productId)
        }
    }

}