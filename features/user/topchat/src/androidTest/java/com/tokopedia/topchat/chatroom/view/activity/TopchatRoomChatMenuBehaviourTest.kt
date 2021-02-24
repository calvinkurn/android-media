package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.matchers.isKeyboardShown
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.`is`
import org.junit.Test

class TopchatRoomChatMenuBehaviourTest : TopchatRoomTest() {

    @Test
    fun click_plus_icon_once_show_attachment_menu() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        clickPlusIconMenu()

        // Then
        onView(withId(R.id.fl_chat_menu)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.ll_sticker_container)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(isDisplayed())
        )
    }

    @Test
    fun click_plus_icon_twice_hide_chat_menu() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        clickPlusIconMenu()
        clickPlusIconMenu()

        // Then
        onView(withId(R.id.fl_chat_menu)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.ll_sticker_container)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(not(isDisplayed()))
        )
    }

    @Test
    fun click_sticker_icon_once_show_sticker_container() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        inflateTestFragment()

        // WHen
        clickStickerIconMenu()

        // Then
        onView(withId(R.id.fl_chat_menu)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.ll_sticker_container)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(not(isDisplayed()))
        )
    }

    @Test
    fun click_sticker_icon_twice_hide_chat_menu_and_show_keyboard() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        inflateTestFragment()

        // WHen
        clickStickerIconMenu()
        clickStickerIconMenu()
        waitForIt(KEYBOARD_DELAY)

        // Then
        assertThat(isKeyboardShown(), `is`(true))
        onView(withId(R.id.fl_chat_menu)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.ll_sticker_container)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(not(isDisplayed()))
        )
    }

    /**
     * The attachment menu should be hidden when user tap sticker menu
     */
    @Test
    fun test_open_attachment_menu_then_open_sticker() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        inflateTestFragment()

        // WHen
        clickPlusIconMenu()
        clickStickerIconMenu()

        // Then
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.ll_sticker_container)).check(
                matches(isDisplayed())
        )
    }

    @Test
    fun click_back_btn_when_attachment_menu_opened() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // WHen
        clickPlusIconMenu()
        pressBack()

        // Then
        onView(withId(R.id.fl_chat_menu)).check(
                matches(not(isDisplayed()))
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(not(isDisplayed()))
        )
    }

    // TODO: Test attachment size buyer and seller
}