package com.tokopedia.topchat.chatroom.view.activity.cassava

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardRobot.clickATCButtonAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardRobot.clickBuyButtonAt
import org.junit.Rule
import org.junit.Test

@CassavaTest
class AtcBuyCassavaTest: TopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(true, true)

    @Test
    fun product_card_atc_and_buy_tracker_test() {
        // Given
        val journeyId = "141"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentUseCase.chatAttachmentNoVariant
        launchChatRoomActivity()
        preventOpenOtherActivity()

        clickATCButtonAt(1)
        clickBuyButtonAt(1)

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }
}