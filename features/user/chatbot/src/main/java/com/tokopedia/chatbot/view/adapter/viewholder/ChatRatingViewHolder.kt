package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by yfsx on 14/05/18.
 */
class ChatRatingViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    chatbotAdapterListener: ChatbotAdapterListener,
    private val viewListener: ChatRatingListener
) : BaseChatBotViewHolder<ChatRatingUiModel>(itemView, chatbotAdapterListener) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private val thumbsUpCntr = itemView.findViewById<CardView>(R.id.rating_option_yes_container)
    private val thumbsDownCntr = itemView.findViewById<CardView>(R.id.rating_option_no_container)
    private val thumbsDown = itemView.findViewById<ImageView>(R.id.rating_option_no)
    private val thumbsUp = itemView.findViewById<ImageView>(R.id.rating_option_yes)
    private val ratingHolder = itemView.findViewById<LinearLayout>(R.id.rating_option_holder)

    override fun bind(viewModel: ChatRatingUiModel) {
        super.bind(viewModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            viewModel.message,
            customChatLayout,
            movementMethod
        )
        bindRatingView(viewModel)
    }

    private fun bindRatingView(viewModel: ChatRatingUiModel) {
        resetRating()
        when (viewModel.ratingStatus) {
            ChatRatingUiModel.RATING_NONE -> {
                ratingHolder.show()
                thumbsUpCntr.setOnClickListener {
                    viewListener.onClickRating(
                        viewModel,
                        ChatRatingUiModel.RATING_GOOD
                    )
                }

                thumbsDownCntr.setOnClickListener {
                    viewListener.onClickRating(
                        viewModel,
                        ChatRatingUiModel.RATING_BAD
                    )
                }
            }

            ChatRatingUiModel.RATING_GOOD -> setRatingGood()
            ChatRatingUiModel.RATING_BAD -> setRatingBad()
            else -> ratingHolder.hide()
        }
    }

    private fun setRatingBad() {
        ratingHolder.visibility = View.VISIBLE
        thumbsUpCntr.hide()
        thumbsDown.setColorFilter(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN500
            )
        )
        thumbsDown.show()
    }

    private fun setRatingGood() {
        ratingHolder.visibility = View.VISIBLE
        thumbsDownCntr.hide()
        thumbsUp.setColorFilter(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        )
        thumbsUp.show()
    }

    private fun resetRating() {
        thumbsUp.colorFilter = null
        thumbsDown.colorFilter = null
        thumbsUpCntr.show()
        thumbsDownCntr.show()
    }

    override fun getCustomChatLayoutId(): Int = com.tokopedia.chatbot.R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_tutd_rating
    }
}
