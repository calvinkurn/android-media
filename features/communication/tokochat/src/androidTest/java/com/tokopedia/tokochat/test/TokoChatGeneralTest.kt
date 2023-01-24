package com.tokopedia.tokochat.test

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatOrderHistoryResponse
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.header.HeaderResult
import com.tokopedia.tokochat.test.robot.header_date.HeaderDateResult
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaResult
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaRobot
import com.tokopedia.tokochat.test.robot.ticker.TickerResult
import com.tokopedia.tokochat_common.util.OrderStatusType
import org.junit.Test

@UiTest
class TokoChatGeneralTest : BaseTokoChatTest() {

    @Test
    fun should_show_chat_room_header() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertHeaderDisplayed(
            interlocutorName = "Tokofood Driver 13",
            licensePlate = "X001OAH"
        )
    }

    @Test
    fun should_show_enabled_call_button() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertCallButtonHeader(isDisabled = false)
    }

    @Test
    fun should_show_disabled_call_button() {
        // Given
        chatOrderHistoryResponse.editAndGetResponseObject {
            it.tokochatOrderProgress.state = OrderStatusType.COMPLETED
        }

        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertCallButtonHeader(isDisabled = true)
    }

    @Test
    fun should_show_order_progress() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertOrderHistory(
            merchantName = "TokoFood Outlet Test 1",
            timeDelivery = "17:35 - 17:38"
        )
    }

    @Test
    fun should_show_first_ticker() {
        // When
        launchChatRoomActivity()
        val adapter = getTokoChatAdapter()
        val lastItem = adapter.lastIndex

        // Then
        TickerResult.assertTickerVisibility(position = lastItem, isVisible = true)
    }

    @Test
    fun should_show_date_header_in_chat_history() {
        // When
        launchChatRoomActivity()
        val adapter = getTokoChatAdapter()
        val lastItem = adapter.lastIndex

        // Then
        HeaderDateResult.assertHeaderDateVisibility(position = lastItem, isVisible = true)
    }

    @Test
    fun should_show_reply_area_and_can_type() {
        // When
        val dummyText = "Ditunggu ya"
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea(dummyText)

        // Then
        ReplyAreaResult.assertReplyAreaIsDisplayed()
        ReplyAreaResult.assertTypeReplyAreaText(dummyText)
    }

    @Test
    fun should_enable_button_send_when_reply_area_is_not_empty() {
        // When
        val dummyText = "Ditunggu ya"
        launchChatRoomActivity()
        ReplyAreaRobot.clearReplyArea()
        ReplyAreaRobot.typeInReplyArea(dummyText)

        // Then
        ReplyAreaResult.assertButtonReply(isDisabled = false)
    }

    @Test
    fun should_disable_button_send_when_reply_area_is_empty() {
        // When
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea("Test 123")
        ReplyAreaRobot.clearReplyArea()

        // Then
        ReplyAreaResult.assertButtonReply(isDisabled = true)
    }
}
