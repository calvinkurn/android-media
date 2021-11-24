package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
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

}