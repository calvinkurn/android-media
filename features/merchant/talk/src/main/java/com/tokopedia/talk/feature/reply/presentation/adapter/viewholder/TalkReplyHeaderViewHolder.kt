package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
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
                replyHeaderTNC.text = HtmlLinkHelper(context, getString(R.string.reply_header_tnc)).spannedString
                replyHeaderTNC.movementMethod = object : LinkMovementMethod() {
                    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
                        val action = event.action

                        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                            var x = event.x
                            var y = event.y.toInt()

                            x -= widget.totalPaddingLeft
                            y -= widget.totalPaddingTop

                            x += widget.scrollX
                            y += widget.scrollY

                            val layout = widget.layout
                            val line = layout.getLineForVertical(y)
                            val off = layout.getOffsetForHorizontal(line, x)

                            val link = buffer.getSpans(off, off, URLSpan::class.java)
                            if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                                return talkReplyHeaderListener.onTermsAndConditionsClicked()
                            }
                        }
                        return super.onTouchEvent(widget, buffer, event);
                    }
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