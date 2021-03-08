package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.item_talk_reply_product_header.view.*

class TalkReplyProductHeaderViewHolder(view: View, private val talkReplyProductHeaderListener: TalkReplyProductHeaderListener) : AbstractViewHolder<TalkReplyProductHeaderModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_product_header
        const val STOCK_THRESHOLD = 5
    }

    override fun bind(element: TalkReplyProductHeaderModel) {
        with(element) {
            itemView.apply {
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductCardClicked(productName, adapterPosition)
                }
                addOnImpressionListener(element.impressHolder) {
                    talkReplyProductHeaderListener.onProductCardImpressed(productName, adapterPosition)
                }
            }
            setImage(thumbnail)
            setProductName(productName, isSellerView)
            setKebabClickListener(isSellerView)
            setStock(isSellerView, stockValue, stockText)
        }
    }

    private fun setKebabClickListener(isSellerView: Boolean) {
        if (isSellerView) {
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

    private fun setStock(isSellerView: Boolean, stockValue: Int, stockText: String) {
        if (isSellerView) {
            itemView.replyProductStock.apply {
                text = stockText
                show()
                if (stockValue < STOCK_THRESHOLD) {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                    return
                }
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                return
            }
        }
        itemView.replyProductStock.hide()
    }

    private fun setImage(imageUrl: String) {
        itemView.replyProductHeaderImage.apply {
            if (imageUrl.isNotBlank()) {
                loadImage(imageUrl)
                return
            }
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_deleted_talk_placeholder))
            setOnClickListener(null)
        }
    }

    private fun setProductName(productName: String, isSellerView: Boolean) {
        itemView.replyProductHeaderName.apply {
            if (isSellerView) {
                val constraintSet = ConstraintSet()
                val constraintLayout = itemView.talkReplyProductHeaderConstraintLayout
                constraintSet.clone(constraintLayout)
                constraintSet.clear(R.id.replyProductHeaderName, ConstraintSet.BOTTOM)
                constraintSet.applyTo(constraintLayout)
            }
            if (productName.isNotBlank()) {
                text = productName
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                return
            }
            text = getString(R.string.reply_product_deleted)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
            setOnClickListener(null)
        }
    }
}