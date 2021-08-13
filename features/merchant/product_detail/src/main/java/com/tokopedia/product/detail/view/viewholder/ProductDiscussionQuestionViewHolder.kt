package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
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
                showAnswer(answer.content)
                showNumberOfOtherAnswersWithCondition(questionID, totalAnswer, dynamicProductDetailListener, adapterPosition, type, name, itemCount)
            } else {
                showNoAnswersText()
            }
        }
    }

    private fun showQuestion(question: String) {
        itemView.productDetailDiscussionInquiry.apply {
            text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
        }
    }

    private fun showInquirerProfilePicture(inquirerThumbnail: String) {
        if(inquirerThumbnail.isNotEmpty()) {
            itemView.productDetailDiscussionInquirerProfilePicture.apply {
                loadIcon(inquirerThumbnail)
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
                text = date
                show()
            }
        } else {
            itemView.productDetailDiscussionInquiryDate.hide()
        }
    }

    private fun showAnswer(answer: String) {
        if(answer.isNotEmpty()) {
            itemView.productDetailDiscussionRespondentAnswer.apply {
                isEnabled = true
                text = HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
                show()
            }
        } else {
            itemView.productDetailDiscussionRespondentAnswer.hide()
        }
    }

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.productDetailDiscussionRespondentProfilePicture.apply {
                loadIcon(userThumbNail)
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
                text = date
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