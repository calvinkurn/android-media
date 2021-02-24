package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomChatMenuBehaviourTest : TopchatRoomTest() {

    /**
     * The attachment menu should be hidden when user tap sticker menu
     */
    @Test
    fun test_open_attachment_menu_then_open_sticker() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsSeller
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
        getChatUseCase.response = firstPageChatAsSeller
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

    @Test
    fun click_sticker_icon_show_sticker_container() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsSeller
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

}