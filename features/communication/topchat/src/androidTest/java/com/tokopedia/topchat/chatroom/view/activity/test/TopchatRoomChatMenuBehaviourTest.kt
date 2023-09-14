package com.tokopedia.topchat.chatroom.view.activity.test

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaResult.assertSendBtnDisabled
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomChatMenuBehaviourTest : TopchatRoomTest() {

    @Test
    fun click_plus_icon_once_show_attachment_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        composeAreaResult {
            assertChatMenuVisibility(isDisplayed())
            assertChatStickerMenuVisibility(not(isDisplayed()))
            assertChatAttachmentMenuVisibility(isDisplayed())
        }
    }

    @Test
    fun click_plus_icon_twice_hide_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickPlusIconMenu()
        }

        // Then
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
            assertChatStickerMenuVisibility(not(isDisplayed()))
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun click_compose_area_when_attachment_menu_visible() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickComposeArea()
        }

        // Then
        generalResult {
            assertKeyboardIsVisible(activity)
        }
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
            assertChatStickerMenuVisibility(not(isDisplayed()))
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun click_compose_area_when_sticker_menu_visible() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickStickerIconMenu()
            clickComposeArea()
        }

        // Then
        generalResult {
            assertKeyboardIsVisible(activity)
        }
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
            assertChatStickerMenuVisibility(not(isDisplayed()))
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun click_sticker_icon_once_show_sticker_container() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickStickerIconMenu()
        }

        // Then
        composeAreaResult {
            assertChatMenuVisibility(isDisplayed())
            assertChatStickerMenuVisibility(isDisplayed())
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun click_sticker_icon_twice_hide_chat_menu_and_show_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickStickerIconMenu() // Open
            clickStickerIconMenu() // Close
        }

        // Then
        generalResult {
            assertKeyboardIsVisible(activity)
        }
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
            assertChatStickerMenuVisibility(not(isDisplayed()))
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
        }
    }

    /**
     * The attachment menu should be hidden when user tap sticker menu
     */
    @Test
    fun test_open_attachment_menu_then_open_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        stickerGroupUseCase.response = stickerGroupAsBuyer
        chatListStickerUseCase.response = stickerListAsBuyer
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickStickerIconMenu()
        }

        // Then
        composeAreaResult {
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
            assertChatStickerMenuVisibility(isDisplayed())
        }
    }

    @Test
    fun click_back_btn_when_attachment_menu_opened() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }
        generalRobot {
            pressBack()
        }

        // Then
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
            assertChatAttachmentMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun attachment_size_is_3_in_mainapp() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        composeAreaResult {
            assertChatAttachmentMenuVisibility(isDisplayed())
            assertAttachmentMenuCount(3)
        }
    }

    @Test
    fun attachment_size_is_4_in_sellerapp() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity(isSellerApp = true)

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        composeAreaResult {
            assertChatAttachmentMenuVisibility(isDisplayed())
            assertAttachmentMenuCount(4)
        }
    }

    @Test
    fun should_able_to_send_msg_after_typing_msg() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        val count = activityTestRule.activity
            .findViewById<RecyclerView>(R.id.recycler_view_chatroom)
            .adapter?.itemCount ?: 0

        composeAreaRobot {
            typeMessageComposeArea("Test")
            clickSendBtn()
        }

        // Then
        msgBubbleResult {
            assertBubbleMsg(0, withText("Test"))
        }
        generalResult {
            assertChatRecyclerview(withTotalItem(count + 1))
        }
        composeAreaRobot {
            typeMessageComposeArea("")
        }
    }

    @Test
    fun should_not_be_able_to_send_msg_when_msg_is_empty() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            typeMessageComposeArea("Test")
            typeMessageComposeArea("")
        }

        // Then
        assertSendBtnDisabled()
    }
}
