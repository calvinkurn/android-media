package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productResult
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import com.tokopedia.websocket.WebSocketResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import com.tokopedia.chat_common.R as chat_commonR

@UiTest
class TopchatRoomBuyerWebSocketTest : BaseBuyerTopchatRoomTest() {

    @Test
    fun sent_text_to_ws_and_got_response_from_ws() {
        // Given
        val myMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
            wsMineResponseText,
            FakeTopchatWebSocket.exStartTime
        )
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            typeMessageComposeArea(myMsg)
            clickSendBtn()
        }
        websocket.simulateResponse(wsMineResponseText)

        // Then
        msgBubbleResult {
            assertBubbleMsg(0, withText(myMsg))
        }
    }

    @Test
    fun received_normal_text_with_label_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
            wsMineResponseText,
            FakeTopchatWebSocket.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsSellerResponseText)

        // Then
        val label = wsSellerResponseText.jsonObject
            ?.get("label")?.asString
        msgBubbleResult {
            assertMsgInfo(0, withText(label))
        }
    }

    @Test
    fun received_normal_text_without_label_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
            wsMineResponseText,
            FakeTopchatWebSocket.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsSellerResponseText.setLabel(""))

        // Then
        msgBubbleResult {
            assertMsgInfo(0, not(isDisplayed()))
        }
    }

    @Test
    fun received_1_product_attachment_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
            wsMineResponseText,
            FakeTopchatWebSocket.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsSellerProductResponse)

        // Then
        productResult {
            assertProductAttachmentAtPosition(position = 0)
            hasProductBuyButtonWithText(
                context.getString(chat_commonR.string.action_buy),
                0
            )
        }
    }

    @Test
    fun received_2_product_attachment_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
            wsMineResponseText,
            FakeTopchatWebSocket.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsSellerProductResponse)
        wsSellerProductResponse = AndroidFileUtil.parse(
            "ws/seller/ws_seller_attach_product_2.json", // Change product ws response
            WebSocketResponse::class.java
        )
        websocket.simulateResponse(wsSellerProductResponse)

        // Then
        productResult {
            assertProductCarouselWithTotal(position = 0, total = 2)
            hasProductCarouselBuyButtonWithText(
                context.getString(chat_commonR.string.action_buy),
                0
            )
            hasProductCarouselBuyButtonWithText(
                context.getString(chat_commonR.string.action_buy),
                1
            )
        }
    }

    @Test
    fun received_3_product_attachment_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
            wsMineResponseText,
            FakeTopchatWebSocket.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsSellerProductResponse)
        wsSellerProductResponse = AndroidFileUtil.parse(
            "ws/seller/ws_seller_attach_product_2.json", // Change product ws response
            WebSocketResponse::class.java
        )
        websocket.simulateResponse(wsSellerProductResponse)
        wsSellerProductResponse = AndroidFileUtil.parse(
            "ws/seller/ws_seller_attach_product_3.json", // Change product ws response
            WebSocketResponse::class.java
        )
        websocket.simulateResponse(wsSellerProductResponse)

        // Then
        productResult {
            assertProductCarouselWithTotal(position = 0, total = 3)
            hasProductCarouselBuyButtonWithText(
                context.getString(chat_commonR.string.action_buy),
                0
            )
            hasProductCarouselBuyButtonWithText(
                context.getString(chat_commonR.string.action_buy),
                1
            )
            hasProductCarouselBuyButtonWithText(
                context.getString(chat_commonR.string.action_buy),
                2
            )
        }
    }
}
