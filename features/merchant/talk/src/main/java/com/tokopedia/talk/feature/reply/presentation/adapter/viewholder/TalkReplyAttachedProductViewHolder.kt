package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.databinding.ItemTalkReplyAttachedProductAnswerBinding
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener

class TalkReplyAttachedProductViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTalkReplyAttachedProductAnswerBinding.bind(view)

    fun bind(
        attachedProduct: AttachedProduct,
        attachedProductCardListener: AttachedProductCardListener,
        isInViewHolder: Boolean
    ) {
        if (attachedProduct.productId.isEmpty()) {
            binding.apply {
                blankSpace.show()
                attachedProductCard.hide()
            }
            return
        }
        binding.apply {
            blankSpace.hide()
            attachedProductCard.show()
            replyAttachedProductImage.loadImage(attachedProduct.thumbnail)
            replyAttachedProductName.text = attachedProduct.name
            replyAttachedProductPrice.text = attachedProduct.priceFormatted
            if (isInViewHolder) {
                binding.root.setOnClickListener {
                    attachedProductCardListener.onClickAttachedProduct(attachedProduct.productId)
                }
                binding.replyAttachedProductRemoveButton.hide()
                return
            }
            replyAttachedProductRemoveButton.setOnClickListener {
                attachedProductCardListener.onDeleteAttachedProduct(attachedProduct.productId)
            }
        }
    }

}