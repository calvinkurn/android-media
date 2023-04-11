package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.rating.ChatRatingUiModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatRatingListener{
   fun onClickRating(element : ChatRatingUiModel, rating : Int)
}