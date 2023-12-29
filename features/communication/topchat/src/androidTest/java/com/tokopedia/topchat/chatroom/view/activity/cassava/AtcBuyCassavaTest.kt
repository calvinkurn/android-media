package com.tokopedia.topchat.chatroom.view.activity.cassava

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.productRobot
import org.junit.Rule
import org.junit.Test

@CassavaTest
class AtcBuyCassavaTest : TopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(true, true)

    @Test
    fun product_card_atc_and_buy_tracker_test() {
        // Given
        val journeyId = "141"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentUseCase.chatAttachmentNoVariant
        launchChatRoomActivity()
        stubIntents()

        productRobot {
            clickATCButtonAt(1)
            clickBuyButtonAt(1)
        }

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }
}
