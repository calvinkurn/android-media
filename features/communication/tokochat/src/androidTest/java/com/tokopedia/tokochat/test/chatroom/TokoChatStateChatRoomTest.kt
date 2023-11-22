package com.tokopedia.tokochat.test.chatroom

import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channelMember.ChannelMember
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.stub.domain.response.ApiResponseModelStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatRoomTest
import com.tokopedia.tokochat.test.chatroom.robot.state.StateResult
import org.junit.Test

@UiTest
class TokoChatStateChatRoomTest : BaseTokoChatRoomTest() {

    @Test
    fun should_show_global_error_when_fail_get_channel_id() {
        // Given
        ApiResponseStub.getInstance().channelIdResponse = ApiResponseModelStub(
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
        ApiResponseStub.getInstance().channelIdResponse = ApiResponseModelStub(
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
        tokoChatRoomUseCase.isConnected = false

        // When
        launchChatRoomActivity()
        Thread.sleep(3000) // Wait for bottomsheet

        // Then
        StateResult.assertGlobalErrorNoConnectionBottomSheet()
    }

    /**
     * These tests will use mock usecase because of how flaky it was when using room DB from SDK
     * The value always changing based on several sources, it's unpredictable
     */
    @Test
    fun should_show_read_only_chatroom() {
        // Given
        ApiResponseStub.getInstance().channelDetailsResponse = ApiResponseModelStub(
            200,
            "channel_details/success_get_channel_details_read_only.json"
        )
        tokoChatRoomUseCase.conversationsChannel = generateConversationsChannelDummy(
            expiresAt = 0,
            readModeStartsAt = 1
        )

        // When
        launchChatRoomActivity()

        // Then
        StateResult.assertReadOnlyReplyArea()
    }

    @Test
    fun should_show_unavailable_bottomsheet_when_expired_chatroom() {
        // Given
        ApiResponseStub.getInstance().channelDetailsResponse = ApiResponseModelStub(
            200,
            "channel_details/success_get_channel_details_read_only.json"
        )
        tokoChatRoomUseCase.conversationsChannel = generateConversationsChannelDummy(
            expiresAt = 1,
            readModeStartsAt = 0
        )

        // When
        launchChatRoomActivity()

        // Then
        StateResult.assertUnavailableChatBottomSheet()
    }

    private fun generateConversationsChannelDummy(
        expiresAt: Long,
        readModeStartsAt: Long
    ): ConversationsChannel {
        return ConversationsChannel(
            id = CHANNEL_ID_DUMMY,
            url = CHANNEL_ID_DUMMY,
            channelImageUrl = "",
            name = GOJEK_ORDER_ID_DUMMY,
            type = "group-booking",
            unreadCount = 0,
            lastMessage = null,
            createdBy = ChannelMember("00000000-0000-0000-0000-000000000000"),
            members = listOf(),
            readOnly = true,
            createdAt = 1673949438430,
            expiresAt = expiresAt,
            readModeStartsAt = readModeStartsAt,
            lastRead = mapOf("bf6ba1cf-90c4-4b8d-9dbb-f78279d67bfc" to 673949690347, "835a69de-577e-4881-bf1d-4e3eed13c643" to 1673950390715),
            updatedAt = 1673950390788,
            metadata = null
        )
    }
}
