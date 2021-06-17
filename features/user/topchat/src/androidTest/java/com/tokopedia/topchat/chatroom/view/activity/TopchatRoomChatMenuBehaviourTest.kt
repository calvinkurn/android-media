package com.tokopedia.topchat.chatroom.view.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.DrawableMatcher
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
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
    fun click_compose_area_when_attachment_menu_visible() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        clickPlusIconMenu()
        clickComposeArea()

        // Then
        assertKeyboardIsVisible()
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
    fun click_compose_area_when_sticker_menu_visible() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        clickStickerIconMenu()
        clickComposeArea()

        // Then
        assertKeyboardIsVisible()
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

        // Then
        assertKeyboardIsVisible()
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

    @Test
    fun attachment_size_is_3_in_mainapp() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // WHen
        clickPlusIconMenu()

        // Then
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(withTotalItem(3))
        )
    }

    @Test
    fun attachment_size_is_4_in_sellerapp() {
        // Given
        setupChatRoomActivity(isSellerApp = true)
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // WHen
        clickPlusIconMenu()

        // Then
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
                matches(withTotalItem(4))
        )
    }

    @Test
    fun should_able_to_send_msg_after_typing_msg() {
        //Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        //When
        val count = activityTestRule.activity
                .findViewById<RecyclerView>(R.id.recycler_view)
                .adapter?.itemCount ?: 0

        onView(withId(R.id.new_comment)).perform(typeText("Test"))
        clickSendBtn()

        //Then
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        0, R.id.tvMessage
                ))
                .check(matches(withText("Test")))
        onView(withId(R.id.recycler_view)).check(matches(withTotalItem(count + 1)))
        onView(withId(R.id.new_comment)).check(matches(withText("")))
    }

    @Test
    fun should_not_be_able_to_send_msg_when_msg_is_empty() {
        //Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        //When
        onView(withId(R.id.new_comment)).perform(typeText("Test"))
        onView(withId(R.id.new_comment)).perform(clearText())

        //Then
        DrawableMatcher.compareDrawable(R.id.send_but, R.drawable.bg_topchat_send_btn_disabled)
    }

    @Test
    fun test_msg_sent_error_empty_text_click() {
        //Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        //When
        onView(withId(R.id.send_but)).perform(click())

        //Then
        assertSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }

}