package com.tokopedia.chatbot.view.listener

import android.content.Intent
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo

/**
 * @author by nisie on 07/12/18.
 */
interface ChatbotContract {
    interface View : BaseChatContract.View {

        fun showSnackbarError(stringId: Int)

        fun clearChatText()

        fun loadChatHistory()

        fun startNewSession()

        fun enableTyping()

        fun uploadUsingSecureUpload(data: Intent)


        fun onSuccessGetTickerData(tickerData: TickerData)

        fun onError(throwable: Throwable)

        fun onSuccessSubmitCsatRating(msg: String)

        fun onSuccessSubmitChatCsat(msg: String)

        fun onSuccessSendRating(pojo: SendRatingPojo, rating: Int, element: ChatRatingUiModel)

        fun hideReplyBox()

    }
}
