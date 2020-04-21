package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reading.view.*
import kotlinx.android.synthetic.main.item_talk_reply.view.*

class TalkReplyViewHolder(view: View) : AbstractViewHolder<TalkReplyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply
    }

    override fun bind(element: TalkReplyUiModel) {
        element.answer.apply {
            showProfilePicture(userThumbnail)
            showDisplayName(userName)
            showDate(createTimeFormatted)
            showSellerLabelWithCondition(isSeller)
            showAnswer(content, answerID)
            showNumberOfLikesWithCondition(likeCount)
        }
    }

    private fun showProfilePicture(userThumbNail: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.replyProfilePicture.apply {
                loadImage(userThumbNail)
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDisplayName(userName: String) {
        if(userName.isNotEmpty()) {
            itemView.replyDisplayName.apply{
                text = userName
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.apply {
                replyDate.text = addBulletPointToDate(date)
                visibility = View.VISIBLE
            }
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.replySellerLabel.visibility = View.VISIBLE
        }
    }

    private fun addBulletPointToDate(date: String): String {
        return String.format(TalkReadingViewHolder.BULLET_POINT, date)
    }

    private fun showAnswer(answer: String, answerId: String) {
        if(answer.isNotEmpty()) {
            itemView.apply {
                replyMessage.text = answer
                visibility = View.VISIBLE
            }
        }
    }

    private fun showNumberOfLikesWithCondition(likeCount: Int) {
        if(likeCount > 0) {
            itemView.replyLikeCount.text = likeCount.toString()
        }
    }



}