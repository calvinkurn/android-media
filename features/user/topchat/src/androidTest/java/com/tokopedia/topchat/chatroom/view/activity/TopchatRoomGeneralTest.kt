package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomGeneralTest : TopchatRoomTest() {

    private val exShopId = "1231"
    private val exUserId = "1232"

    @Test
    fun test_intent_ask_seller_with_custom_msg() {
        // Given
        val intentMsg = "Hi seller"
        setupChatRoomActivity {
            val intent = RouteManager.getIntent(
                    context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER_WITH_MSG, exShopId, intentMsg
            )
            it.putExtras(intent)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // Then
        onView(withId(R.id.new_comment)).check(matches(withText(intentMsg)))
    }

    @Test
    fun test_intent_ask_buyer_with_custom_msg() {
        // Given
        val intentMsg = "Hi buyer"
        setupChatRoomActivity {
            val intent = RouteManager.getIntent(
                    context, ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG, exUserId, intentMsg
            )
            it.putExtras(intent)
        }
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // Then
        onView(withId(R.id.new_comment)).check(matches(withText(intentMsg)))
    }

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