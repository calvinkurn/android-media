package com.tokopedia.talk.feature.reply.presentation.adapter.viewholder

import android.graphics.Color
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAttachedProductAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.item_talk_reply.view.*
import kotlinx.android.synthetic.main.item_talk_reply_header.view.*

class TalkReplyViewHolder(view: View,
                          private val attachedProductCardListener: AttachedProductCardListener,
                          private val onKebabClickedListener: OnKebabClickedListener,
                          private val threadListener: ThreadListener
) : AbstractViewHolder<TalkReplyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reply
        const val IN_VIEWHOLDER = true
    }

    override fun bind(element: TalkReplyUiModel) {
        itemView.talkReplyContainer.setBackgroundColor(Color.WHITE)
        element.answer.apply {
            showProfilePicture(userThumbnail, userId)
            showDisplayName(userName, userId)
            showDate(createTimeFormatted)
            showSellerLabelWithCondition(isSeller)
            showAnswer(content, state.isMasked, maskedContent)
            if(attachedProductCount > 0) {
                showAttachedProducts(attachedProducts.toMutableList())
            }
            showKebabWithConditions(answerID, state.allowReport, state.allowDelete, onKebabClickedListener)
            showTickerWithCondition(state.isMasked, state.allowUnmask)
        }
    }

    private fun showProfilePicture(userThumbNail: String, userId: String) {
        if(userThumbNail.isNotEmpty()) {
            itemView.replyProfilePicture.apply {
                loadImage(userThumbNail)
                setOnClickListener {
                    threadListener.onUserDetailsClicked(userId)
                }
                show()
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
                show()
            }
        }
    }

    private fun showDate(date: String) {
        if(date.isNotEmpty()) {
            itemView.replyDate.apply {
                text = addBulletPointToDate(date)
                show()
            }
        }
    }

    private fun showSellerLabelWithCondition(isSeller: Boolean) {
        if(isSeller) {
            itemView.replySellerLabel.show()
        }
    }

    private fun addBulletPointToDate(date: String): String {
        return String.format(itemView.context.getString(R.string.talk_formatted_date), date)
    }

    private fun showAnswer(answer: String, isMasked: Boolean, maskedContent: String) {
        if(isMasked) {
            itemView.replyMessage.apply {
                text = maskedContent
                show()
                isEnabled = false
            }
            return
        }
        if(answer.isNotEmpty()) {
            itemView.replyMessage.apply {
                text = HtmlLinkHelper(context, answer).spannedString
                movementMethod = LinkMovementMethod.getInstance()
                show()
            }
        }
    }

    private fun showAttachedProducts(attachedProducts: MutableList<AttachedProduct>) {
        val attachedProductAdapter = TalkReplyAttachedProductAdapter(attachedProductCardListener, IN_VIEWHOLDER)
        attachedProducts.add(0, AttachedProduct())
        attachedProductAdapter.setData(attachedProducts)
        itemView.replyAttachedProductsRecyclerView.apply {
            adapter = attachedProductAdapter
            show()
        }
    }

    private fun showKebabWithConditions(commentId: String, allowReport: Boolean, allowDelete: Boolean, onKebabClickedListener: OnKebabClickedListener) {
        if(allowReport || allowDelete){
            itemView.replyKebab.setOnClickListener {
                onKebabClickedListener.onKebabClicked(commentId, allowReport, allowDelete)
            }
            itemView.replyKebab.show()
        }
    }

    private fun showTickerWithCondition(isMasked: Boolean, allowUnmask: Boolean) {
        if(isMasked && allowUnmask) {
            itemView.replyReportedTicker.show()
        }
    }


}