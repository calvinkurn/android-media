package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.junit.Test

class TopchatRoomGeneralTest : TopchatRoomTest() {

    private val exShopId = "1231"

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

}