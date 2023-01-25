package com.tokopedia.tokochat.test

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleResult
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaRobot
import com.tokopedia.tokochat.test.robot.ticker.TickerResult
import org.junit.Test

@UiTest
class TokoChatBubbleChatTest : BaseTokoChatTest() {

    @Test
    fun should_show_picture_attachment() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200, "chat_history/success_get_chat_history_image_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertImageAttachmentVisibility(
            position = 0,
            isVisible = true
        )
        resetDatabase()
    }

    @Test
    fun should_show_not_supported_attachment_voice_notes() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200, "chat_history/success_get_chat_history_voice_notes_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertMessageBubbleText(
            position = 0,
            text = activity.getString(
                R.string.tokochat_unsupported_attachment_voice_note)
        )
        resetDatabase()
    }

    @Test
    fun should_show_not_supported_attachment_general() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200, "chat_history/success_get_chat_history_sticker_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertMessageBubbleText(
            position = 0,
            text = activity.getString(
                R.string.tokochat_unsupported_attachment_general)
        )
        resetDatabase()
    }

    @Test
    fun should_show_new_sent_message() {
        // When
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea("Test 123")
        ReplyAreaRobot.clickReplyButton()

        // Then
        MessageBubbleResult.assertMessageBubbleText(
            position = 0,
            text = "Test 123"
        )
        resetDatabase()
    }

    @Test
    fun should_show_long_message_bottom_sheet() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200, "chat_history/success_get_chat_history_sticker_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then

    }

    @Test
    fun should_show_admin_message_as_ticker() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200, "chat_history/success_get_chat_history_system.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        TickerResult.assertTickerVisibility(
            position = 0,
            isVisible = true
        )
        TickerResult.assertTickerText(
            "Pesanan selesai! Kamu bisa liat riwayat chat dan juga nge-chat driver sampai 2 jam setelah pesanan diselesaikan."
        )
        resetDatabase()
    }

    @Test
    fun should_show_mark_read() {
        // Given

        // When
        launchChatRoomActivity()

        // Then
        resetDatabase()
    }
}
