package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.test.robot.topads.TopAdsResult.assertHeadline
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxTopAdsTest : BaseUniversalInboxTest() {
    @Test
    fun should_show_headline_topads() {
        // When
        launchActivity()
        generalRobot {
            scrollToPosition(28) // End of page 1
            scrollToPosition(45)
        }

        // Then
        assertHeadline(44)
    }

    @Test
    fun should_not_show_headline_topads() {
        // Given
        GqlResponseStub.topAdsHeadlineResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(30) // End of page 1
            scrollToPosition(47)
        }

        // Then
        assertHeadline(46, true)
    }
}
