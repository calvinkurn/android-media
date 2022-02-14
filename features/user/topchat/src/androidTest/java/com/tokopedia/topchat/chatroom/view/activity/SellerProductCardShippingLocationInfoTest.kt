package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.shippinglocation.ShippingLocationResult.hasNoVisibleShippingLocationOn
import com.tokopedia.topchat.chatroom.view.activity.robot.shippinglocation.ShippingLocationResult.hasShippingLocationOnWithText
import com.tokopedia.topchat.chatroom.view.activity.robot.shippinglocation.ShippingLocationResult.hasVisibleShippingLocationOn
import org.junit.Test

@UiTest
class SellerProductCardShippingLocationInfoTest : TopchatRoomTest() {

    @Test
    fun should_show_shipping_location_info_if_i_am_seller_and_it_exist_on_response() {
        // Given
        getChatUseCase.response = getChatUseCase.withShippingInfo
        chatAttachmentUseCase.response = chatAttachmentUseCase.withShippingInfo
        val shippingText = chatAttachmentUseCase.getShippingText(
            0, chatAttachmentUseCase.response
        )
        launchChatRoomActivity()

        // Then
        hasVisibleShippingLocationOn(position = 1)
        hasShippingLocationOnWithText(position = 1, shippingText)
    }

    @Test
    fun should_hide_shipping_location_info_if_i_am_seller_and_it_is_empty_on_response() {
        // Given
        getChatUseCase.response = getChatUseCase.withShippingInfo
        chatAttachmentUseCase.response = chatAttachmentUseCase.withoutShippingInfo
        launchChatRoomActivity()

        // Then
        hasNoVisibleShippingLocationOn(position = 1)
    }

    @Test
    fun should_hide_shipping_location_info_if_i_am_buyer_and_it_exist_on_response() {
        // Given
        getChatUseCase.response = getChatUseCase.withShippingInfoBuyer
        chatAttachmentUseCase.response = chatAttachmentUseCase.withoutShippingInfo
        launchChatRoomActivity()

        // Then
        hasNoVisibleShippingLocationOn(position = 1)
    }

    @Test
    fun should_hide_shipping_location_info_if_tokocabang_is_true() {
        // Given
        getChatUseCase.response = getChatUseCase.withShippingInfo
        chatAttachmentUseCase.response = chatAttachmentUseCase.withShippingInfoAndTokocabang
        launchChatRoomActivity()

        // Then
        hasNoVisibleShippingLocationOn(position = 1)
    }
}