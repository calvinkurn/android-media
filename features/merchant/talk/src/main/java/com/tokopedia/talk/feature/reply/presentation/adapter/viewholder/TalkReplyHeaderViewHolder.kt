package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_talk_reply_header.view.*

class TalkReplyHeaderViewHolder(view: View,
                                private val onKebabClickedListener: OnKebabClickedListener,
                                private val talkReplyHeaderListener: TalkReplyHeaderListener) :
        AbstractViewHolder<TalkReplyHeaderModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_header
    }

    override fun bind(element: TalkReplyHeaderModel) {
        with(element) {
            showQuestionWithCondition(isMasked, question, maskedContent)
            showKebabWithConditions(allowReport, allowDelete, onKebabClickedListener)
            showFollowWithCondition(allowFollow, isFollowed, talkReplyHeaderListener)
            itemView.apply {
                replyHeaderDate.text = date
                replyHeaderTNC.setOnClickListener {
                    talkReplyHeaderListener.onTermsAndConditionsClicked()
                }
            }
        }
    }

    private fun showQuestionWithCondition(isMasked: Boolean, question: String, maskedContent: String) {
        itemView.replyHeaderMessage.apply {
            if(isMasked) {
                text = maskedContent
                isEnabled = false
                return
            }
            text = question
        }
    }

    private fun showKebabWithConditions(allowReport: Boolean, allowDelete: Boolean, onKebabClickedListener: OnKebabClickedListener) {
        if(allowReport || allowDelete){
            itemView.replyHeaderKebab.apply {
                setOnClickListener {
                    onKebabClickedListener.onKebabClicked("", allowReport, allowDelete)
                }
                show()
            }
        }
    }

    private fun showFollowWithCondition(allowFollow: Boolean, isFollowed: Boolean, talkReplyHeaderListener: TalkReplyHeaderListener) {
        if(allowFollow) {
            itemView.replyHeaderFollowButton.apply {
                setOnClickListener {
                    talkReplyHeaderListener.onFollowUnfollowButtonClicked()
                }
                if(isFollowed) {
                    setButtonToFollowed()
                } else {
                    setButtonToUnfollowed()
                }
                show()
            }
        }
    }

    private fun setButtonToFollowed() {
        itemView.replyHeaderFollowButton.apply {
            text = context.getString(R.string.reply_header_following_button)
            buttonType = UnifyButton.Type.ALTERNATE
        }
    }

    private fun setButtonToUnfollowed() {
        itemView.replyHeaderFollowButton.apply {
            text = context.getString(R.string.reply_header_follow_button)
            buttonType = UnifyButton.Type.MAIN
        }
    }
}