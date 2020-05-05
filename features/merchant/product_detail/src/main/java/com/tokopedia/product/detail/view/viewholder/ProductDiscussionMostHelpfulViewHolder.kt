package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.adapter.ProductDiscussionQuestionsAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion_most_helpful.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_empty_state.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_single_question.view.*

class ProductDiscussionMostHelpfulViewHolder(view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDiscussionMostHelpfulDataModel>(view) {

    companion object {
        const val EMPTY_TALK_URL = ""
        val LAYOUT = R.layout.item_dynamic_discussion_most_helpful
    }

    override fun bind(element: ProductDiscussionMostHelpfulDataModel) {
        with(element) {
            return when {
                element.totalQuestion < 1 -> {
                    showEmptyState()
                }
                element.totalQuestion == 1 -> {
                    showTitle(element.totalQuestion)
                    showSingleQuestion(questions.first())
                    hideOtherPartialLayouts()
                }
                else -> {
                    showTitle(element.totalQuestion)
                    showMultipleQuestions(questions)
                    hideOtherPartialLayouts()
                }
            }
        }
    }

    private fun showEmptyState() {
        itemView.productDiscussionMostHelpfulEmptyLayout.apply {
            visibility = View.VISIBLE
            productDetailDiscussionEmptyButton.setOnClickListener {
                listener.onDiscussionSendQuestionClicked()
            }
            productDetailDiscussionEmptyImage.loadImage(EMPTY_TALK_URL)
        }
    }

    private fun showSingleQuestion(question: Question) {
        itemView.productDiscussionMostHelpfulSingleQuestionLayout.apply {
            visibility = View.VISIBLE
            with(question) {
                productDetailDiscussionSingleQuestion.text = question.content
                productDetailDiscussionSingleQuestionChevron.setOnClickListener {
                    listener.goToTalkReading()
                }
                if(totalAnswer == 0) {
                    showNoAnswersText()
                    return
                }
                showProfilePicture(answer.userThumbnail, answer.userId)
                showDisplayName(answer.userName, answer.userId)
                showSellerLabelWithCondition(answer.isSeller)
                showDate(answer.createTimeFormatted)
                showAnswer(answer.content)
                showNumberOfAttachedProductsWithCondition(answer.attachedProductCount)
                showNumberOfOtherAnswersWithCondition(totalAnswer, questionID)
            }
        }
    }

    private fun showMultipleQuestions(questions: List<Question>) {
        val questionsAdapter = ProductDiscussionQuestionsAdapter(questions, listener)
        itemView.productDiscussionMostHelpfulQuestions.apply {
            adapter = questionsAdapter
            visibility = View.VISIBLE
        }
    }

    private fun showTitle(totalQuestion: Int) {
        itemView.apply {
            productDiscussionMostHelpfulTitle.text = String.format(getString(R.string.product_detail_discussion_title), totalQuestion)
            productDiscussionMostHelpfulTitle.visibility = View.VISIBLE
            productDiscussionMostHelpfulSeeAll.setOnClickListener {
                listener.goToTalkReading()
            }
            productDiscussionMostHelpfulSeeAll.visibility = View.VISIBLE
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionProfilePicture.apply {
                loadImage(userThumbNail)
                setOnClickListener {
                    listener.onUserDetailsClicked(userId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDisplayName(userName: String, userId: String) {
        if(userName.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionDisplayName.apply{
                text = userName
                setOnClickListener {
                    listener.onUserDetailsClicked(userId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showAnswer(answer: String) {
        if(answer.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionMessage.apply {
                text = answer
                setOnClickListener {
                    listener.goToTalkReading()
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionDate.apply {
                text = getString(R.string.product_detail_discussion_formatted_date, date)
                visibility = View.VISIBLE
            }
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.productDetailDiscussionSingleQuestionSellerLabel.visibility = View.VISIBLE
        }
    }

    private fun showNumberOfAttachedProductsWithCondition(attachedProducts: Int) {
        if(attachedProducts > 0) {
            itemView.apply {
                productDetailDiscussionSingleQuestionAttachedProductIcon.visibility = View.VISIBLE
                productDetailDiscussionSingleQuestionAttachedProductCount.text = String.format(context.getString(R.string.product_detail_discussion_attached_products), attachedProducts)
                productDetailDiscussionSingleQuestionAttachedProductCount.visibility = View.VISIBLE
            }
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, questionId: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemView.productDetailDiscussionSingleQuestionAttachedSeeOtherAnswers.apply {
                text = String.format(context.getString(R.string.product_detail_discussion_other_answers), answersToShow)
                setOnClickListener {
                    listener.goToTalkReading()
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showNoAnswersText() {
        itemView.productDetailDiscussionSingleQuestionAttachedNoAnswers.visibility = View.VISIBLE
        hideOtherElements()
    }

    private fun hideOtherElements() {
        itemView.apply {
            productDetailDiscussionSingleQuestionAttachedProductCount.visibility = View.GONE
            productDetailDiscussionSingleQuestionAttachedProductIcon.visibility = View.GONE
            productDetailDiscussionSingleQuestionMessage.visibility = View.GONE
            productDetailDiscussionSingleQuestionProfilePicture.visibility = View.GONE
            productDetailDiscussionSingleQuestionDisplayName.visibility = View.GONE
            productDetailDiscussionSingleQuestionDate.visibility = View.GONE
            productDetailDiscussionSingleQuestionAttachedSeeOtherAnswers.visibility = View.GONE
            productDetailDiscussionSingleQuestionSellerLabel.visibility = View.GONE
        }
    }

    private fun hideOtherPartialLayouts() {
        itemView.apply {
            productDiscussionMostHelpfulSingleQuestionLayout.visibility = View.GONE
            productDiscussionMostHelpfulEmptyLayout.visibility = View.GONE
        }
    }

}