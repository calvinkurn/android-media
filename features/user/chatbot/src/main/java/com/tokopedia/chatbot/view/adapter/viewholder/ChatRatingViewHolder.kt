package com.tokopedia.chatbot.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener

import java.util.Date

/**
 * @author by yfsx on 14/05/18.
 */
class ChatRatingViewHolder(itemView: View,
                           private val chatLinkHandlerListener: ChatLinkHandlerListener,
                           private val viewListener: ChatRatingListener) : BaseChatViewHolder<ChatRatingViewModel>(itemView) {
    private val message: TextView
    private val ratingHolder: LinearLayout
    private val ratingYes: ImageView
    private val ratingNo: ImageView
    private val ratingSelected: ImageView

    init {
        view = itemView
        message = itemView.findViewById(R.id.message)
        hour = itemView.findViewById(R.id.hour)
        date = itemView.findViewById(R.id.date)
        ratingHolder = itemView.findViewById(R.id.rating_option_holder)
        ratingYes = itemView.findViewById(R.id.rating_option_yes)
        ratingNo = itemView.findViewById(R.id.rating_option_no)
        ratingSelected = itemView.findViewById(R.id.rating_selected)
    }

    override fun bind(element: ChatRatingViewModel) {
        view.setOnClickListener { v -> KeyboardHandler.DropKeyboard(itemView.context, view) }

        message.movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
        message.text = MethodChecker.fromHtml(element.message)
        date.visibility = View.VISIBLE
        var time: String?

        try {
            val myTime = java.lang.Long.parseLong(element.replyTime)
            time = DateFormat.getLongDateFormat(itemView.context).format(Date(myTime))
        } catch (e: NumberFormatException) {
            time = element.replyTime
        }

        date.text = time

        if (element.isShowDate) {
            date.visibility = View.VISIBLE
        } else {
            date.visibility = View.GONE
        }


        var hourTime: String?

        try {
            hourTime = ChatTimeConverter.formatTime(java.lang.Long.parseLong(element.replyTime))
        } catch (e: NumberFormatException) {
            hourTime = element.replyTime
        }

        hour.text = hourTime

        when (element.ratingStatus) {
            ChatRatingViewModel.RATING_NONE -> {
                ratingHolder.visibility = View.VISIBLE
                ratingSelected.visibility = View.GONE
                ratingYes.setOnClickListener { v -> viewListener.onClickRating(element, ChatRatingViewModel.RATING_GOOD) }

                ratingNo.setOnClickListener { v -> viewListener.onClickRating(element, ChatRatingViewModel.RATING_BAD) }
            }

            ChatRatingViewModel.RATING_GOOD -> {
                ratingHolder.visibility = View.GONE
                ratingSelected.isSelected = true
                ratingSelected.visibility = View.VISIBLE
            }

            ChatRatingViewModel.RATING_BAD -> {
                ratingHolder.visibility = View.GONE
                ratingSelected.isSelected = false
                ratingSelected.visibility = View.VISIBLE
            }

            else -> {
                ratingHolder.visibility = View.GONE
                ratingSelected.visibility = View.GONE
            }
        }

    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_chat_rating
    }
}
