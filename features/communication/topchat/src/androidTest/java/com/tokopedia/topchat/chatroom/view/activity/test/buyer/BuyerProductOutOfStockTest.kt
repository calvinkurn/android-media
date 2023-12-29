package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.productResult
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
        productResult {
            hasVisibleRemindMeBtnAt(position = 1)
            hasNoVisibleAtcBtnAt(position = 1)
            hasNoVisibleBuyBtnAt(position = 1)
            hasNoVisibleEmptyStockLabelAt(position = 1)
        }
    }

    @Test
    fun should_show_remind_me_button_and_show_empty_stock_label_if_product_is_not_upcoming_campaign_and_inactive() {
        // Given
        getChatUseCase.response = getChatUseCase.upComingCampaignProduct
        chatAttachmentUseCase.response = chatAttachmentUseCase.notUpComingCampaignProductAndInactive
        launchChatRoomActivity()

        // Then
        productResult {
            hasVisibleRemindMeBtnAt(position = 1)
            hasNoVisibleAtcBtnAt(position = 1)
            hasNoVisibleBuyBtnAt(position = 1)
            hasVisibleLabelAtWithText(
                position = 1,
                stringRes = R.string.title_topchat_empty_stock
            )
        }
    }

    @Test
    fun should_show_buy_and_atc_button_and_hide_empty_stock_label_if_product_is_not_upcoming_campaign_and_active() {
        // Given
        getChatUseCase.response = getChatUseCase.upComingCampaignProduct
        chatAttachmentUseCase.response = chatAttachmentUseCase.notUpComingCampaignProductAndActive
        launchChatRoomActivity()

        // Then
        productResult {
            hasNoVisibleRemindMeBtnAt(position = 1)
            hasVisibleAtcBtnAt(position = 1)
            hasVisibleBuyBtnAt(position = 1)
            hasNoVisibleEmptyStockLabelAt(position = 1)
        }
    }
}
