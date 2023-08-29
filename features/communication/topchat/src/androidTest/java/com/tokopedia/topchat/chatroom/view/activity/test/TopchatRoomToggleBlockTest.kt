package com.tokopedia.topchat.chatroom.view.activity.test

import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.headerRobot
import org.junit.Test

class TopchatRoomToggleBlockTest : TopchatRoomTest() {

    @Test
    fun should_show_blocked_toaster_when_click_header_menu_block_user() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatToggleBlockChatUseCase.response = chatToggleBlockChatUseCase.createResponse(
            isBlocked = true,
            isPromoBlocked = true
        )
        launchChatRoomActivity()

        // When
        headerRobot {
            clickThreeDotsMenu()
            clickBlockUser() // Header menu option
            clickBlockUser() // Pop-up button
        }

        // Then
        generalResult {
            assertToasterText(context.getString(com.tokopedia.topchat.R.string.title_success_block_chat))
        }
    }

    @Test
    fun should_show_unblocked_toaster_when_click_header_menu_block_user() {
        // Given
        getChatUseCase.response = getChatUseCase.blockedChatResponse
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatToggleBlockChatUseCase.response = chatToggleBlockChatUseCase.createResponse(
            isBlocked = true,
            isPromoBlocked = true
        )
        launchChatRoomActivity()

        // When
        headerRobot {
            clickThreeDotsMenu()
            clickUnBlockUser()
        }

        // Then
        generalResult {
            assertToasterText(context.getString(com.tokopedia.topchat.R.string.title_success_unblock_chat))
        }
    }

    @Test
    fun should_show_error_toaster_when_failed_to_block_user() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatToggleBlockChatUseCase.isError = true
        launchChatRoomActivity()

        // When
        headerRobot {
            clickThreeDotsMenu()
            clickBlockUser() // Header menu option
            clickBlockUser() // Pop-up button
        }

        // Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }
}
