package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleAtcBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleBuyBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleEmptyStockLabelAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleRemindMeBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasVisibleAtcBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasVisibleBuyBtnAt
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
        hasNoVisibleEmptyStockLabelAt(position = 1)
    }

    @Test
    fun should_show_buy_and_atc_button_and_hide_empty_stock_label_if_product_is_not_upcoming_campaign_and_active() {
        // Given
        getChatUseCase.response = getChatUseCase.upComingCampaignProduct
        chatAttachmentUseCase.response = chatAttachmentUseCase.notUpComingCampaignProductAndActive
        launchChatRoomActivity()

        // Then
        hasNoVisibleRemindMeBtnAt(position = 1)
        hasVisibleAtcBtnAt(position = 1)
        hasVisibleBuyBtnAt(position = 1)
        hasNoVisibleEmptyStockLabelAt(position = 1)
    }
}
