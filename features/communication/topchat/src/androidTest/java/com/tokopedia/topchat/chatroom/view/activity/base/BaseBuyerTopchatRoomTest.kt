package com.tokopedia.topchat.chatroom.view.activity.base

import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.websocket.WebSocketResponse

open class BaseBuyerTopchatRoomTest : TopchatRoomTest() {

    protected var chatSrwResponseMultipleQuestion = ChatSmartReplyQuestionResponse()
    protected var chatSrwProductBundlingResponse = ChatSmartReplyQuestionResponse()
    protected var wsMineResponseText: WebSocketResponse = WebSocketResponse()
    protected var wsInterlocutorResponseText: WebSocketResponse = WebSocketResponse()
    protected var wsSellerResponseText: WebSocketResponse = WebSocketResponse()
    protected var wsSellerInvoiceResponse: WebSocketResponse = WebSocketResponse()
    protected var wsSellerImageResponse: WebSocketResponse = WebSocketResponse()
    protected var wsSellerProductResponse: WebSocketResponse = WebSocketResponse()
    protected var wsTickerReminderResponse: WebSocketResponse = WebSocketResponse()

    private val templateChats = listOf(
        "I am buyer",
        "Is this product ready?"
    )

    override fun before() {
        super.before()
        setupDefaultResponse()
    }

    override fun setupResponse() {
        super.setupResponse()
        chatSrwResponse = AndroidFileUtil.parse(
            "buyer/success_get_srw_questions.json",
            ChatSmartReplyQuestionResponse::class.java
        )
        chatSrwResponseMultipleQuestion = AndroidFileUtil.parse(
            "buyer/success_get_srw_multiple_questions.json",
            ChatSmartReplyQuestionResponse::class.java
        )
        chatSrwProductBundlingResponse = AndroidFileUtil.parse(
            "buyer/success_get_srw_product_bundling.json",
            ChatSmartReplyQuestionResponse::class.java
        )
        wsMineResponseText = AndroidFileUtil.parse(
            "ws_response_text.json",
            WebSocketResponse::class.java
        )
        wsSellerResponseText = AndroidFileUtil.parse(
            "buyer/ws_opposite_with_label.json",
            WebSocketResponse::class.java
        )
        wsSellerInvoiceResponse = AndroidFileUtil.parse(
            "buyer/ws_seller_invoice_response.json",
            WebSocketResponse::class.java
        )
        wsSellerImageResponse = AndroidFileUtil.parse(
            "buyer/ws_seller_attach_image.json",
            WebSocketResponse::class.java
        )
        wsSellerProductResponse = AndroidFileUtil.parse(
            "ws/seller/ws_seller_attach_product.json",
            WebSocketResponse::class.java
        )
        wsInterlocutorResponseText = AndroidFileUtil.parse(
            "seller/ws_interlocutor_response_text.json",
            WebSocketResponse::class.java
        )
        wsTickerReminderResponse = AndroidFileUtil.parse(
            "ticker_reminder/success_get_ws_response_with_ticker_reminder.json",
            WebSocketResponse::class.java
        )
    }

    private fun setupDefaultResponse() {
        chatSrwUseCase.response = chatSrwResponse
        getTemplateChatRoomUseCase.response = successGetTemplateResponse
    }

    protected fun WebSocketResponse.setLabel(label: String): WebSocketResponse {
        jsonObject?.remove("label")
        jsonObject?.addProperty("label", label)
        return this
    }
}
