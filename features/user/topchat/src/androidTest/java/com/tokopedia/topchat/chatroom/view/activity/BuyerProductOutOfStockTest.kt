package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleAtcBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleBuyBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasVisibleRemindMeBtnAt
import org.junit.Test

@UiTest
class BuyerProductOutOfStockTest : TopchatRoomTest() {

    @Test
    fun should_show_remind_me_button_and_hide_empty_stock_label_if_product_is_upcoming_campaign() {
        // Given
        getChatUseCase.response = getChatUseCase.upComingCampaignProduct
        chatAttachmentUseCase.response = chatAttachmentUseCase.upComingCampaignProduct
        launchChatRoomActivity()

        // Then
        hasVisibleRemindMeBtnAt(position = 1)
        hasNoVisibleAtcBtnAt(position = 1)
        hasNoVisibleBuyBtnAt(position = 1)
    }

    // TODO: should show remind me button and show empty stock label if product is not upcoming campaign and inactive
    // TODO: should show buy and atc button and hide empty stock label if product is not upcoming campaign and active

}