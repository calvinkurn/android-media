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
import com.tokopedia.product.detail.databinding.ItemDynamicDiscussionMostHelpfulQuestionAndAnswerBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener

class ProductDiscussionQuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemDynamicDiscussionMostHelpfulQuestionAndAnswerBinding.bind(view)

    fun bind(
        question: Question,
        productDetailListener: ProductDetailListener,
        type: String,
        name: String,
        adapterPosition: Int,
        itemCount: Int
    ) {
        with(question) {
            itemView.setOnClickListener {
                productDetailListener.goToTalkReply(
                    questionID,
                    ComponentTrackDataModel(type, name, adapterPosition),
                    itemCount.toString()
                )
            }
            showQuestion(content)
            showInquirerName(userName)
            showInquirerProfilePicture(userThumbnail)
            showInquiryDate(createTimeFormatted)
            if (totalAnswer > 0 && answer.answerID.isNotEmpty()) {
                hideNoAnswersText()
                showProfilePicture(answer.userThumbnail)
                showDisplayName(answer.userName)
                showSellerLabelWithCondition(answer.isSeller)
                showDate(answer.createTimeFormatted)
                showAnswer(answer.content)
                showNumberOfOtherAnswersWithCondition(
                    questionID,
                    totalAnswer,
                    productDetailListener,
                    adapterPosition,
                    type,
                    name,
                    itemCount
                )
            } else {
                showNoAnswersText()
            }
        }
    }

    private fun showQuestion(question: String) {
        binding.productDetailDiscussionInquiry.apply {
            text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
        }
    }

    private fun showInquirerProfilePicture(inquirerThumbnail: String) {
        if(inquirerThumbnail.isNotEmpty()) {
            binding.productDetailDiscussionInquirerProfilePicture.apply {
                loadIcon(inquirerThumbnail)
                show()
            }
        } else {
            binding.productDetailDiscussionInquirerProfilePicture.hide()
        }
    }

    private fun showInquirerName(inquirerName: String) {
        if(inquirerName.isNotEmpty()) {
            binding.productDetailDiscussionInquirerName.apply{
                text = inquirerName
                show()
            }
        } else {
            binding.productDetailDiscussionInquirerName.hide()
        }
    }

    private fun showInquiryDate(date: String) {
        if(date.isNotEmpty()) {
            binding.productDetailDiscussionInquiryDate.apply {
                text = date
                show()
            }
        } else {
            binding.productDetailDiscussionInquiryDate.hide()
        }
    }

    private fun showAnswer(answer: String) {
        if(answer.isNotEmpty()) {
            binding.productDetailDiscussionRespondentAnswer.apply {
                isEnabled = true
                text = HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("\n", " ")
                show()
            }
        } else {
            binding.productDetailDiscussionRespondentAnswer.hide()
        }
    }

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            binding.productDetailDiscussionRespondentProfilePicture.apply {
                loadIcon(userThumbNail)
                show()
            }
        } else {
            binding.productDetailDiscussionRespondentProfilePicture.hide()
        }
    }

    private fun showDisplayName(userName: String) {
        if(userName.isNotEmpty()) {
            binding.productDetailDiscussionRespondentDisplayName.apply{
                text = userName
                show()
            }
        } else {
            binding.productDetailDiscussionRespondentDisplayName.hide()
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            binding.productDetailDiscussionRespondentResponseDate.apply {
                text = date
                show()
            }
        } else {
            binding.productDetailDiscussionRespondentResponseDate.hide()
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            binding.productDetailDiscussionRespondentSellerLabel.show()
            binding.productDetailDiscussionRespondentDisplayName.hide()
        } else {
            binding.productDetailDiscussionRespondentSellerLabel.hide()
        }
    }

    private fun showNoAnswersText() {
        binding.productDetailDiscussionNoAnswersText.show()
        hideOtherElements()
    }

    private fun hideNoAnswersText() {
        binding.productDetailDiscussionNoAnswersText.hide()
    }

    private fun showNumberOfOtherAnswersWithCondition(
        questionId: String,
        answer: Int,
        productDetailListener: ProductDetailListener,
        adapterPosition: Int,
        type: String,
        name: String,
        itemCount: Int
    ) {
        val answersToShow = answer - 1
        if (answersToShow > 0) {
            binding.productDetailDiscussionSeeOtherAnswers.apply {
                text = itemView.context.getString(
                    R.string.product_detail_discussion_total_answers,
                    answersToShow
                )
                setOnClickListener {
                    productDetailListener.goToTalkReply(
                        questionId,
                        ComponentTrackDataModel(type, name, adapterPosition),
                        itemCount.toString()
                    )
                }
                show()
            }
        } else {
            binding.productDetailDiscussionSeeOtherAnswers.hide()
        }
    }

    private fun hideOtherElements() {
        binding.apply {
            productDetailDiscussionRespondentAnswer.hide()
            productDetailDiscussionRespondentProfilePicture.hide()
            productDetailDiscussionRespondentDisplayName.hide()
            productDetailDiscussionRespondentResponseDate.hide()
            productDetailDiscussionSeeOtherAnswers.hide()
            productDetailDiscussionRespondentSellerLabel.hide()
        }
    }
}
