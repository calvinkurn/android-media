package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductAttachmentAtPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductBuyButtonWithText
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductCarouselBuyButtonWithText
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasProductCarouselWithTotal
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.websocket.WebSocketResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import com.tokopedia.chat_common.R as chatCommonR

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
        typeMessage(myMsg)
        onView(withIndex(withId(R.id.send_but), 0))
            .perform(click())
        websocket.simulateResponse(wsMineResponseText)

        // Then
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                0,
                R.id.tvMessage
            )
        ).check(matches(withText(myMsg)))
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
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                0,
                R.id.txt_info
            )
        ).check(matches(withText(label)))
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
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                0,
                R.id.txt_info
            )
        ).check(matches(not(isDisplayed())))
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
        hasProductAttachmentAtPosition(position = 0)
        hasProductBuyButtonWithText(
            context.getString(chatCommonR.string.action_buy),
            0
        )
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
        hasProductCarouselWithTotal(position = 0, total = 2)
        hasProductCarouselBuyButtonWithText(
            context.getString(chatCommonR.string.action_buy),
            0
        )
        hasProductCarouselBuyButtonWithText(
            context.getString(chatCommonR.string.action_buy),
            1
        )
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
        hasProductCarouselWithTotal(position = 0, total = 3)
        hasProductCarouselBuyButtonWithText(
            context.getString(chatCommonR.string.action_buy),
            0
        )
        hasProductCarouselBuyButtonWithText(
            context.getString(chatCommonR.string.action_buy),
            1
        )
        hasProductCarouselBuyButtonWithText(
            context.getString(chatCommonR.string.action_buy),
            2
        )
    }
}
