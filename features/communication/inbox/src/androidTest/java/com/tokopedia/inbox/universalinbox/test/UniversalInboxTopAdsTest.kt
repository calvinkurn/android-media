package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralRobot.scrollToPosition
import com.tokopedia.inbox.universalinbox.test.robot.topads.TopAdsResult.assertHeadline
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxTopAdsTest : BaseUniversalInboxTest() {
    @Test
    fun should_show_headline_topads() {
        // When
        launchActivity()
        scrollToPosition(30) // End of page 1
        scrollToPosition(47)

        // Then
        assertHeadline(46)
    }

    @Test
    fun should_not_show_headline_topads() {
        // Given
        GqlResponseStub.topAdsHeadlineResponse.isError = true

        // When
        launchActivity()
        scrollToPosition(30) // End of page 1
        scrollToPosition(47)

        // Then
        assertHeadline(46, true)
    }
}
