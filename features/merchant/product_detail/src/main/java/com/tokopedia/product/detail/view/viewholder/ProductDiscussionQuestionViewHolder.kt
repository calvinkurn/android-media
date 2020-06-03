package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion_most_helpful_question_and_answer.view.*

class ProductDiscussionQuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(question: Question, dynamicProductDetailListener: DynamicProductDetailListener, type: String, name: String, adapterPosition: Int, itemCount: Int) {
        with(question) {
            showQuestion(dynamicProductDetailListener, content, questionID, type, name, adapterPosition, itemCount)
            setChevronClickListener(dynamicProductDetailListener, questionID, type, name, adapterPosition, itemCount)
            if(totalAnswer == 0) {
                showNoAnswer()
                return
            }
            showAnswerThumbnail(answer.userThumbnail)
            showAnswersCount(questionID, totalAnswer, dynamicProductDetailListener, adapterPosition, type, name, itemCount)
        }
    }

    private fun showQuestion(dynamicProductDetailListener: DynamicProductDetailListener, question: String, questionId: String, type: String, name: String, adapterPosition: Int, itemCount: Int) {
        itemView.productDetailDiscussionQuestion.apply {
            text = question
            setOnClickListener {
                dynamicProductDetailListener.goToTalkReply(questionId, ComponentTrackDataModel(type, name, adapterPosition), itemCount.toString())
            }
        }
    }

    private fun setChevronClickListener(dynamicProductDetailListener: DynamicProductDetailListener, questionId: String, type: String, name: String, adapterPosition: Int, itemCount: Int) {
        itemView.productDetailDiscussionQuestionChevron.setOnClickListener {
            dynamicProductDetailListener.goToTalkReply(questionId, ComponentTrackDataModel(type, name, adapterPosition), itemCount.toString())
        }
    }

    private fun showNoAnswer() {
        itemView.productDetailDiscussionTotalAnswer.apply {
            text = context.getString(R.string.product_detail_discussion_no_answers)
            setTextColor(context.getResColor(R.color.Neutral_N700_32))
            show()
        }
    }

    private fun showAnswersCount(questionId: String, answer: Int, dynamicProductDetailListener: DynamicProductDetailListener, adapterPosition: Int, type: String, name: String, itemCount: Int) {
        itemView.productDetailDiscussionTotalAnswer.apply {
            text = itemView.context.getString(R.string.product_detail_discussion_total_answers, answer)
            setOnClickListener {
                dynamicProductDetailListener.goToTalkReply(questionId, ComponentTrackDataModel(type, name, adapterPosition), itemCount.toString())
            }
            setTextColor(context.getResColor(R.color.Neutral_N700_68))
            show()
        }
    }

    private fun showAnswerThumbnail(userThumbnail: String) {
        if(userThumbnail.isNotEmpty()) {
            itemView.productDetailDiscussionProfilePicture.apply {
                loadImage(userThumbnail)
                show()
            }
        }
    }
}