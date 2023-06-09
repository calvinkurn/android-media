package com.tokopedia.tokochat.test

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.state.StateResult
import org.junit.Test

@UiTest
class TokoChatStateChatRoomTest : BaseTokoChatTest() {

    override fun resetDatabase() {
        super.resetDatabase()
        resetChannelDetailDatabase()
    }

    @Test
    fun should_show_global_error_when_fail_get_channel_id() {
        // Given
        ApiResponseStub.channelIdResponse = Pair(
            400,
            "channel_id/fail_get_channel_id_invalid_channel.json"
        )

        // When
        launchChatRoomActivity()
        Thread.sleep(3000)

        // Then
        StateResult.assertGlobalErrorIsDisplayed()
    }

    @Test
    fun should_not_show_global_error_when_success_get_channel_id() {
        // When
        launchChatRoomActivity()

        // Then
        StateResult.assertGlobalErrorIsNotDisplayed()
    }

    @Test
    fun should_show_unavailable_bottom_sheet() {
        // Given
        ApiResponseStub.channelIdResponse = Pair(
            400,
            "channel_id/fail_get_channel_id_chat_closed.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        StateResult.assertUnavailableChatBottomSheet()
    }

    @Test
    fun should_show_global_error_bottom_sheet() {
        // Given
        tokoChatChannelUseCase.isConnected = false

        // When
        launchChatRoomActivity()
        Thread.sleep(7000) // Wait 7s

        // Then
        StateResult.assertGlobalErrorNoConnectionBottomSheet()
    }

    @Test
    fun should_show_read_only_chatroom() {
        // Given
        ApiResponseStub.channelDetailsResponse = Pair(
            200,
            "channel_details/success_get_channel_details_read_only.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        StateResult.assertReadOnlyReplyArea()
    }
}
