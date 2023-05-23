package com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.ItemTalkInboxOldBinding
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxViewHolderListener
import com.tokopedia.unifyprinciples.Typography

class TalkInboxOldViewHolder(
        view: View,
        private val talkInboxViewHolderListener: TalkInboxViewHolderListener
) : AbstractViewHolder<TalkInboxOldUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_inbox_old
    }

    private val binding: ItemTalkInboxOldBinding = ItemTalkInboxOldBinding.bind(view)

    override fun bind(element: TalkInboxOldUiModel) {
        with(element.inboxDetail) {
            setProductThumbnail(productThumbnail)
            setProductName(productName)
            setQuestion(content, isMasked)
            setNotification(isUnread)
            setCountAndDate(totalAnswer, lastReplyTime, element.isSellerView)
            binding.root.addOnImpressionListener(element.impressHolder) {
                talkInboxViewHolderListener.onInboxItemImpressed(questionID, adapterPosition, isUnread)
            }
            binding.root.setOnClickListener {
                talkInboxViewHolderListener.onInboxItemClicked(null, element, adapterPosition)
            }
        }
    }

    private fun setProductThumbnail(productThumbnail: String) {
        with(binding) {
            if(productThumbnail.isEmpty()) {
            talkInboxProductThumbnail.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.ic_deleted_talk_placeholder))
                talkInboxProductName.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
                return
            }
            talkInboxProductThumbnail.setImageUrl(productThumbnail)
            talkInboxProductName.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
        }
    }

    private fun setProductName(productName: String) {
        binding.talkInboxProductName.text = productName
    }

    private fun setQuestion(question: String, isMasked: Boolean) {
        binding.talkInboxMessage.apply {
            text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
            if(isMasked) {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
                setWeight(Typography.REGULAR)
            } else {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                setWeight(Typography.BOLD)
            }
        }
    }

    private fun setNotification(isUnread: Boolean) {
        if(isUnread) {
            binding.talkInboxNotification.showWithCondition(isUnread)
        } else {
            binding.talkInboxNotification.hide()
        }
    }

    private fun setCountAndDate(totalAnswer: Int, date: String, isSellerView: Boolean) {
        with(binding) {
            when {
                totalAnswer == 0 && isSellerView -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_empty_seller_old)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                        setWeight(Typography.BOLD)
                    }
                }
                totalAnswer == 0 && !isSellerView -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_empty_buyer)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
                        setWeight(Typography.REGULAR)
                    }
                }
                else -> {
                    talkInboxAnswerCount.apply {
                        text = context.getString(R.string.inbox_total_count_old, totalAnswer.toString())
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
                        setWeight(Typography.REGULAR)
                    }
                }
            }
            talkInboxDate.text = root.context.getString(R.string.inbox_date_old, date)
        }

    }

}