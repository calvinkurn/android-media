package com.tokopedia.chatbot.chatbot2.view.listener

import android.content.Intent
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes

/**
 * @author by nisie on 07/12/18.
 */
interface ChatbotContract {
    interface View : BaseChatContract.View {
        fun onUploadUndersizedImage()

        fun onUploadOversizedImage()

        fun clearChatText()

        fun openCsat(csatResponse: WebSocketCsatResponse)

        fun onReceiveChatSepratorEvent(chatSepratorUiModel: ChatSepratorUiModel, quickReplyList: List<QuickReplyUiModel>)

        fun updateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?)

        fun removeDummy(visitable: Visitable<*>)

        fun loadChatHistory()

        fun startNewSession()

        fun blockTyping()

        fun enableTyping()

        fun uploadUsingSecureUpload(data: Intent)

        fun sendInvoiceForArticle()

        fun onSuccessGetTickerData(tickerData: com.tokopedia.chatbot.chatbot2.data.TickerData.TickerData)

        fun onError(throwable: Throwable)

        fun onSuccessSubmitCsatRating(msg: String)

        fun onSuccessSubmitChatCsat(msg: String)

        fun visibilityReplyBubble(state: Boolean)

        fun onSuccessSendRating(pojo: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo)

        fun videoUploadEligibilityHandler(state: Boolean)

        fun onVideoUploadChangeView(uiModel: VideoUploadUiModel)

        fun setBigReplyBoxTitle(text: String, placeholder: String)

        fun hideReplyBox()
    }
}
