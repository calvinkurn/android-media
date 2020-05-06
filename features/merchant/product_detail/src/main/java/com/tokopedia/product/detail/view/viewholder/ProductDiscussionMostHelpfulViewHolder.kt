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
import kotlinx.android.synthetic.main.partial_dynamic_discussion_local_load.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_empty_state.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_single_question.view.*

class ProductDiscussionMostHelpfulViewHolder(view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDiscussionMostHelpfulDataModel>(view) {

    companion object {
        private const val EMPTY_TALK_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/talk_pdp_empty.png"
        val LAYOUT = R.layout.item_dynamic_discussion_most_helpful
    }

    override fun bind(element: ProductDiscussionMostHelpfulDataModel) {
        with(element) {
            return when {
                questions == null -> {
                    showLocalLoad()
                    hideSingleQuestionLayout()
                    hideEmptyState()
                    hideShimmer()
                    hideMultipleQuestion()
                    hideTitle()
                }
                isShimmering -> {
                    showShimmer()
                    hideSingleQuestionLayout()
                    hideEmptyState()
                    hideLocalLoad()
                    hideMultipleQuestion()
                    hideTitle()
                }
                (totalQuestion < 1 && element.questions?.isEmpty() == true) -> {
                    showEmptyState()
                    hideSingleQuestionLayout()
                    hideLocalLoad()
                    hideShimmer()
                    hideTitle()
                    hideMultipleQuestion()
                }
                element.totalQuestion == 1 -> {
                    showTitle(element.totalQuestion)
                    showSingleQuestion(questions?.first())
                    hideEmptyState()
                    hideShimmer()
                    hideLocalLoad()
                    hideMultipleQuestion()
                }
                else -> {
                    showTitle(element.totalQuestion)
                    showMultipleQuestions(questions)
                    hideSingleQuestionLayout()
                    hideEmptyState()
                    hideShimmer()
                    hideLocalLoad()
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
            productDetailDiscussionEmptyImage.loadImage(EMPTY_TALK_IMAGE_URL)
        }
    }

    private fun showSingleQuestion(question: Question?) {
        question?.let {
            itemView.productDiscussionMostHelpfulSingleQuestionLayout.apply {
                visibility = View.VISIBLE
                with(it) {
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
    }

    private fun showMultipleQuestions(questions: List<Question>?) {
        questions?.let {
            val questionsAdapter = ProductDiscussionQuestionsAdapter(it, listener)
            itemView.productDiscussionMostHelpfulQuestions.apply {
                adapter = questionsAdapter
                visibility = View.VISIBLE
            }
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

    private fun showLocalLoad() {
        itemView.apply {
            productDiscussionLocalLoadLayout.visibility = View.VISIBLE
            productDetailDiscussionLocalLoad.apply {
                title?.text = getString(R.string.product_detail_discussion_local_load_title)
                description?.text = getString(R.string.product_detail_discussion_local_load_description)
                refreshBtn?.setOnClickListener {
                    listener.onDiscussionRefreshClicked()
                }
            }
        }
    }

    private fun hideMultipleQuestion() {
        itemView.productDiscussionMostHelpfulQuestions.visibility = View.GONE
    }

    private fun hideTitle() {
        itemView.apply {
            productDiscussionMostHelpfulTitle.visibility = View.GONE
            productDiscussionMostHelpfulSeeAll.visibility = View.GONE
        }
    }

    private fun showShimmer() {
        itemView.productDiscussionShimmerLayout.visibility = View.VISIBLE
    }

    private fun hideSingleQuestionLayout() {
        itemView.productDiscussionMostHelpfulSingleQuestionLayout.visibility = View.GONE
    }

    private fun hideEmptyState() {
        itemView.productDiscussionMostHelpfulEmptyLayout.visibility = View.GONE
    }

    private fun hideShimmer() {
        itemView.productDiscussionShimmerLayout.visibility = View.GONE
    }

    private fun hideLocalLoad() {
        itemView.productDiscussionLocalLoadLayout.visibility = View.GONE
    }

}