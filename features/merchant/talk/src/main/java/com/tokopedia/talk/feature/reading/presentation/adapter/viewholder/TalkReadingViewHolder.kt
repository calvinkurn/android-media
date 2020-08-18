package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reading.view.*

class TalkReadingViewHolder(view: View, private val threadListener: ThreadListener) : AbstractViewHolder<TalkReadingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reading
    }

    override fun bind(element: TalkReadingUiModel) {
        element.question.apply {
            itemView.setOnClickListener { threadListener.onThreadClicked(questionID) }
            showInquirerName(userName, element.isMyQuestion)
            showInquirerProfilePicture(userThumbnail)
            showInquiryDate(createTimeFormatted)
            showQuestionWithCondition(state.isMasked, content, maskedContent, questionID)
            if(totalAnswer > 0 && answer.answerID.isNotEmpty()) {
                hideNoAnswersText()
                showProfilePicture(answer.userThumbnail)
                showDisplayName(answer.userName)
                showSellerLabelWithCondition(answer.isSeller)
                showDate(answer.createTimeFormatted)
                if(answer.state.isMasked) {
                    showMaskedAnswer(answer.maskedContent, questionID)
                    return
                }
                showAnswer(answer.content, questionID)
                showNumberOfOtherAnswersWithCondition(totalAnswer, questionID)
            } else {
                showNoAnswersText()
            }
        }
    }

    private fun showInquirerProfilePicture(inquirerThumbnail: String) {
        if(inquirerThumbnail.isNotEmpty()) {
            itemView.readingInquirerProfilePicture.apply {
                loadImage(inquirerThumbnail)
                show()
            }
        } else {
            itemView.readingInquirerProfilePicture.hide()
        }
    }

    private fun showInquirerName(inquirerName: String, isMyQuestion: Boolean) {
        if(isMyQuestion) {
            itemView.apply {
                readingInquirerYouLabel.show()
                readingInquirerName.hide()
            }
            return
        }
        if(inquirerName.isNotEmpty()) {
            itemView.readingInquirerName.apply{
                text = inquirerName
                show()
            }
            itemView.readingInquirerYouLabel.hide()
        } else {
            itemView.readingInquirerName.hide()
        }
    }

    private fun showInquiryDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.readingInquiryDate.apply {
                text = date
                show()
            }
        } else {
            itemView.readingInquiryDate.hide()
        }
    }

    private fun showQuestionWithCondition(isMasked: Boolean, content: String, maskedContent: String, questionId: String) {
        itemView.readingInquiry.apply {
            text = if(isMasked) {
                isEnabled = false
                maskedContent
            } else {
                isEnabled = true
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
            }
        }
    }

    private fun showMaskedAnswer(maskedContent: String, questionId: String) {
        itemView.apply {
            readingRespondentAnswer.apply {
                text = maskedContent
                isEnabled = false
                show()
            }
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

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.readingRespondentProfilePicture.apply {
                loadImage(userThumbNail)
                show()
            }
        } else {
            itemView.readingRespondentProfilePicture.hide()
        }
    }

    private fun showDisplayName(userName: String) {
        if(userName.isNotEmpty()) {
            itemView.readingRespondentDisplayName.apply{
                text = userName
                show()
            }
        } else {
            itemView.readingRespondentDisplayName.hide()
        }
    }

    private fun showAnswer(answer: String, questionId: String) {
        if(answer.isNotEmpty()) {
            itemView.readingRespondentAnswer.apply {
                isEnabled = true
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                text = HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
                show()
            }
        } else {
            itemView.readingRespondentAnswer.hide()
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.readingRespondentResponseDate.apply {
                text = date
                show()
            }
        } else {
            itemView.readingRespondentResponseDate.hide()
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.readingRespondentSellerLabel.show()
            itemView.readingRespondentDisplayName.hide()
        } else {
            itemView.readingRespondentSellerLabel.hide()
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, questionId: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemView.readingSeeOtherAnswers.apply {
                text = String.format(context.getString(R.string.reading_other_answers), answersToShow)
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                show()
            }
        } else {
            itemView.readingSeeOtherAnswers.hide()
        }
    }

    private fun hideOtherElements() {
        itemView.apply {
            readingRespondentAnswer.hide()
            readingRespondentProfilePicture.hide()
            readingRespondentDisplayName.hide()
            readingRespondentResponseDate.hide()
            readingSeeOtherAnswers.hide()
            readingRespondentSellerLabel.hide()
        }
    }


}