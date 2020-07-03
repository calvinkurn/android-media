package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.adapter.ProductDiscussionQuestionsAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion_most_helpful.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_local_load.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_empty_state.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_single_question.view.*

class ProductDiscussionMostHelpfulViewHolder(view: View,
                                             private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductDiscussionMostHelpfulDataModel>(view) {

    companion object {
        const val SINGLE_QUESTION_TRACKING = "1"
        private const val EMPTY_TALK_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/talk_product_detail_empty.png"
        val LAYOUT = R.layout.item_dynamic_discussion_most_helpful
    }

    override fun bind(element: ProductDiscussionMostHelpfulDataModel) {
        with(element) {
            return when {
                questions == null && !isShimmering-> {
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
                    showEmptyState(type, name)
                    hideSingleQuestionLayout()
                    hideLocalLoad()
                    hideShimmer()
                    hideTitle()
                    hideMultipleQuestion()
                }
                questions?.size == 1 -> {
                    showTitle(totalQuestion, type, name, SINGLE_QUESTION_TRACKING)
                    showSingleQuestion(questions?.first(), type, name)
                    hideEmptyState()
                    hideShimmer()
                    hideLocalLoad()
                    hideMultipleQuestion()
                }
                else -> {
                    showTitle(totalQuestion, type, name, questions?.size.toString())
                    showMultipleQuestions(questions, type, name)
                    hideSingleQuestionLayout()
                    hideEmptyState()
                    hideShimmer()
                    hideLocalLoad()
                }
            }
        }
    }

    private fun showEmptyState(type: String, name: String) {
        itemView.productDiscussionMostHelpfulEmptyLayout.apply {
            show()
            productDetailDiscussionEmptyButton.setOnClickListener {
                listener.onDiscussionSendQuestionClicked(ComponentTrackDataModel(type, name, adapterPosition + 1))
            }
            productDetailDiscussionEmptyImage.loadImage(EMPTY_TALK_IMAGE_URL)
        }
    }

    private fun showSingleQuestion(question: Question?, type: String, name: String) {
        question?.let { questionData ->
            itemView.productDiscussionMostHelpfulSingleQuestionLayout.apply {
                show()
                with(questionData) {
                    productDetailDiscussionSingleQuestion.apply {
                        text = question.content
                        setOnClickListener {
                            listener.goToTalkReply(questionID, ComponentTrackDataModel(type, name, adapterPosition + 1), SINGLE_QUESTION_TRACKING)
                        }
                    }
                    productDetailDiscussionSingleQuestionChevron.setOnClickListener {
                        listener.goToTalkReply(questionID, ComponentTrackDataModel(type, name, adapterPosition + 1), SINGLE_QUESTION_TRACKING)
                    }
                    if(totalAnswer == 0) {
                        showNoAnswersText()
                        return
                    }
                    showProfilePicture(answer.userThumbnail, answer.userId)
                    showDisplayName(answer.userName, answer.userId)
                    showSellerLabelWithCondition(answer.isSeller)
                    showDate(answer.createTimeFormatted)
                    showAnswer(answer.content, questionID, type, name)
                    showNumberOfAttachedProductsWithCondition(answer.attachedProductCount)
                    showNumberOfOtherAnswersWithCondition(totalAnswer, type, name, SINGLE_QUESTION_TRACKING)
                }
            }
        }
    }

    private fun showMultipleQuestions(questions: List<Question>?, type: String, name: String) {
        questions?.let {
            val questionsAdapter = ProductDiscussionQuestionsAdapter(it, listener, type, name, adapterPosition + 1)
            itemView.productDiscussionMostHelpfulQuestions.apply {
                adapter = questionsAdapter
                show()
            }
        }
    }

    private fun showTitle(totalQuestion: Int, type: String, name: String, numberOfThreadsShown: String) {
        itemView.apply {
            productDiscussionMostHelpfulTitle.text = String.format(getString(R.string.product_detail_discussion_title), totalQuestion)
            productDiscussionMostHelpfulTitle.show()
            productDiscussionMostHelpfulSeeAll.setOnClickListener {
                listener.goToTalkReading(ComponentTrackDataModel(type, name, adapterPosition + 1), numberOfThreadsShown)
            }
            productDiscussionMostHelpfulSeeAll.show()
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionProfilePicture.apply {
                loadImage(userThumbNail)
                setOnClickListener {
                    listener.onUserDetailsClicked(userId)
                }
                show()
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
                show()
            }
        }
    }

    private fun showAnswer(answer: String, questionId: String, type: String, name: String) {
        if(answer.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionMessage.apply {
                text = answer
                setOnClickListener {
                    listener.goToTalkReply(questionId, ComponentTrackDataModel(type, name, adapterPosition + 1), SINGLE_QUESTION_TRACKING)
                }
                show()
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.productDetailDiscussionSingleQuestionDate.apply {
                text = getString(R.string.product_detail_discussion_formatted_date, date)
                show()
            }
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.productDetailDiscussionSingleQuestionSellerLabel.show()
        }
    }

    private fun showNumberOfAttachedProductsWithCondition(attachedProducts: Int) {
        if(attachedProducts > 0) {
            itemView.apply {
                productDetailDiscussionSingleQuestionAttachedProductIcon.show()
                productDetailDiscussionSingleQuestionAttachedProductCount.text = String.format(context.getString(R.string.product_detail_discussion_attached_products), attachedProducts)
                productDetailDiscussionSingleQuestionAttachedProductCount.show()
            }
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, type: String, name: String, numberOfThreadsShown: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemView.productDetailDiscussionSingleQuestionAttachedSeeOtherAnswers?.apply {
                text = String.format(context.getString(R.string.product_detail_discussion_other_answers), answersToShow)
                setOnClickListener {
                    listener.goToTalkReading(ComponentTrackDataModel(type, name, adapterPosition + 1), numberOfThreadsShown)
                }
                show()
            }
        }
    }

    private fun showNoAnswersText() {
        itemView.productDetailDiscussionSingleQuestionAttachedNoAnswers.show()
        hideOtherElements()
    }

    private fun hideOtherElements() {
        itemView.apply {
            productDetailDiscussionSingleQuestionAttachedProductCount.hide()
            productDetailDiscussionSingleQuestionAttachedProductIcon.hide()
            productDetailDiscussionSingleQuestionMessage.hide()
            productDetailDiscussionSingleQuestionProfilePicture.hide()
            productDetailDiscussionSingleQuestionDisplayName.hide()
            productDetailDiscussionSingleQuestionDate.hide()
            productDetailDiscussionSingleQuestionAttachedSeeOtherAnswers.hide()
            productDetailDiscussionSingleQuestionSellerLabel.hide()
        }
    }

    private fun showLocalLoad() {
        itemView.apply {
            productDiscussionLocalLoadLayout.show()
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
        itemView.productDiscussionMostHelpfulQuestions.hide()
    }

    private fun hideTitle() {
        itemView.apply {
            productDiscussionMostHelpfulTitle.hide()
            productDiscussionMostHelpfulSeeAll.hide()
        }
    }

    private fun showShimmer() {
        itemView.productDiscussionShimmerLayout.show()
    }

    private fun hideSingleQuestionLayout() {
        itemView.productDiscussionMostHelpfulSingleQuestionLayout.hide()
    }

    private fun hideEmptyState() {
        itemView.productDiscussionMostHelpfulEmptyLayout.hide()
    }

    private fun hideShimmer() {
        itemView.productDiscussionShimmerLayout.hide()
    }

    private fun hideLocalLoad() {
        itemView.productDiscussionLocalLoadLayout.hide()
    }

}