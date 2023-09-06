package com.tokopedia.topchat.chatroom.view.activity.test.seller

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.productResult
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
        productResult {
            assertProductAttachmentAtPosition(0)
            assertStockCountBtnAt(0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        }
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
        productResult {
            assertProductCarouselWithTotal(position = 0, total = 2)
            assertStockCountBtnOnCarouselAt(
                position = 0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertStockCountBtnOnCarouselAt(
                position = 1, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        }
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
        productResult {
            assertProductCarouselWithTotal(position = 0, total = 3)
            assertStockCountBtnOnCarouselAt(
                position = 0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertStockCountBtnOnCarouselAt(
                position = 1, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertStockCountBtnOnCarouselAt(
                position = 2, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        }
    }
}
