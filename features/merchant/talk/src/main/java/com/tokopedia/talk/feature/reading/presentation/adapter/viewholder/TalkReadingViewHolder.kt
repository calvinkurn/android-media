package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
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
            showQuestionWithCondition(state.isMasked, content, maskedContent, questionID)
            if(totalAnswer > 0 && answer.answerID.isNotEmpty()) {
                hideNoAnswersText()
                showProfilePicture(answer.userThumbnail, answer.userId)
                showDisplayName(answer.userName, answer.userId)
                showSellerLabelWithCondition(answer.isSeller)
                showDate(answer.createTimeFormatted)
                if(answer.state.isMasked) {
                    showMaskedAnswer(answer.maskedContent, questionID)
                    return
                }
                showAnswer(answer.content, questionID)
                showNumberOfLikesWithCondition(answer.likeCount, answer.state.allowLike)
                showNumberOfAttachedProductsWithCondition(answer.attachedProductCount)
                showNumberOfOtherAnswersWithCondition(totalAnswer, questionID)
            } else {
                showNoAnswersText()
            }
        }
    }

    private fun showQuestionWithCondition(isMasked: Boolean, content: String, maskedContent: String, questionId: String) {
        itemView.readingQuestionTitle.apply {
            text = if(isMasked) {
                isEnabled = false
                maskedContent
            } else {
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                content
            }
        }
    }

    private fun showMaskedAnswer(maskedContent: String, questionId: String) {
        itemView.readingMessage.apply {
            text = maskedContent
            isEnabled = false
            visibility = View.VISIBLE
        }
    }

    private fun showNoAnswersText() {
        itemView.readingNoAnswersText.visibility = View.VISIBLE
        hideOtherElements()
    }

    private fun hideNoAnswersText() {
        itemView.apply {
            readingNoAnswersText.visibility = View.GONE
            likeIcon.visibility = View.VISIBLE
            likeCount.visibility = View.VISIBLE
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.readingProfilePicture.apply {
                loadImage(userThumbNail)
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDisplayName(userName: String, userId: String) {
        if(userName.isNotEmpty()) {
            itemView.readingDisplayName.apply{
                text = userName
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showAnswer(answer: String, questionId: String) {
        if(answer.isNotEmpty()) {
            itemView.readingMessage.apply {
                text = answer
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.readingDate.apply {
                text = addBulletPointToDate(date)
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
        return String.format(itemView.context.getString(R.string.talk_formatted_date), date)
    }

    private fun showNumberOfLikesWithCondition(likeCount: Int, allowLike: Boolean) {
        if(!allowLike) {
            return
        }
        if(likeCount > 0) {
            itemView.likeCount.apply {
                text = likeCount.toString()
                visibility = View.VISIBLE
            }
        }
    }

    private fun showNumberOfAttachedProductsWithCondition(attachedProducts: Int) {
        if(attachedProducts > 0) {
            itemView.apply {
                attachedProductIcon.visibility = View.VISIBLE
                attachedProductCount.text = String.format(context.getString(R.string.reading_attached_product), attachedProducts)
                attachedProductCount.visibility = View.VISIBLE
            }
        }
    }

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, questionId: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemView.seeOtherAnswers.apply {
                text = String.format(context.getString(R.string.reading_other_answers), answersToShow)
                setOnClickListener {
                    threadListener.onThreadClicked(questionId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun hideOtherElements() {
        itemView.apply {
            likeIcon.visibility = View.GONE
            likeCount.visibility = View.GONE
            attachedProductCount.visibility = View.GONE
            attachedProductIcon.visibility = View.GONE
            readingMessage.visibility = View.GONE
            readingProfilePicture.visibility = View.GONE
            readingDisplayName.visibility = View.GONE
            readingDate.visibility = View.GONE
            seeOtherAnswers.visibility = View.GONE
            readingSellerLabel.visibility = View.GONE
        }
    }


}