package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.ItemTalkReplyHeaderBinding
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyUnmaskCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TalkReplyHeaderViewHolder(view: View,
                                private val onKebabClickedListener: OnKebabClickedListener,
                                private val talkReplyHeaderListener: TalkReplyHeaderListener,
                                private val threadListener: ThreadListener
) :
        AbstractViewHolder<TalkReplyHeaderModel>(view), TalkReplyUnmaskCardListener {

    companion object {
        val LAYOUT = R.layout.item_talk_reply_header
    }

    private val binding = ItemTalkReplyHeaderBinding.bind(view)

    override fun bind(element: TalkReplyHeaderModel) {
        with(element) {
            showQuestionWithCondition(isMasked, question, maskedContent, allowUnmask, isSeller)
            showKebabWithConditions(
                allowReport = allowReport,
                allowDelete = allowDelete,
                allowBlock = allowReport && isSeller,
                onKebabClickedListener = onKebabClickedListener
            )
            showFollowWithCondition(allowFollow, isFollowed, talkReplyHeaderListener)
            showProfilePictureAndNameWithCondition(element.userThumbnail, element.userId.toString())
            showHeaderDateWithCondition(date)
            showUserNameWithCondition(element.userName, element.isMyQuestion)
            showMaskingState(element.isMasked, element.allowUnmask, element.maskedContent)
            binding.apply {
                replyHeaderDate.text = itemView.context.getString(R.string.reply_dot_builder, date)
                replyHeaderTNC.text = HtmlLinkHelper(itemView.context, getString(R.string.reply_header_tnc)).spannedString
                replyHeaderTNC.setCustomMovementMethod { talkReplyHeaderListener.onTermsAndConditionsClicked() }
            }
        }
    }

    override fun onUnmaskQuestionOptionSelected(isMarkNotFraud: Boolean, commentId: String) {
        if (isMarkNotFraud) {
            threadListener.onUnmaskCommentOptionSelected(commentId)
        } else {
            threadListener.onDismissUnmaskCard(commentId)
        }
    }

    private fun showHeaderDateWithCondition(date: String) = with(binding) {
        if (date.isNotEmpty()) {
            replyHeaderDate.show()
            replyHeaderDate.text = itemView.context.getString(R.string.reply_dot_builder, date)
        } else {
            replyHeaderDate.hide()
        }
    }

    private fun showProfilePictureAndNameWithCondition(userThumbnail: String, userId: String) = with(binding) {
        replyUserImage.shouldShowWithAction(userThumbnail.isNotEmpty()) {
            replyUserImage.apply {
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

    private fun showUserNameWithCondition(userName: String, isMyQuestion: Boolean) = with(binding) {
        replyUserName.text = userName
        if (isMyQuestion) {
            labelMyQuestion.show()
            return
        }
        labelMyQuestion.hide()
    }

    private fun showQuestionWithCondition(isMasked: Boolean, question: String, maskedContent: String, allowUnmask: Boolean, isSeller: Boolean) {
        binding.replyHeaderMessage.apply {
            if (isMasked) {
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                if (allowUnmask && isSeller) {
                    text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    setType(Typography.HEADING_4)
                    setWeight(Typography.BOLD)
                    return
                }
                setType(Typography.BODY_2)
                setWeight(Typography.REGULAR)
                text = maskedContent
                return
            }
            text = HtmlLinkHelper(context, question).spannedString
            setType(Typography.HEADING_4)
            setWeight(Typography.BOLD)
            setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            setCustomMovementMethod(fun(link: String): Boolean { return threadListener.onUrlClicked(link) })
        }
    }

    private fun showKebabWithConditions(
        allowReport: Boolean,
        allowDelete: Boolean,
        allowBlock: Boolean,
        onKebabClickedListener: OnKebabClickedListener
    ) {
        if (allowReport || allowDelete || allowBlock) {
            binding.replyHeaderKebab.apply {
                setOnClickListener {
                    onKebabClickedListener.onKebabClicked(
                        commentId = "",
                        allowReport = allowReport,
                        allowDelete = allowDelete,
                        allowBlock = allowBlock
                    )
                }
                show()
            }
        } else {
            binding.replyHeaderKebab.hide()
        }
    }

    private fun showFollowWithCondition(allowFollow: Boolean, isFollowed: Boolean, talkReplyHeaderListener: TalkReplyHeaderListener) {
        if (allowFollow) {
            binding.replyHeaderFollowButton.apply {
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
            binding.replyHeaderFollowButton.hide()
        }
    }

    private fun showMaskingState(isMasked: Boolean, allowUnmask: Boolean, maskedContent: String) {
        if (isMasked) {
            if (allowUnmask) {
                binding.replyUnmaskCard.apply {
                    show()
                    setListener(this@TalkReplyHeaderViewHolder, "")
                }
            } else {
                binding.replyQuestionTicker.apply {
                    show()
                    setTextDescription(maskedContent)
                }
                binding.replyUnmaskCard.hide()
            }
        } else {
            binding.replyQuestionTicker.hide()
            binding.replyUnmaskCard.hide()
        }
    }
}
