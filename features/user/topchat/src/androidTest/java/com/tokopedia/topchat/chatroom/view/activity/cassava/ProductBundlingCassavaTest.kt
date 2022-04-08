package com.tokopedia.topchat.chatroom.view.activity.cassava

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doScrollChatToPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickCtaProductBundling
import org.hamcrest.MatcherAssert
import org.junit.Test

class ProductBundlingCassavaTest: TopchatRoomTest() {

    var cassavaTestRule = CassavaTestRule(false, false)

    @Test
    fun track_multiple_product_bundling() {
        // Given
        val journeyId = ""
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity()

        // Then
        MatcherAssert.assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun track_single_product_bundling() {
        // Given
        val journeyId = ""
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        launchChatRoomActivity()

        // Then
        MatcherAssert.assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun track_click_product_bundling() {
        // Given
        val journeyId = ""
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickCtaProductBundling(0)

        //Then
        MatcherAssert.assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }
}