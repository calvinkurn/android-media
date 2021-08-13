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
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxViewHolderListener
import com.tokopedia.talk.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_talk_inbox.view.*

class TalkInboxOldViewHolder(
        view: View,
        private val talkInboxViewHolderListener: TalkInboxViewHolderListener
) : AbstractViewHolder<TalkInboxOldUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_inbox_old
    }

    override fun bind(element: TalkInboxOldUiModel) {
        with(element.inboxDetail) {
            setProductThumbnail(productThumbnail)
            setProductName(productName)
            setQuestion(content, isMasked)
            setNotification(isUnread)
            setCountAndDate(totalAnswer, lastReplyTime, element.isSellerView)
            itemView.addOnImpressionListener(element.impressHolder) {
                talkInboxViewHolderListener.onInboxItemImpressed(questionID, adapterPosition, isUnread)
            }
            itemView.setOnClickListener {
                talkInboxViewHolderListener.onInboxItemClicked(null, element, adapterPosition)
            }
        }
    }

    private fun setProductThumbnail(productThumbnail: String) {
        with(itemView) {
            if(productThumbnail.isEmpty()) {
            itemView.talkInboxProductThumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_deleted_talk_placeholder))
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

    private fun setQuestion(question: String, isMasked: Boolean) {
        itemView.talkInboxMessage.apply {
            text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
            if(isMasked) {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                setWeight(Typography.REGULAR)
            } else {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                setWeight(Typography.BOLD)
            }
        }
    }

    private fun setNotification(isUnread: Boolean) {
        if(isUnread) {
            itemView.talkInboxNotification.showWithCondition(isUnread)
        } else {
            itemView.talkInboxNotification.hide()
        }
    }

    private fun setCountAndDate(totalAnswer: Int, date: String, isSellerView: Boolean) {
        with(itemView) {
            when {
                totalAnswer == 0 && isSellerView -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_empty_seller_old)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                        setWeight(Typography.BOLD)
                    }
                }
                totalAnswer == 0 && !isSellerView -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_empty_buyer)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        setWeight(Typography.REGULAR)
                    }
                }
                else -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_old, totalAnswer.toString())
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        setWeight(Typography.REGULAR)
                    }
                }
            }
            talkInboxDate.text = context.getString(R.string.inbox_date_old, date)
        }

    }

}