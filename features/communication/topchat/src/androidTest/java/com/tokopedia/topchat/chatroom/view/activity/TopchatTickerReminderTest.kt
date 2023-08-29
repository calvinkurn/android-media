package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.openPageWithApplink
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderResult.assertReminderTickerIsNotAtPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderResult.assertReminderTickerNotVisible
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderResult.assertReminderTickerVisibleAtPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderResult.assertReminderTickerVisibleWithText
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderRobot.clickTickerCloseButtonAt
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderRobot.clickTickerLabel
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import org.junit.Test

@UiTest
class TopchatTickerReminderTest : BaseBuyerTopchatRoomTest() {

    @Test
    fun should_show_announcement_ticker_when_gql_response_srw_ticker() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultTickerReminder
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            reminderTickerUseCase.defaultTickerReminder.getReminderTicker.replyId
        )
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // Then
        assertReminderTickerVisibleAtPosition(1)
    }

    @Test
    fun should_show_warning_ticker_when_gql_response_fraud_ticker() {
        // Given
        val url = "https://www.tokopedia.com/help/article/st-1030-jaga-keamanan-akun-tokopedia"
        val urlLabel = "<a href='https://www.tokopedia.com/help/article/st-1030-jaga-keamanan-akun-tokopedia'>Click disini</a>"
        val subText = "Hati-hati penipuan, ya! Hindari bertransaksi dan menghubungi penjual di luar Tokopedia."
        reminderTickerUseCase.response = reminderTickerUseCase.customTickerReminder(
            featureId = 2,
            subText = "$subText $urlLabel",
            urlLabel = urlLabel,
            replyId = "1234",
            tickerType = "warning"
        )
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            reminderTickerUseCase.defaultTickerReminder.getReminderTicker.replyId
        )
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        clickTickerLabel("Click disini")

        // Then
        assertReminderTickerVisibleAtPosition(1)
        assertReminderTickerVisibleWithText(1, subText)
        openPageWithApplink(url)
    }

    @Test
    fun should_show_ticker_below_matched_bubble_if_eligible_position_found_in_page_2_or_more() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultTickerReminder
        getChatUseCase.response = getChatUseCase.noMatchTickerReminderReplyId
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // When
        val lastIndex = getChatUseCase.getLastIndexOf(getChatUseCase.response)
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            reminderTickerUseCase.defaultTickerReminder.getReminderTicker.replyId
        )
        generalRobot {
            scrollChatToPosition(lastIndex)
            val newlastIndex = lastIndex + getChatUseCase.getLastIndexOf(getChatUseCase.response)
            scrollChatToPosition(newlastIndex)
            smoothScrollChatToPosition(newlastIndex)
        }

        // Then
        val expectedTickerPosition = lastIndex + 2
        assertReminderTickerVisibleAtPosition(expectedTickerPosition)
    }

    @Test
    fun should_close_ticker_when_close_btn_closed() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultTickerReminder
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            reminderTickerUseCase.defaultTickerReminder.getReminderTicker.replyId
        )
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // When
        clickTickerCloseButtonAt(1)

        // Then
        assertReminderTickerNotVisible()
    }

    @Test
    fun should_not_show_ticker_if_no_ticker_available_to_exist() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.falseSrwPrompt
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            reminderTickerUseCase.defaultTickerReminder.getReminderTicker.replyId
        )
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // Then
        assertReminderTickerNotVisible()
    }

    @Test
    fun should_not_show_ticker_if_no_eligible_position_exist() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.noRegexMatchSrwPrompt
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            "wrong reply Id"
        )
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // Then
        assertReminderTickerNotVisible()
    }

    @Test
    fun sent_fraud_text_to_ws_and_got_ticker_from_response_ws() {
        // Given
        val testMsg = "Fraud text 123"
        reminderTickerUseCase.response = reminderTickerUseCase.defaultTickerReminder
        getChatUseCase.response = getChatUseCase.noMatchTickerReminderReplyId
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // When
        sendWebSocketTickerMessageWithLocalId("7e5bc157-d61c-4050-b070-c9278d204bc5", testMsg)

        // Then
        assertViewInRecyclerViewAt(1, R.id.tvMessage, withText(testMsg))
        assertReminderTickerVisibleAtPosition(0)
    }

    @Test
    fun should_not_show_double_ticker_from_response_ws() {
        // Given
        val testMsg = "Fraud text 123"
        reminderTickerUseCase.response = reminderTickerUseCase.defaultTickerReminder
        getChatUseCase.response = getChatUseCase.noMatchTickerReminderReplyId
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // When
        // Send first ws message
        sendWebSocketTickerMessageWithLocalId("localId1", testMsg)

        // Send second ws message
        sendWebSocketTickerMessageWithLocalId("localId2", testMsg)

        // Then
        assertReminderTickerVisibleAtPosition(1)
        assertReminderTickerIsNotAtPosition(0)
    }

    @Test
    fun should_not_show_double_ticker_from_response_ws_and_gql() {
        // Given
        val testMsg = "Fraud text 123"
        reminderTickerUseCase.response = reminderTickerUseCase.defaultTickerReminder
        getChatUseCase.response = getChatUseCase.getTickerReminderWithReplyId(
            reminderTickerUseCase.defaultTickerReminder.getReminderTicker.replyId
        )
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultTickerReminderAttachment
        launchChatRoomActivity()

        // When
        sendWebSocketTickerMessageWithLocalId("localId1", testMsg)

        // Then
        assertReminderTickerVisibleAtPosition(3)
        assertReminderTickerIsNotAtPosition(0)
    }

    private fun sendWebSocketTickerMessageWithLocalId(localId: String, message: String) {
        webSocketPayloadGenerator.fakeLocalId = localId
        changeResponseWebSocket(
            wsTickerReminderResponse
        ) {
            it.jsonObject?.addProperty(
                "local_id",
                localId
            )
            it.jsonObject?.get("reminder_ticker")?.asJsonObject?.addProperty(
                "reply_id",
                localId
            )
        }
        changeResponseStartTime(
            wsTickerReminderResponse,
            FakeTopchatWebSocket.exStartTime
        )
        composeAreaRobot {
            typeMessageComposeArea(message)
            clickSendBtn()
        }
        websocket.simulateResponse(wsTickerReminderResponse)
    }
}
