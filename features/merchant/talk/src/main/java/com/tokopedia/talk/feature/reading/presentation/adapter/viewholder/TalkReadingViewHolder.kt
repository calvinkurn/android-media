package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.ItemTalkReadingBinding
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener
import com.tokopedia.unifycomponents.Label

class TalkReadingViewHolder(view: View, private val threadListener: ThreadListener) : AbstractViewHolder<TalkReadingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reading
    }

    private val itemTalkReadingBinding: ItemTalkReadingBinding = ItemTalkReadingBinding.bind(view)

    override fun bind(element: TalkReadingUiModel) {
        element.question.apply {
            itemView.setOnClickListener { threadListener.onThreadClicked(questionID) }
            showInquirerName(userName, state.isYours)
            showInquirerProfilePicture(userThumbnail)
            showInquiryDate(createTimeFormatted)
            showQuestionWithCondition(state.isMasked, content, maskedContent, questionID)
            if(totalAnswer > 0 && answer.answerID.isNotEmpty()) {
                hideNoAnswersText()
                showProfilePicture(answer.userThumbnail)
                showDisplayName(answer.userName)
                showLabelWithCondition(answer.isSeller, answer.state.isYours)
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
            itemTalkReadingBinding.readingInquirerProfilePicture.apply {
                loadImage(inquirerThumbnail)
                show()
            }
        } else {
            itemTalkReadingBinding.readingInquirerProfilePicture.hide()
        }
    }

    private fun showInquirerName(inquirerName: String, isMyQuestion: Boolean) {
        if(isMyQuestion) {
            itemTalkReadingBinding.readingInquirerYouLabel.show()
        } else {
            itemTalkReadingBinding.readingInquirerYouLabel.hide()
        }
        if(inquirerName.isNotEmpty()) {
            itemTalkReadingBinding.readingInquirerName.apply{
                text = inquirerName
                show()
            }
        } else {
            itemTalkReadingBinding.readingInquirerName.hide()
        }
    }

    private fun showInquiryDate(date: String) {
        if(date.isNotEmpty()) {
            itemTalkReadingBinding.readingInquiryDate.apply {
                text = date
                show()
            }
        } else {
            itemTalkReadingBinding.readingInquiryDate.hide()
        }
    }

    private fun showQuestionWithCondition(isMasked: Boolean, content: String, maskedContent: String, questionId: String) {
        itemTalkReadingBinding.readingInquiry.apply {
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
        itemTalkReadingBinding.apply {
            readingRespondentAnswer.apply {
                text = maskedContent
                isEnabled = false
                show()
            }
        }
    }

    private fun showNoAnswersText() {
        itemTalkReadingBinding.readingNoAnswersText.show()
        hideOtherElements()
    }

    private fun hideNoAnswersText() {
        itemTalkReadingBinding.apply {
            readingNoAnswersText.hide()
        }
    }

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            itemTalkReadingBinding.readingRespondentProfilePicture.apply {
                loadImage(userThumbNail)
                show()
            }
        } else {
            itemTalkReadingBinding.readingRespondentProfilePicture.hide()
        }
    }

    private fun showDisplayName(userName: String) {
        if(userName.isNotEmpty()) {
            itemTalkReadingBinding.readingRespondentDisplayName.apply{
                text = userName
                show()
            }
        } else {
            itemTalkReadingBinding.readingRespondentDisplayName.hide()
        }
    }

    private fun showAnswer(answer: String, questionId: String) {
        if(answer.isNotEmpty()) {
            itemTalkReadingBinding.readingRespondentAnswer.apply {
                isEnabled = true
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                text = HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
                show()
            }
        } else {
            itemTalkReadingBinding.readingRespondentAnswer.hide()
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemTalkReadingBinding.readingRespondentResponseDate.apply {
                text = date
                show()
            }
        } else {
            itemTalkReadingBinding.readingRespondentResponseDate.hide()
        }
    }

    private fun showLabelWithCondition(isSeller: Boolean, isYours: Boolean) {
        itemTalkReadingBinding.apply {
            when {
                isSeller -> {
                    readingRespondentSellerLabel.apply {
                        text = context.getString(R.string.reading_seller_label)
                        setLabelType(Label.GENERAL_LIGHT_GREEN)
                        show()
                    }
                    readingRespondentDisplayName.hide()
                }
                isYours -> {
                    readingRespondentSellerLabel.apply {
                        text = context.getString(R.string.reading_your_question_label)
                        setLabelType(Label.GENERAL_LIGHT_GREY)
                        show()
                    }
                }
                else -> {
                    readingRespondentSellerLabel.hide()
                }
            }
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, questionId: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemTalkReadingBinding.readingSeeOtherAnswers.apply {
                text = String.format(context.getString(R.string.reading_other_answers), answersToShow)
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                show()
            }
        } else {
            itemTalkReadingBinding.readingSeeOtherAnswers.hide()
        }
    }

    private fun hideOtherElements() {
        itemTalkReadingBinding.apply {
            readingRespondentAnswer.hide()
            readingRespondentProfilePicture.hide()
            readingRespondentDisplayName.hide()
            readingRespondentResponseDate.hide()
            readingSeeOtherAnswers.hide()
            readingRespondentSellerLabel.hide()
        }
    }


}