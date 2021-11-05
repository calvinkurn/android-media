package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.junit.Test

class TopchatRoomGeneralTest : TopchatRoomTest() {

    private val exShopId = "1231"
    private val exUserId = "1232"

    @Test
    fun test_intent_ask_seller_with_custom_msg() {
        // Given
        val intentMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity {
            val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER_WITH_MSG, exShopId, intentMsg
            )
            it.putExtras(intent)
        }

        // Then
        onView(withId(R.id.new_comment)).check(matches(withText(intentMsg)))
    }

    @Test
    fun test_intent_ask_buyer_with_custom_msg() {
        // Given
        val intentMsg = "Hi buyer"
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity {
            val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG, exUserId, intentMsg
            )
            it.putExtras(intent)
        }

        // Then
        onView(withId(R.id.new_comment)).check(matches(withText(intentMsg)))
    }

    @Test
    fun should_show_dummy_toolbar_when_using_chatroom_askseller_deeplink() {
        // Given
        val source = "product"
        val shopName = "Testing Tokopedia"
        getChatUseCase.response = GetExistingChatPojo()
        getChatUseCase.isError = true

        // When
        launchChatRoomActivity {
            val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ASKSELLER,
                exShopId, "", source, shopName, ""
            )
            it.data = intent.data
            it.putExtras(intent)
        }

        // Then
        assertToolbarTitle(shopName)
    }

}