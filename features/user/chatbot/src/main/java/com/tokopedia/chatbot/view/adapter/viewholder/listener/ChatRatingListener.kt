package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.rating.ChatRatingViewModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatRatingListener{
   fun onClickRating(element : ChatRatingViewModel,rating : Int)
}