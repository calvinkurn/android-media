package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.ItemTalkReplyBinding
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAttachedProductAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyUnmaskCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label.Companion.GENERAL_LIGHT_GREEN
import com.tokopedia.unifycomponents.Label.Companion.GENERAL_LIGHT_GREY

class TalkReplyViewHolder(view: View,
                          private val attachedProductCardListener: AttachedProductCardListener,
                          private val onKebabClickedListener: OnKebabClickedListener,
                          private val threadListener: ThreadListener
) : AbstractViewHolder<TalkReplyUiModel>(view), TalkReplyUnmaskCardListener {

    companion object {
        val LAYOUT = R.layout.item_talk_reply
        const val IN_VIEWHOLDER = true
    }

    private val binding: ItemTalkReplyBinding = ItemTalkReplyBinding.bind(view)

    override fun onUnmaskQuestionOptionSelected(isMarkNotFraud: Boolean, commentId: String) {
        if (isMarkNotFraud) {
            threadListener.onUnmaskCommentOptionSelected(commentId)
        } else {
            threadListener.onDismissUnmaskCard(commentId)
        }
    }

    override fun bind(element: TalkReplyUiModel) {
        binding.talkReplyContainer.setBackgroundColor(ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        element.answer.apply {
            showProfilePicture(userThumbnail, userId, isSeller, element.shopId)
            showDisplayName(userName, userId, isSeller, element.shopId)
            showDate(createTimeFormatted)
            showLabelWithCondition(isSeller, element.isMyQuestion)
            showAnswer(content, state.isMasked, element.isSellerView, maskedContent, state.allowUnmask)
            showAttachedProducts(attachedProducts.toMutableList())
            showKebabWithConditions(answerID, state.allowReport, state.allowDelete, onKebabClickedListener)
            showMaskingState(state.isMasked, state.allowUnmask, maskedContent, answerID)
            showSmartReplyLabel(state.isAutoReplied)
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String, isSeller: Boolean, shopId: String) {
        if (userThumbNail.isNotEmpty()) {
            binding.replyProfilePicture.apply {
                loadImage(userThumbNail)
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId, isSeller, shopId)
                }
                show()
            }
        } else {
            binding.replyProfilePicture.hide()
        }
    }

    private fun showDisplayName(userName: String, userId: String, isSeller: Boolean, shopId: String) {
        if (userName.isNotEmpty()) {
            binding.replyDisplayName.apply {
                text = HtmlCompat.fromHtml(userName, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId, isSeller, shopId)
                }
                show()
            }
        } else {
            binding.replyDisplayName.hide()
        }
    }

    private fun showDate(date: String) {
        if (date.isNotEmpty()) {
            binding.replyDate.apply {
                text = addBulletPointToDate(date)
                show()
            }
        } else {
            binding.replyDate.hide()
        }
    }

    private fun showLabelWithCondition(isSeller: Boolean, isMyQuestion: Boolean) = with(binding) {
        when {
            isSeller -> {
                replySellerLabel.text = binding.root.context.getString(R.string.reading_seller_label)
                replySellerLabel.setLabelType(GENERAL_LIGHT_GREEN)
                replySellerLabel.show()
            }
            isMyQuestion -> {
                replySellerLabel.text = binding.root.context.getString(R.string.reading_your_question_label)
                replySellerLabel.setLabelType(GENERAL_LIGHT_GREY)
                replySellerLabel.show()
            }
            else -> {
                replySellerLabel.hide()
            }
        }
    }

    private fun addBulletPointToDate(date: String): String {
        return String.format(binding.root.context.getString(R.string.talk_formatted_date), date)
    }

    private fun showAnswer(answer: String, isMasked: Boolean, isSeller: Boolean, maskedContent: String, allowUnmask: Boolean) {
        if (isMasked) {
            binding.replyMessage.apply {
                text = if (allowUnmask || isSeller) HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString() else maskedContent
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
                show()
            }
            return
        }
        if (answer.isNotEmpty()) {
            binding.replyMessage.apply {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                text = HtmlLinkHelper(context, answer).spannedString
                movementMethod = object : LinkMovementMethod() {
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
                                return threadListener.onUrlClicked(link.first().url.toString())
                            }
                        }
                        return super.onTouchEvent(widget, buffer, event);
                    }
                }
                show()
            }
        }
    }

    private fun showAttachedProducts(attachedProducts: MutableList<AttachedProduct>) {
        if (attachedProducts.isNotEmpty()) {
            val attachedProductAdapter = TalkReplyAttachedProductAdapter(attachedProductCardListener, IN_VIEWHOLDER)
            attachedProducts.add(0, AttachedProduct())
            attachedProductAdapter.setData(attachedProducts)
            binding.replyAttachedProductsRecyclerView.apply {
                adapter = attachedProductAdapter
                show()
            }
        } else {
            binding.replyAttachedProductsRecyclerView.hide()
        }
    }

    private fun showKebabWithConditions(commentId: String, allowReport: Boolean, allowDelete: Boolean, onKebabClickedListener: OnKebabClickedListener) {
        if (allowReport || allowDelete) {
            binding.replyKebab.apply {
                setOnClickListener {
                    onKebabClickedListener.onKebabClicked(
                        commentId = commentId,
                        allowReport = allowReport,
                        allowDelete = allowDelete,
                        allowBlock = false
                    )
                }
                show()
            }
        } else {
            binding.replyKebab.hide()
        }
    }

    private fun showMaskingState(isMasked: Boolean, allowUnmask: Boolean, maskedContent: String, commentId: String) {
        when {
            isMasked && allowUnmask -> {
                binding.replyCommentUnmaskCard.apply {
                    show()
                    setListener(this@TalkReplyViewHolder, commentId)
                }
                binding.replyCommentTicker.hide()
            }
            isMasked && !allowUnmask -> {
                binding.replyCommentTicker.apply {
                    show()
                    setTextDescription(maskedContent)
                }
                binding.replyCommentUnmaskCard.hide()
            }
            else -> {
                binding.replyCommentUnmaskCard.hide()
                binding.replyCommentTicker.hide()
            }
        }
    }

    private fun showSmartReplyLabel(isAutoReplied: Boolean) {
        binding.replySmartReplyLabel.apply {
            setLabelType(getColorString(com.tokopedia.unifyprinciples.R.color.Unify_NN50))
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            showWithCondition(isAutoReplied)
        }
    }

    private fun getColorString(color: Int): String {
        return "#${Integer.toHexString(ContextCompat.getColor(binding.root.context, color))}"
    }
}
