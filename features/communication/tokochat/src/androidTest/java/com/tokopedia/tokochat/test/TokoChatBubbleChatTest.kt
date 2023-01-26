package com.tokopedia.tokochat.test

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleResult
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleRobot
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaRobot
import com.tokopedia.tokochat.test.robot.ticker.TickerResult
import org.junit.Test

@UiTest
class TokoChatBubbleChatTest : BaseTokoChatTest() {

    @Test
    fun should_show_picture_attachment() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_image_ext.json"
        )

        // When
        launchChatRoomActivity()
        Thread.sleep(3000) // Wait for image to show

        // Then
        MessageBubbleResult.assertImageAttachmentVisibility(
            position = 0,
            isVisible = true
        )
    }

    @Test
    fun should_show_not_supported_attachment_voice_notes() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_voice_notes_ext.json"
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
                R.string.tokochat_unsupported_attachment_voice_note
            )
        )
    }

    @Test
    fun should_show_not_supported_attachment_general() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_sticker_ext.json"
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
                R.string.tokochat_unsupported_attachment_general
            )
        )
    }

    @Test
    fun should_show_new_sent_message_and_pending_mark() {
        // When
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea("Test 123")
        ReplyAreaRobot.clickReplyButton()
        Thread.sleep(2000) // Wait for message to be saved in db

        // Then
        MessageBubbleResult.assertMessageBubbleCheckMark(
            position = 0
        )
    }

    @Test
    fun should_show_long_message_bubble_message_when_text_too_long() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_long_message.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleReadMoreText(
            position = 0
        )
    }

    @Test
    fun should_show_long_message_bottom_sheet() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_long_message.json"
        )

        // When
        launchChatRoomActivity()
        MessageBubbleRobot.clickReadMore()
        Thread.sleep(2000) // Wait for bottomsheet to show

        // Then
        MessageBubbleResult.assertMessageBubbleBottomSheet()
    }

    @Test
    fun should_show_admin_message_as_ticker() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_system.json"
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
    }
}
