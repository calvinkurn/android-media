package com.tokopedia.chatbot.view.adapter.viewholder

import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.EllipsizeMaker
import com.tokopedia.chatbot.EllipsizeMaker.MESSAGE_LINE_COUNT
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import java.util.*

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
    private val mesageLayout: RelativeLayout
    private val mesageBottom: TextView

    init {
        view = itemView
        message = itemView.findViewById(R.id.message)
        hour = itemView.findViewById(R.id.hour)
        date = itemView.findViewById(R.id.date)
        ratingHolder = itemView.findViewById(R.id.rating_option_holder)
        ratingYes = itemView.findViewById(R.id.rating_option_yes)
        ratingNo = itemView.findViewById(R.id.rating_option_no)
        ratingSelected = itemView.findViewById(R.id.rating_selected)
        mesageLayout = itemView.findViewById(R.id.message_text_holder)
        mesageBottom = itemView.findViewById(R.id.bottom_view)
    }

    override fun bind(element: ChatRatingViewModel) {
        view.setOnClickListener { v -> KeyboardHandler.DropKeyboard(itemView.context, view) }

        message.movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
        setMessage(element)
        date.visibility = View.VISIBLE
        var time: String?

        try {
            val myTime = (element.replyTime)?.toLong() ?: 0L
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

        hour.text = getHourTime(element.replyTime ?: "")

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

    override fun getHourTime(replyTime: String): String {
        return ChatBotTimeConverter.getHourTime(replyTime)
    }

    private fun setMessage(element: ChatRatingViewModel) {
        if (element.message.isNotEmpty()) {
            message.text = MethodChecker.fromHtml(element.message)
            message.post {
                if (message.lineCount > MESSAGE_LINE_COUNT) {
                    message.maxLines = MESSAGE_LINE_COUNT
                    message.text = EllipsizeMaker.getTruncatedMsg(message)
                    mesageBottom.visibility = View.VISIBLE
                    mesageBottom.setOnClickListener {
                        showFullMessage(element.message)
                    }

                } else {
                    mesageBottom.visibility = View.GONE
                    MethodChecker.setBackground(mesageLayout, ContextCompat.getDrawable(itemView.context,com.tokopedia.chat_common.R.drawable.left_bubble))
                }
            }

        }
    }

    private fun showFullMessage(message: String) {
        this.message.maxLines = Int.MAX_VALUE
        this.message.text = MethodChecker.fromHtml(message)
        mesageBottom.visibility = View.GONE
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_chat_rating
    }
}
