package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_talk_reply_product_header.view.*

class TalkReplyProductHeaderViewHolder(view: View, private val talkReplyProductHeaderListener: TalkReplyProductHeaderListener) : AbstractViewHolder<TalkReplyProductHeaderModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_product_header
    }

    override fun bind(element: TalkReplyProductHeaderModel) {
        with(element) {
            setImage(thumbnail)
            setProductName(productName)
        }
    }

    private fun setImage(imageUrl: String) {
        itemView.replyProductHeaderImage.apply {
            if(imageUrl.isNotBlank()) {
                loadImage(imageUrl)
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductClicked()
                }
                return
            }
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_deleted_talk_inbox))
            setOnClickListener(null)
        }
    }

    private fun setProductName(productName: String) {
        itemView.replyProductHeaderName.apply {
            if(productName.isNotBlank()) {
                text = productName
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_96))
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductClicked()
                }
                return
            }
            text = getString(R.string.reply_product_deleted)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_32))
            setOnClickListener(null)
        }
    }
}