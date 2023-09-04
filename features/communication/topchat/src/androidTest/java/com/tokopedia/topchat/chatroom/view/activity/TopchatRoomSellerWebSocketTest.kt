package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductAttachmentAtPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductCarouselStockButtonWithText
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductCarouselWithTotal
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductStockButtonWithText
import com.tokopedia.websocket.WebSocketResponse
import org.junit.Test

class TopchatRoomSellerWebSocketTest : BaseSellerTopchatRoomTest() {

    @Test
    fun received_1_product_attachment_from_buyer() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsBuyerProductResponse)

        // Then
        hasProductAttachmentAtPosition(position = 0)
        hasProductStockButtonWithText(position = 0)
    }

    @Test
    fun received_2_product_attachment_from_buyer() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsBuyerProductResponse)
        wsBuyerProductResponse = AndroidFileUtil.parse(
            "ws/buyer/ws_buyer_attach_product_2.json", // Change product ws response
            WebSocketResponse::class.java
        )
        websocket.simulateResponse(wsBuyerProductResponse)

        // Then
        hasProductCarouselWithTotal(position = 0, total = 2)
        hasProductCarouselStockButtonWithText(position = 0)
        hasProductCarouselStockButtonWithText(position = 1)
    }

    @Test
    fun received_3_product_attachment_from_buyer() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsBuyerProductResponse)
        wsBuyerProductResponse = AndroidFileUtil.parse(
            "ws/buyer/ws_buyer_attach_product_2.json", // Change product ws response
            WebSocketResponse::class.java
        )
        websocket.simulateResponse(wsBuyerProductResponse)
        wsBuyerProductResponse = AndroidFileUtil.parse(
            "ws/buyer/ws_buyer_attach_product_3.json", // Change product ws response
            WebSocketResponse::class.java
        )
        websocket.simulateResponse(wsBuyerProductResponse)

        // Then
        hasProductCarouselWithTotal(position = 0, total = 3)
        hasProductCarouselStockButtonWithText(position = 0)
        hasProductCarouselStockButtonWithText(position = 1)
        hasProductCarouselStockButtonWithText(position = 2)
    }
}
