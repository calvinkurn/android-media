package com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_talk_inbox.view.*

class TalkInboxViewHolder(view: View) : AbstractViewHolder<TalkInboxUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_inbox
    }

    override fun bind(element: TalkInboxUiModel) {
        with(element.inboxDetail) {
            setProductThumbnail(productThumbnail)
            setProductName(productName)
            setQuestion(content, isMasked)
            setNotification(isUnread)
        }
    }

    private fun setProductThumbnail(productThumbnail: String) {
        itemView.talkInboxProductThumbnail.setImageUrl(productThumbnail)
    }

    private fun setProductName(productName: String) {
        itemView.talkInboxProductName.text = productName
    }

    private fun setQuestion(question: String, isMasked: Boolean) {
        itemView.talkInboxMessage.apply {
            text = question
            if(isMasked) {
                setTextColor(ContextCompat.getColor(context, R.color.Neutral_N700_32))
                setWeight(Typography.REGULAR)
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.Neutral_N700_96))
                setWeight(Typography.BOLD)
            }
        }
    }

    private fun setNotification(isUnread: Boolean) {
        if(isUnread) {
            itemView.talkInboxNotification.showWithCondition(isUnread)
        }
    }

}