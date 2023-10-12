package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.ItemTalkReplyProductHeaderBinding
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener

class TalkReplyProductHeaderViewHolder(
    view: View,
    private val talkReplyProductHeaderListener: TalkReplyProductHeaderListener
) : AbstractViewHolder<TalkReplyProductHeaderModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_product_header
        const val STOCK_THRESHOLD = 5
    }

    private val binding = ItemTalkReplyProductHeaderBinding.bind(view)

    override fun bind(element: TalkReplyProductHeaderModel) {
        with(element) {
            itemView.apply {
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductCardClicked(
                        productName,
                        adapterPosition
                    )
                }
                addOnImpressionListener(element.impressHolder) {
                    talkReplyProductHeaderListener.onProductCardImpressed(
                        productName,
                        adapterPosition
                    )
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
            binding.replyProductHeaderKebab.apply {
                setOnClickListener {
                    talkReplyProductHeaderListener.onKebabClicked()
                }
                show()
            }
            return
        }
        binding.replyProductHeaderKebab.hide()
    }

    private fun setStock(isSellerView: Boolean, stockValue: Int, stockText: String) {
        if (isSellerView) {
            binding.replyProductStock.apply {
                text = stockText
                show()
                if (stockValue < STOCK_THRESHOLD) {
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_RN500
                        )
                    )
                    return
                }
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                    )
                )
                return
            }
        }
        binding.replyProductStock.hide()
    }

    private fun setImage(imageUrl: String) {
        binding.replyProductHeaderImage.apply {
            if (imageUrl.isNotBlank()) {
                loadImage(imageUrl)
                return
            }
            setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_deleted_talk_placeholder
                )
            )
            setOnClickListener(null)
        }
    }

    private fun setProductName(productName: String, isSellerView: Boolean) {
        binding.replyProductHeaderName.apply {
            if (isSellerView) {
                val constraintSet = ConstraintSet()
                val constraintLayout = binding.talkReplyProductHeaderConstraintLayout
                constraintSet.clone(constraintLayout)
                constraintSet.clear(R.id.replyProductHeaderName, ConstraintSet.BOTTOM)
                constraintSet.applyTo(constraintLayout)
            }
            if (productName.isNotBlank()) {
                text = productName
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                    )
                )
                return
            }
            text = getString(R.string.reply_product_deleted)
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
                )
            )
            setOnClickListener(null)
        }
    }
}