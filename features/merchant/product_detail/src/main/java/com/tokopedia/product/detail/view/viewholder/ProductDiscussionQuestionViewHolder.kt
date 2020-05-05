package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion_most_helpful_question_and_answer.view.*

class ProductDiscussionQuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(question: Question, dynamicProductDetailListener: DynamicProductDetailListener) {
        with(question) {
            showQuestion(content)
            setChevronClickListener(dynamicProductDetailListener)
            if(totalAnswer == 0) {
                showNoAnswer()
                return
            }
            showAnswerThumbnail(answer.userThumbnail)
            showAnswersCount(totalAnswer)
        }
    }

    private fun showQuestion(question: String) {
        itemView.productDetailDiscussionQuestion.text = question
    }

    private fun setChevronClickListener(dynamicProductDetailListener: DynamicProductDetailListener) {
        itemView.productDetailDiscussionQuestionChevron.setOnClickListener {
            dynamicProductDetailListener.goToTalkReading()
        }
    }

    private fun showNoAnswer() {
        itemView.productDetailDiscussionQuestionNoAnswers.visibility = View.VISIBLE
    }

    private fun showAnswersCount(answer: Int) {
        itemView.productDetailDiscussionTotalAnswer.apply {
            text = itemView.context.getString(R.string.product_detail_discussion_total_answers, answer)
            visibility = View.VISIBLE

        }
    }

    private fun showAnswerThumbnail(userThumbnail: String) {
        itemView.productDetailDiscussionProfilePicture.apply {
            loadImage(userThumbnail)
            visibility = View.VISIBLE
        }
    }
}