package com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxViewHolderListener
import com.tokopedia.talk.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_talk_inbox.view.*

class TalkInboxViewHolder(
        view: View,
        private val talkInboxViewHolderListener: TalkInboxViewHolderListener
) : AbstractViewHolder<TalkInboxUiModel>(view) {

    companion object {
        const val DELETED_PRODUCT_PLACEHOLDER = "https://ecs7.tokopedia.net/img/android/others/talk_reply_deleted_product_placeholder.png"
        val LAYOUT = R.layout.item_talk_inbox
    }

    override fun bind(element: TalkInboxUiModel) {
        with(element.inboxDetail) {
            setProductThumbnail(productThumbnail)
            setProductName(productName)
            setQuestion(content)
            setNotification(if(element.isSellerView) state.isUnresponded else isUnread)
            setCountAndDate(totalAnswer, lastReplyTime)
            setAlertState(state.hasProblem, element.isSellerView)
            itemView.addOnImpressionListener(element.impressHolder) {
                talkInboxViewHolderListener.onInboxItemImpressed(questionID, adapterPosition, isUnread)
            }
            itemView.setOnClickListener {
                talkInboxViewHolderListener.onInboxItemClicked(element, null, adapterPosition)
            }
        }
    }

    private fun setProductThumbnail(productThumbnail: String) {
        with(itemView) {
            if (productThumbnail.isEmpty()) {
                itemView.talkInboxProductThumbnail.setImageUrl(DELETED_PRODUCT_PLACEHOLDER)
                talkInboxProductName.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                return
            }
            talkInboxProductThumbnail.setImageUrl(productThumbnail)
            talkInboxProductName.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    private fun setProductName(productName: String) {
        itemView.talkInboxProductName.text = productName
    }

    private fun setQuestion(question: String) {
        itemView.talkInboxMessage.apply {
            text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            setWeight(Typography.BOLD)
        }
    }

    private fun setNotification(isUnread: Boolean) {
        if (isUnread) {
            itemView.talkInboxNotification.showWithCondition(isUnread)
        } else {
            itemView.talkInboxNotification.hide()
        }
    }

    private fun setCountAndDate(totalAnswer: Int, date: String) {
        with(itemView) {
            when (totalAnswer) {
                0 -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_empty_seller)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        setWeight(Typography.BOLD)
                    }
                }
                else -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count, totalAnswer.toString())
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        setWeight(Typography.REGULAR)
                    }
                }
            }
            talkInboxDate.text = date
        }

    }

    private fun setAlertState(hasProblem: Boolean, isSellerView: Boolean) {
        itemView.talkInboxAlertSignifier.showWithCondition(hasProblem && isSellerView)
    }

}