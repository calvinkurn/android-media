package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import org.junit.Test

class TopchatRoomBuyerAttachedVoucherTest : BaseBuyerTopchatRoomTest() {

    @Test
    fun should_open_product_list_from_voucher_page_when_click_product_voucher_on_mainapp() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithBuyerResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
            doActionOnListItemAt(1, click())
        }

        // Then
        generalResult {
            openPageWithApplink("tokopedia://shop/10973651/voucher/7050189")
        }
    }
}
