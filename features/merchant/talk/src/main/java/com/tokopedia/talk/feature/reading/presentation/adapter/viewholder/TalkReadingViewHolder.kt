package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reading.view.*

class TalkReadingViewHolder(view: View) : AbstractViewHolder<TalkReadingUiModel>(view) {

    companion object {
        const val BULLET_POINT = "\u2022 %s"
        const val ATTACHED_PRODUCT = "%d produk"
        const val OTHER_ANSWERS = "Lihat %d Jawaban Lainnya"
        val LAYOUT = R.layout.item_talk_reading
    }

    override fun bind(element: TalkReadingUiModel) {
        itemView.apply {
            readingQuestionTitle.text = element.question.content
            readingProfilePicture.loadImage(element.question.answer.userThumbnail)
            showSellerLabelWithCondition(element.question.answer.isSeller)
            readingDisplayName.text = element.question.answer.userName
            readingDate.text = addBulletPointToDate(element.question.answer.createTimeFormatted)
            readingMessage.text = element.question.answer.content
            showNumberOfLikesWithCondition(element.question.likeCount)
            showNumberOfAttachedProductsWithCondition(element.question.answer.attachedProductCount)
            showNumberOfOtherAnswersWithCondition(element.question.totalAnswer)
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

    private fun showNumberOfLikesWithCondition(likeCount: String) {
        if(likeCount.toIntOrZero() > 0) {
            itemView.likeCount.text = likeCount
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
            itemView.seeOtherAnswers.text = String.format(OTHER_ANSWERS, otherAnswers)
            itemView.visibility = View.VISIBLE
        }
    }


}