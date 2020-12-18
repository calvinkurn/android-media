package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.item_talk_reply_product_header.view.*

class TalkReplyProductHeaderViewHolder(view: View, private val talkReplyProductHeaderListener: TalkReplyProductHeaderListener) : AbstractViewHolder<TalkReplyProductHeaderModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_product_header
    }

    override fun bind(element: TalkReplyProductHeaderModel) {
        with(element) {
            itemView.apply {
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductCardClicked(productName, adapterPosition)
                }
                addOnImpressionListener(ImpressHolder()) {
                    talkReplyProductHeaderListener.onProductCardImpressed(productName, adapterPosition)
                }
            }
            setImage(thumbnail)
            setProductName(productName, isSellerView)
            setKebabClickListener(isSellerView)
            setStock(isSellerView, stock)
        }
    }

    private fun setKebabClickListener(isSellerView: Boolean) {
        if(isSellerView) {
            itemView.replyProductHeaderKebab.apply {
                setOnClickListener {
                    talkReplyProductHeaderListener.onKebabClicked()
                }
                show()
            }
            return
        }
        itemView.replyProductHeaderKebab.hide()
    }

    private fun setStock(isSellerView: Boolean, stock: String) {
        if(isSellerView) {
            itemView.replyProductStock.apply {
                text = stock
                show()
            }
            return
        }
        itemView.replyProductStock.hide()
    }

    private fun setImage(imageUrl: String) {
        itemView.replyProductHeaderImage.apply {
            if(imageUrl.isNotBlank()) {
                loadImage(imageUrl)
                return
            }
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_deleted_talk_placeholder))
            setOnClickListener(null)
        }
    }

    private fun setProductName(productName: String, isSellerView: Boolean) {
        itemView.replyProductHeaderName.apply {
            if(productName.isNotBlank()) {
                text = productName
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                return
            }
            text = getString(R.string.reply_product_deleted)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
            setOnClickListener(null)
            if(isSellerView) {
                val constraintSet = ConstraintSet()
                val constraintLayout = talkReplyProductHeaderConstraintLayout
                constraintSet.clear(replyProductHeaderName.id, ConstraintSet.BOTTOM)
                constraintSet.applyTo(constraintLayout)
            }
        }
    }
}