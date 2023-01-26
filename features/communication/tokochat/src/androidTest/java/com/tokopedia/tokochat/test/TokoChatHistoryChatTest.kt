package com.tokopedia.tokochat.test

import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.header_date.HeaderDateResult
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleResult
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleRobot
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaRobot
import com.tokopedia.tokochat.test.robot.ticker.TickerResult
import com.tokopedia.tokochat.view.chatroom.TokoChatViewModel
import org.junit.Test

@UiTest
class TokoChatHistoryChatTest : BaseTokoChatTest() {

    // Need to prepare before class,
    // in case the database is not ready after been deleted from other test class
    override fun before() {
        super.before()
        ConversationsRepository.instance!!.initGroupBookingChat(
            GOJEK_ORDER_ID_DUMMY,
            TokoChatViewModel.TOKOFOOD_SERVICE_TYPE,
            object : ConversationsGroupBookingListener {
                override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {}
                override fun onGroupBookingChannelCreationStarted() {
                    idlingResourceBeforeClass.increment()
                }
                override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                    idlingResourceBeforeClass.decrement()
                }
            },
            OrderChatType.Unknown
        )
    }

    @Test
    fun should_show_date_header_in_chat_history() {
        // When
        launchChatRoomActivity()
        val adapter = getTokoChatAdapter()
        val position = adapter.lastIndex - 1

        // Then
        HeaderDateResult.assertHeaderDateVisibility(position = position, isVisible = true)
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
