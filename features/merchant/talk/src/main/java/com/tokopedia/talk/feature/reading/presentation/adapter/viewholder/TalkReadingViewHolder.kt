package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_talk_reading.view.*

class TalkReadingViewHolder(view: View, private val threadListener: ThreadListener) : AbstractViewHolder<TalkReadingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reading
    }

    override fun bind(element: TalkReadingUiModel) {
        element.question.apply {
            itemView.setOnClickListener { threadListener.onThreadClicked(questionID) }
            showQuestionWithCondition(state.isMasked, content, maskedContent, questionID)
            if(totalAnswer > 0 && answer.answerID.isNotEmpty()) {
                hideNoAnswersText()
                showProfilePicture(answer.userThumbnail, answer.userId, answer.isSeller, element.shopId)
                showDisplayName(answer.userName, answer.userId, answer.isSeller, element.shopId)
                showSellerLabelWithCondition(answer.isSeller)
                showDate(answer.createTimeFormatted)
                if(answer.state.isMasked) {
                    showMaskedAnswer(answer.maskedContent, questionID)
                    return
                }
                showAnswer(answer.content, questionID)
                showNumberOfAttachedProductsWithCondition(answer.attachedProductCount)
                showNumberOfOtherAnswersWithCondition(totalAnswer, questionID)
            } else {
                showNoAnswersText()
            }
        }
    }

    private fun showQuestionWithCondition(isMasked: Boolean, content: String, maskedContent: String, questionId: String) {
        itemView.readingQuestionTitle.apply {
            text = if(isMasked) {
                isEnabled = false
                maskedContent
            } else {
                isEnabled = true
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            }
        }
    }

    private fun showMaskedAnswer(maskedContent: String, questionId: String) {
        itemView.apply {
            readingMessage.apply {
                text = maskedContent
                isEnabled = false
                show()
            }
            attachedProductIcon.hide()
            attachedProductCount.hide()
            seeOtherAnswers.hide()
        }
    }

    private fun showNoAnswersText() {
        itemView.readingNoAnswersText.show()
        hideOtherElements()
    }

    private fun hideNoAnswersText() {
        itemView.apply {
            readingNoAnswersText.hide()
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String, isSeller: Boolean, shopId: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.readingProfilePicture.apply {
                loadImage(userThumbNail)
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId, isSeller, shopId)
                }
                show()
            }
        } else {
            itemView.readingProfilePicture.hide()
        }
    }

    private fun showDisplayName(userName: String, userId: String, isSeller: Boolean, shopId: String) {
        if(userName.isNotEmpty()) {
            itemView.readingDisplayName.apply{
                text = userName
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId, isSeller, shopId)
                }
                show()
            }
        } else {
            itemView.readingDisplayName.hide()
        }
    }

    private fun showAnswer(answer: String, questionId: String) {
        if(answer.isNotEmpty()) {
            itemView.readingMessage.apply {
                isEnabled = true
                text = HtmlLinkHelper(context, answer).spannedString
                setCustomMovementMethod(fun(link: String) : Boolean {return threadListener.onLinkClicked(link)})
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                val viewTreeObserver = readingMessage.viewTreeObserver
                val maxLines = resources.getInteger(R.integer.talk_reading_max_lines)
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val secondViewTreeObserver = readingMessage.viewTreeObserver
                        secondViewTreeObserver.removeOnGlobalLayoutListener(this)
                        if (lineCount > maxLines) {
                            val endOfLastLine = layout.getLineEnd(maxLines - 1)
                            val spannableStringBuilder = SpannableStringBuilder()
                            spannableStringBuilder.append(text.subSequence(0, endOfLastLine - resources.getInteger(R.integer.talk_reading_length_of_ellipsis))).append(resources.getString(R.string.reading_ellipsis))
                            text = spannableStringBuilder
                        }
                    }
                })
                show()
            }
        } else {
            itemView.readingMessage.hide()
        }
    }

    private fun Typography.setCustomMovementMethod(linkAction: (String) -> Boolean) {
        setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val widget = v as TextView
                val text: Any = widget.text
                if (text is Spanned) {
                    val action = event!!.action
                    if (action == MotionEvent.ACTION_UP
                            || action == MotionEvent.ACTION_DOWN) {
                        var x = event.x.toInt()
                        var y = event.y.toInt()
                        x -= widget.totalPaddingLeft
                        y -= widget.totalPaddingTop
                        x += widget.scrollX
                        y += widget.scrollY
                        val layout: Layout = widget.layout
                        val line: Int = layout.getLineForVertical(y)
                        val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())
                        val link = text.getSpans(off, off, URLSpan::class.java)
                        if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                            return linkAction.invoke(link.first().url.toString())
                        }
                    }
                }
                return false
            }
        })
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.readingDate.apply {
                text = addBulletPointToDate(date)
                show()
            }
        } else {
            itemView.readingDate.hide()
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.readingSellerLabel.show()
        } else {
            itemView.readingSellerLabel.hide()
        }
    }

    private fun addBulletPointToDate(date: String): String {
        return String.format(itemView.context.getString(R.string.talk_formatted_date), date)
    }

    private fun showNumberOfAttachedProductsWithCondition(attachedProducts: Int) {
        if(attachedProducts > 0) {
            itemView.apply {
                attachedProductIcon.show()
                attachedProductCount.text = String.format(context.getString(R.string.reading_attached_product), attachedProducts)
                attachedProductCount.show()
            }
        } else {
            itemView.apply {
                attachedProductIcon.hide()
                attachedProductCount.hide()
            }
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, questionId: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemView.seeOtherAnswers.apply {
                text = String.format(context.getString(R.string.reading_other_answers), answersToShow)
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                show()
            }
        } else {
            itemView.seeOtherAnswers.hide()
        }
    }

    private fun hideOtherElements() {
        itemView.apply {
            attachedProductCount.hide()
            attachedProductIcon.hide()
            readingMessage.hide()
            readingProfilePicture.hide()
            readingDisplayName.hide()
            readingDate.hide()
            seeOtherAnswers.hide()
            readingSellerLabel.hide()
        }
    }


}