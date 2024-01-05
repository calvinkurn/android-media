package com.tokopedia.topchat.chatroom.view.activity.test

import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.headerResult
import com.tokopedia.topchat.chatroom.view.activity.robot.orderProgressResult
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.RoomSettingFraudAlertViewHolder
import org.junit.Before
import org.junit.Test

@UiTest
class TopchatRoomGeneralTest : TopchatRoomTest() {

    private val exShopId = "1231"
    private val exUserId = "1232"
    private var orderProgressResponseNotEmpty = OrderProgressResponse()

    @Before
    fun additionalSetup() {
        orderProgressResponseNotEmpty = AndroidFileUtil.parse(
            "success_get_order_progress.json",
            OrderProgressResponse::class.java
        )
    }

    @Test
    fun test_intent_ask_seller_with_custom_msg() {
        // Given
        val intentMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConst.TOPCHAT_ROOM_ASKSELLER_WITH_MSG,
                exShopId,
                intentMsg
            )
            it.putExtras(intent)
        }

        // Then
        composeAreaResult {
            assertTypeMessageText(intentMsg)
        }
    }

    @Test
    fun test_intent_ask_buyer_with_custom_msg() {
        // Given
        val intentMsg = "Hi buyer"
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG,
                exUserId,
                intentMsg
            )
            it.putExtras(intent)
        }

        // Then
        composeAreaResult {
            assertTypeMessageText(intentMsg)
        }
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
                context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                exShopId,
                "",
                source,
                shopName,
                ""
            )
            it.data = intent.data
            it.putExtras(intent)
        }

        // Then
        headerResult {
            assertToolbarTitle(shopName)
        }
    }

    @Test
    fun should_show_fraud_alert() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        getChatUseCase.response.chatReplies.list = arrayListOf()
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatRoomSettingUseCase.response = chatRoomSettingResponse
        launchChatRoomActivity(isSellerApp = true)

        // Then
        generalResult {
            assertChatRecyclerview(
                // position 0 is TopchatEmptyViewHolder
                hasViewHolderItemAtPosition(1, RoomSettingFraudAlertViewHolder::class.java)
            )
            assertSettingFraudAlert(withSubstring("Hati-hati penipuan!"))
        }
    }

    @Test
    fun should_show_order_progress() {
        getChatUseCase.response = firstPageChatAsBuyer
        getChatUseCase.response.chatReplies.list = arrayListOf()
        chatAttachmentUseCase.response = chatAttachmentResponse
        orderProgressUseCase.response = orderProgressResponseNotEmpty
        launchChatRoomActivity()

        // Then
        orderProgressResult {
            assertOrderProgressTitle(orderProgressResponseNotEmpty.chatOrderProgress.name)
        }
    }

    @Test
    fun should_not_show_order_progress() {
        getChatUseCase.response = firstPageChatAsBuyer
        getChatUseCase.response.chatReplies.list = arrayListOf()
        chatAttachmentUseCase.response = chatAttachmentResponse
        orderProgressUseCase.response = orderProgressResponse
        launchChatRoomActivity()

        // Then
        orderProgressResult {
            assertOrderProgressGone()
        }
    }
}
