package com.tokopedia.product.detail.view.viewholder

import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_dynamic_discussion_most_helpful_question_and_answer.view.*

class ProductDiscussionQuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(question: Question, dynamicProductDetailListener: DynamicProductDetailListener, type: String, name: String, adapterPosition: Int, itemCount: Int) {
        with(question) {
            itemView.setOnClickListener { dynamicProductDetailListener.goToTalkReply(questionID, ComponentTrackDataModel(type, name, adapterPosition), itemCount.toString()) }
            showQuestion(content)
            showInquirerName(userName)
            showInquirerProfilePicture(userThumbnail)
            showInquiryDate(createTimeFormatted)
            if(totalAnswer > 0 && answer.answerID.isNotEmpty()) {
                hideNoAnswersText()
                showProfilePicture(answer.userThumbnail)
                showDisplayName(answer.userName)
                showSellerLabelWithCondition(answer.isSeller)
                showDate(answer.createTimeFormatted)
                showAnswer(answer.content, dynamicProductDetailListener)
                showNumberOfOtherAnswersWithCondition(questionID, totalAnswer, dynamicProductDetailListener, adapterPosition, type, name, itemCount)
            } else {
                showNoAnswersText()
            }
        }
    }

    private fun showQuestion(question: String) {
        itemView.productDetailDiscussionInquiry.apply {
            text = question
            HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        }
    }

    private fun showInquirerProfilePicture(inquirerThumbnail: String) {
        if(inquirerThumbnail.isNotEmpty()) {
            itemView.productDetailDiscussionInquirerProfilePicture.apply {
                loadImage(inquirerThumbnail)
                show()
            }
        } else {
            itemView.productDetailDiscussionInquirerProfilePicture.hide()
        }
    }

    private fun showInquirerName(inquirerName: String) {
        if(inquirerName.isNotEmpty()) {
            itemView.productDetailDiscussionInquirerName.apply{
                text = inquirerName
                show()
            }
        } else {
            itemView.productDetailDiscussionInquirerName.hide()
        }
    }

    private fun showInquiryDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.productDetailDiscussionInquiryDate.apply {
                text = addBulletPointToDate(date)
                show()
            }
        } else {
            itemView.productDetailDiscussionInquiryDate.hide()
        }
    }

    private fun showAnswer(answer: String, dynamicProductDetailListener: DynamicProductDetailListener) {
        if(answer.isNotEmpty()) {
            itemView.productDetailDiscussionRespondentAnswer.apply {
                isEnabled = true
                text = HtmlLinkHelper(context, answer).spannedString
                setCustomMovementMethod(fun(link: String) : Boolean {return dynamicProductDetailListener.onDiscussionLinkClicked(link)})
                val viewTreeObserver = productDetailDiscussionRespondentAnswer.viewTreeObserver
                val maxLines = resources.getInteger(R.integer.discussion_max_lines)
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val secondViewTreeObserver = productDetailDiscussionRespondentAnswer.viewTreeObserver
                        secondViewTreeObserver.removeOnGlobalLayoutListener(this)
                        if (lineCount > maxLines) {
                            val endOfLastLine = layout.getLineEnd(maxLines - 1)
                            val spannableStringBuilder = SpannableStringBuilder()
                            spannableStringBuilder.append(text.subSequence(0, endOfLastLine - resources.getInteger(R.integer.discussion_length_of_ellipsis))).append(resources.getString(R.string.product_detail_discussion_ellipsis))
                            text = spannableStringBuilder
                        }
                    }
                })
                show()
            }
        } else {
            itemView.productDetailDiscussionRespondentAnswer.hide()
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

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.productDetailDiscussionRespondentProfilePicture.apply {
                loadImage(userThumbNail)
                show()
            }
        } else {
            itemView.productDetailDiscussionRespondentProfilePicture.hide()
        }
    }

    private fun showDisplayName(userName: String) {
        if(userName.isNotEmpty()) {
            itemView.productDetailDiscussionRespondentDisplayName.apply{
                text = userName
                show()
            }
        } else {
            itemView.productDetailDiscussionRespondentDisplayName.hide()
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.productDetailDiscussionRespondentResponseDate.apply {
                text = addBulletPointToDate(date)
                show()
            }
        } else {
            itemView.productDetailDiscussionRespondentResponseDate.hide()
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.productDetailDiscussionRespondentSellerLabel.show()
            itemView.productDetailDiscussionRespondentDisplayName.hide()
        } else {
            itemView.productDetailDiscussionRespondentSellerLabel.hide()
        }
    }

    private fun addBulletPointToDate(date: String): String {
        return String.format(itemView.context.getString(R.string.product_detail_discussion_formatted_date), date)
    }

    private fun showNoAnswersText() {
        itemView.productDetailDiscussionNoAnswersText.show()
        hideOtherElements()
    }

    private fun hideNoAnswersText() {
        itemView.productDetailDiscussionNoAnswersText.hide()
    }

    private fun showNumberOfOtherAnswersWithCondition(questionId: String, answer: Int, dynamicProductDetailListener: DynamicProductDetailListener, adapterPosition: Int, type: String, name: String, itemCount: Int) {
        val answersToShow = answer - 1
        if(answersToShow > 0) {
            itemView.productDetailDiscussionSeeOtherAnswers.apply {
                text = itemView.context.getString(R.string.product_detail_discussion_total_answers, answersToShow)
                setOnClickListener {
                    dynamicProductDetailListener.goToTalkReply(questionId, ComponentTrackDataModel(type, name, adapterPosition), itemCount.toString())
                }
                show()
            }
        } else {
            itemView.productDetailDiscussionSeeOtherAnswers.hide()
        }
    }

    private fun hideOtherElements() {
        itemView.apply {
            productDetailDiscussionRespondentAnswer.hide()
            productDetailDiscussionRespondentProfilePicture.hide()
            productDetailDiscussionRespondentDisplayName.hide()
            productDetailDiscussionRespondentResponseDate.hide()
            productDetailDiscussionSeeOtherAnswers.hide()
            productDetailDiscussionRespondentSellerLabel.hide()
        }
    }
}