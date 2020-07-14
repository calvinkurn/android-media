package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reply_product_header.view.*

class TalkReplyProductHeaderViewHolder(view: View, private val talkReplyProductHeaderListener: TalkReplyProductHeaderListener) : AbstractViewHolder<TalkReplyProductHeaderModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_product_header
    }

    override fun bind(element: TalkReplyProductHeaderModel) {
        with(element) {
            itemView.apply {
                replyProductHeaderImage.apply {
                    loadImage(thumbnail)
                    setOnClickListener {
                        talkReplyProductHeaderListener.onProductClicked()
                    }
                }
                replyProductHeaderName.apply {
                    text = productName
                    setOnClickListener {
                        talkReplyProductHeaderListener.onProductClicked()
                    }
                }
            }
        }
    }
}