package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatRatingListener {
    fun onClickRating(element: ChatRatingUiModel, rating: Int)
}
