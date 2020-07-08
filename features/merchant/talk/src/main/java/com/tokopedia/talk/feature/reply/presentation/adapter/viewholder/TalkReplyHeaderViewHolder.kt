package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
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
            showProfilePictureAndNameWithCondition(element.userThumbnail, element.userId.toString())
            showHeaderDateWithCondition(date)
            showUserNameWithCondition(element.userName, element.isMyQuestion)
            itemView.apply {
                replyHeaderDate.text = context.getString(R.string.reply_dot_builder, date)
                replyHeaderTNC.text = HtmlLinkHelper(context, getString(R.string.reply_header_tnc)).spannedString
                replyHeaderTNC.setCustomMovementMethod { talkReplyHeaderListener.onTermsAndConditionsClicked() }
            }
        }
    }

    private fun showHeaderDateWithCondition(date: String) = with(itemView) {
        if (date.isNotEmpty()) {
            replyHeaderDate.show()
            replyHeaderDate.text = context.getString(R.string.reply_dot_builder, date)
        } else {
            replyHeaderDate.hide()
        }
    }

    private fun showProfilePictureAndNameWithCondition(userThumbnail: String, userId: String) = with(itemView) {
        replyUserImage?.shouldShowWithAction(userThumbnail.isNotEmpty()) {
            replyUserImage?.apply {
                loadImage(userThumbnail)
                setOnClickListener {
                    threadListener.goToProfilePage(userId)
                }
                show()
            }
        }
    }

    private fun Typography.setCustomMovementMethod(linkAction: (String) -> Boolean) {
        this.movementMethod = object : LinkMovementMethod() {
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
                        return linkAction.invoke(link.first().url.toString())
                    }
                }
                return super.onTouchEvent(widget, buffer, event);
            }
        }
    }

    private fun showUserNameWithCondition(userName: String, isMyQuestion: Boolean) = with(itemView) {
        if (isMyQuestion) {
            replyUserName.hide()
            labelMyQuestion.show()
        } else {
            labelMyQuestion.hide()
            replyUserName.text = userName
        }
    }

    private fun showQuestionWithCondition(isMasked: Boolean, question: String, maskedContent: String) {
        itemView.replyHeaderMessage.apply {
            if (isMasked) {
                text = maskedContent
                isEnabled = false
                return
            }
            text = HtmlLinkHelper(context, question).spannedString
            setCustomMovementMethod(fun(link: String): Boolean { return threadListener.onUrlClicked(link) })
        }
    }

    private fun showKebabWithConditions(allowReport: Boolean, allowDelete: Boolean, onKebabClickedListener: OnKebabClickedListener) {
        if (allowReport || allowDelete) {
            itemView.replyHeaderKebab.apply {
                setOnClickListener {
                    onKebabClickedListener.onKebabClicked("", allowReport, allowDelete)
                }
                show()
            }
        }
    }

    private fun showFollowWithCondition(allowFollow: Boolean, isFollowed: Boolean, talkReplyHeaderListener: TalkReplyHeaderListener) {
        if (allowFollow) {
            itemView.replyHeaderFollowButton.apply {
                setOnClickListener {
                    talkReplyHeaderListener.onFollowUnfollowButtonClicked()
                }
                if (isFollowed) {
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