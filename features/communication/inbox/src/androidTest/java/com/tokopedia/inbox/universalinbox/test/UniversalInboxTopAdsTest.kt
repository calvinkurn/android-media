package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.test.robot.topAdsResult
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxTopAdsTest : BaseUniversalInboxTest() {
    @Test
    fun should_show_headline_topads() {
        // When
        launchActivity()
        generalRobot {
            scrollToPosition(20) // Reach Product Recommendation
            scrollToPosition(30) // Scroll First Page
            scrollToPosition(40) // Scroll Second Page
            scrollToPosition(50) // Scroll to Headline
        }

        // Then
        topAdsResult {
            assertHeadline(48)
        }

        // When
        generalRobot {
            scrollToPosition(65) // Scroll to 2nd Headline
        }

        // Then
        topAdsResult {
            assertHeadline(62)
        }
    }

    @Test
    fun should_not_show_headline_topads() {
        // Given
        GqlResponseStub.topAdsHeadlineResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(20) // Reach Product Recommendation
            scrollToPosition(30) // Scroll First Page
            scrollToPosition(40) // Scroll Second Page
            scrollToPosition(50) // Scroll to Headline
        }

        // Then
        topAdsResult {
            assertHeadline(47, true)
        }
    }
}
