package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingViewHolder
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAttachedProductAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.item_talk_reply.view.*

class TalkReplyViewHolder(view: View,
                          private val attachedProductCardListener: AttachedProductCardListener,
                          private val onKebabClickedListener: OnKebabClickedListener,
                          private val threadListener: ThreadListener
) : AbstractViewHolder<TalkReplyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply
        val IN_VIEWHOLDER = true
    }

    override fun bind(element: TalkReplyUiModel) {
        element.answer.apply {
            showProfilePicture(userThumbnail, userId)
            showDisplayName(userName, userId)
            showDate(createTimeFormatted)
            showSellerLabelWithCondition(isSeller)
            showAnswer(content, answerID)
            showNumberOfLikesWithCondition(likeCount)
            if(attachedProductCount > 0) {
                showAttachedProducts(attachedProducts)
            }
            showKebabWithConditions(answerID, state.allowReport, state.allowDelete, onKebabClickedListener)
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.replyProfilePicture.apply {
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
            itemView.replyDisplayName.apply{
                text = userName
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId)
                }
                visibility = View.VISIBLE
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.replyDate.apply {
                text = addBulletPointToDate(date)
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

    private fun showAttachedProducts(attachedProducts: List<AttachedProduct>) {
        val adapter = TalkReplyAttachedProductAdapter(attachedProductCardListener, IN_VIEWHOLDER)
        itemView.replyAttachedProductsRecyclerView.adapter = adapter
        adapter.setData(attachedProducts)
        itemView.replyAttachedProductsRecyclerView.visibility = View.VISIBLE
    }

    private fun showKebabWithConditions(commentId: String, allowReport: Boolean, allowDelete: Boolean, onKebabClickedListener: OnKebabClickedListener) {
        if(allowReport || allowDelete){
            itemView.replyKebab.setOnClickListener {
                onKebabClickedListener.onKebabClicked(commentId, allowReport, allowDelete)
            }
            itemView.replyKebab.visibility = View.VISIBLE
        }
    }



}