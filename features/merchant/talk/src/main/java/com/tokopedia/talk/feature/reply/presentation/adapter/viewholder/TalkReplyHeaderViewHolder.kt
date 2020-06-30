package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.common.utils.setCustomMovementMethod
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_talk_reply_header.view.*

class TalkReplyHeaderViewHolder(view: View,
                                private val onKebabClickedListener: OnKebabClickedListener,
                                private val talkReplyHeaderListener: TalkReplyHeaderListener,
                                private val threadListener: ThreadListener) :
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
                replyHeaderTNC.text = HtmlLinkHelper(context, getString(R.string.reply_header_tnc)).spannedString
                replyHeaderTNC.setCustomMovementMethod { talkReplyHeaderListener.onTermsAndConditionsClicked() }
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
            text = HtmlLinkHelper(context, question).spannedString
            setCustomMovementMethod(fun(link: String) : Boolean { return threadListener.onUrlClicked(link) })
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
                    text = context.getString(R.string.reply_header_following_button)
                    buttonType = UnifyButton.Type.ALTERNATE
                } else {
                    text = context.getString(R.string.reply_header_follow_button)
                    buttonType = UnifyButton.Type.MAIN
                }
                show()
            }
        }
    }
}