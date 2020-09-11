package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyUnmaskCardListener
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
        AbstractViewHolder<TalkReplyHeaderModel>(view), TalkReplyUnmaskCardListener {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_header
    }

    override fun bind(element: TalkReplyHeaderModel) {
        with(element) {
            showQuestionWithCondition(isMasked, question, maskedContent, allowUnmask)
            showKebabWithConditions(allowReport, allowDelete, onKebabClickedListener)
            showFollowWithCondition(allowFollow, isFollowed, talkReplyHeaderListener)
            showProfilePictureAndNameWithCondition(element.userThumbnail, element.userId.toString())
            showHeaderDateWithCondition(date)
            showUserNameWithCondition(element.userName, element.isMyQuestion)
            showUnmaskCardWithCondition(element.allowUnmask)
            itemView.apply {
                replyHeaderDate.text = context.getString(R.string.reply_dot_builder, date)
                replyHeaderTNC.text = HtmlLinkHelper(context, getString(R.string.reply_header_tnc)).spannedString
                replyHeaderTNC.setCustomMovementMethod { talkReplyHeaderListener.onTermsAndConditionsClicked() }
            }
        }
    }

    override fun onUnmaskQuestionOptionSelected(isMarkNotFraud: Boolean, commentId: String) {
        if(isMarkNotFraud) {
            threadListener.onUnmaskCommentOptionSelected(commentId)
        } else {
            threadListener.onDismissUnmaskCard(commentId)
            itemView.replyUnmaskCard.hide()
        }
    }

    private fun showUnmaskCardWithCondition(allowUnmask: Boolean) {
        if(allowUnmask) {
            itemView.replyUnmaskCard.apply {
                show()
                setListener(this@TalkReplyHeaderViewHolder, "")
            }
        } else {
            itemView.replyUnmaskCard.hide()
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
        replyUserName.text = userName
        if (isMyQuestion) {
            labelMyQuestion.show()
            return
        }
        labelMyQuestion.hide()
    }

    private fun showQuestionWithCondition(isMasked: Boolean, question: String, maskedContent: String, allowUnmask: Boolean) {
        itemView.replyHeaderMessage.apply {
            if (isMasked) {
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_32))
                if(!allowUnmask) {
                    setType(Typography.BODY_2)
                    setWeight(Typography.REGULAR)
                    text = maskedContent
                    return
                }
                text = question
                setType(Typography.HEADING_4)
                setWeight(Typography.BOLD)
            }
            text = HtmlLinkHelper(context, question).spannedString
            setType(Typography.HEADING_4)
            setWeight(Typography.BOLD)
            setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_96))
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
        } else {
            itemView.replyHeaderKebab.hide()
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
        } else {
            itemView.replyHeaderFollowButton.hide()
        }
    }
}