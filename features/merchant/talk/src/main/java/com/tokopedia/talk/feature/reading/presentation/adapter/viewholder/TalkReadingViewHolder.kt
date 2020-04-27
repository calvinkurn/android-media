package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reading.view.*

class TalkReadingViewHolder(view: View, private val threadListener: ThreadListener) : AbstractViewHolder<TalkReadingUiModel>(view) {

    companion object {
        const val BULLET_POINT = "\u2022 %s"
        const val ATTACHED_PRODUCT = "%d produk"
        const val OTHER_ANSWERS = "Lihat %d Jawaban Lainnya"
        const val MAX_LINES = 2
        val LAYOUT = R.layout.item_talk_reading
    }

    override fun bind(element: TalkReadingUiModel) {
        itemView.apply {
            readingQuestionTitle.text = element.question.content
            readingQuestionTitle.setOnClickListener {
                threadListener.onThreadClicked(element.question.questionID)
            }
            if(element.question.totalAnswer > 0 && element.question.answer.answerID.isNotEmpty()) {
                element.question.apply {
                    showProfilePicture(answer.userThumbnail, answer.userId)
                    showSellerLabelWithCondition(answer.isSeller)
                    showDisplayName(answer.userName, answer.userId)
                    showDate(answer.createTimeFormatted)
                    showAnswer(answer.content, questionID)
                    showNumberOfLikesWithCondition(answer.likeCount)
                    showNumberOfAttachedProductsWithCondition(answer.attachedProductCount)
                    showNumberOfOtherAnswersWithCondition(totalAnswer, questionID)
                    hideNoAnswersText()
                }
            } else {
                showNoAnswersText()
            }
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
                setSingleLine(false)
                ellipsize = TextUtils.TruncateAt.END
                setLines(MAX_LINES)
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

    private fun showNumberOfOtherAnswersWithCondition(otherAnswers: Int, questionId: String) {
        val answersToShow = otherAnswers - 1
        if(answersToShow > 0) {
            itemView.seeOtherAnswers.apply {
                text = String.format(OTHER_ANSWERS, answersToShow)
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