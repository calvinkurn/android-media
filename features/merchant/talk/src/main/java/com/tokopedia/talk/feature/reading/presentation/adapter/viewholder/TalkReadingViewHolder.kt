package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnThreadClickListener
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reading.view.*

class TalkReadingViewHolder(view: View, private val onThreadClickListener: OnThreadClickListener) : AbstractViewHolder<TalkReadingUiModel>(view) {

    companion object {
        const val BULLET_POINT = "\u2022 %s"
        const val ATTACHED_PRODUCT = "%d produk"
        const val OTHER_ANSWERS = "Lihat %d Jawaban Lainnya"
        val LAYOUT = R.layout.item_talk_reading
    }

    override fun bind(element: TalkReadingUiModel) {
        itemView.apply {
            readingQuestionTitle.text = element.question.content
            readingQuestionTitle.setOnClickListener {
                onThreadClickListener.onThreadClicked(element.question.questionID)
            }
            if(element.question.totalAnswer > 0 && element.question.answer.answerID.isNotEmpty()) {
                element.question.apply {
                    showProfilePicture(answer.userThumbnail)
                    showSellerLabelWithCondition(answer.isSeller)
                    showDisplayName(answer.userName)
                    showDate(answer.createTimeFormatted)
                    showAnswer(answer.content, questionID)
                    showNumberOfLikesWithCondition(answer.likeCount)
                    showNumberOfAttachedProductsWithCondition(answer.attachedProductCount)
                    showNumberOfOtherAnswersWithCondition(totalAnswer)
                }
                return
            }
            showNoAnswersText()
        }
    }

    private fun showNoAnswersText() {
        itemView.readingNoAnswersText.visibility = View.VISIBLE
    }

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.readingProfilePicture.apply {
                loadImage(userThumbNail)
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDisplayName(userName: String) {
        if(userName.isNotEmpty()) {
            itemView.readingDisplayName.apply{
                text = userName
                visibility = View.VISIBLE
            }
        }
    }

    private fun showAnswer(answer: String, questionId: String) {
        if(answer.isNotEmpty()) {
            itemView.apply {
                readingMessage.text = answer
                readingMessage.setOnClickListener {
                    onThreadClickListener.onThreadClicked(questionId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.apply {
                readingDate.text = addBulletPointToDate(date)
                visibility = View.VISIBLE
            }
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.readingSellerLabel.visibility = View.VISIBLE
        }
    }

    private fun addBulletPointToDate(date: String): String {
        return String.format(BULLET_POINT, date)
    }

    private fun showNumberOfLikesWithCondition(likeCount: Int) {
        if(likeCount > 0) {
            itemView.likeCount.text = likeCount.toString()
        }
    }

    private fun showNumberOfAttachedProductsWithCondition(attachedProductCount: Int) {
        if(attachedProductCount > 0) {
            itemView.attachedProductIcon.visibility = View.VISIBLE
            itemView.attachedProductCount.text = String.format(ATTACHED_PRODUCT, attachedProductCount)
            itemView.attachedProductCount.visibility = View.VISIBLE
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int) {
        if(otherAnswers > 0) {
            itemView.seeOtherAnswers.text = String.format(OTHER_ANSWERS, (otherAnswers - 1))
            itemView.visibility = View.VISIBLE
        }
    }


}