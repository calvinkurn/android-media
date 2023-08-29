package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.chat_common.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
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
        onView(withId(R.id.header_menu)).perform(click())
        onView(withText("Blokir Pengguna")).perform(click()) // Header menu option
        onView(withText("Blokir Pengguna")).perform(click()) // Pop-up button

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
        onView(withId(R.id.header_menu)).perform(click())
        onView(withText("Batalkan blokir pengguna")).perform(click())

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
        onView(withId(R.id.header_menu)).perform(click())
        onView(withText("Blokir Pengguna")).perform(click()) // Header menu option
        onView(withText("Blokir Pengguna")).perform(click()) // Pop-up button

        // Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }
}
