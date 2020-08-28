package com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_inbox.view.*

class TalkInboxViewHolder(view: View) : AbstractViewHolder<TalkInboxUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_inbox
    }

    override fun bind(element: TalkInboxUiModel) {
        with(element.inboxDetail) {
            setProductThumbnail(productThumbnail)
            setProductName(productName)
            setQuestion(content)
            setNotification(isUnread)
        }
    }

    private fun setProductThumbnail(productThumbnail: String) {
        itemView.talkInboxProductThumbnail.setImageUrl(productThumbnail)
    }

    private fun setProductName(productName: String) {
        itemView.talkInboxProductName.text = productName
    }

    private fun setQuestion(question: String) {
        itemView.talkInboxMessage.text = question
    }

    private fun setNotification(isUnread: Boolean) {
        if(isUnread) {
            itemView.talkInboxNotification.showWithCondition(isUnread)
        }
    }

}