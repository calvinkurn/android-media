package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import org.hamcrest.CoreMatchers.not
import org.junit.Test

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
}
