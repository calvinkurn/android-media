package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleAtcBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleBuyBtnAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasNoVisibleEmptyStockLabelAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult.hasVisibleLabelAtWithText
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
    fun should_show_remind_me_button_and_show_empty_stock_label_if_product_is_not_upcoming_campaign_and_inactive() {
        // Given
        getChatUseCase.response = getChatUseCase.upComingCampaignProduct
        chatAttachmentUseCase.response = chatAttachmentUseCase.notUpComingCampaignProduct
        launchChatRoomActivity()

        // Then
        hasVisibleRemindMeBtnAt(position = 1)
        hasNoVisibleAtcBtnAt(position = 1)
        hasNoVisibleBuyBtnAt(position = 1)
        hasVisibleLabelAtWithText(
            position = 1,
            stringRes = R.string.title_topchat_empty_stock
        )
    }

    // TODO: should show buy and atc button and hide empty stock label if product is not upcoming campaign and active

}