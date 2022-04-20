package com.tokopedia.topchat.chatroom.view.activity.cassava

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doScrollChatToPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickCtaProductBundling
import org.junit.Rule
import org.junit.Test

@CassavaTest
class ProductBundlingCassavaTest: TopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(true, true)

    @Test
    fun test_product_bundling() {
        val journeyId = "234"

        // Test
        track_multiple_product_bundling()
        track_single_product_bundling()
        track_click_product_bundling()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    private fun track_multiple_product_bundling() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity()

        //Close
        finishChatRoomActivity()
    }

    private fun track_single_product_bundling() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        launchChatRoomActivity()

        //Close
        finishChatRoomActivity()
    }

    private fun track_click_product_bundling() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickCtaProductBundling(0)

        //Close
        finishChatRoomActivity()
    }
}