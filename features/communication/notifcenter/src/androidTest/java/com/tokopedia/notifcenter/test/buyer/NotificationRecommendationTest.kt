package com.tokopedia.notifcenter.test.buyer

import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.filterRobot
import com.tokopedia.notifcenter.test.robot.generalRobot
import com.tokopedia.notifcenter.test.robot.recommendationResult
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotificationRecommendationTest : BaseNotificationTest() {

    @Test
    fun should_show_product_recom_when_response_is_not_empty() {
        // Given
        topAdsRepository.response = topAdsRepository.defaultResponse

        // When
        launchActivity()
        Thread.sleep(300)
        generalRobot {
            smoothScrollNotificationTo(8)
        }

        // Then
        recommendationResult {
            assertRecommendationTitleAt(7)
            assertRecommendationAt(8)
        }
    }

    @Test
    fun should_show_product_recom_when_TDN_does_not_has_ad() {
        // Given
        topAdsRepository.response = topAdsRepository.noDataResponse

        // When
        launchActivity()
        Thread.sleep(300)
        generalRobot {
            smoothScrollNotificationTo(8)
        }

        // Then
        recommendationResult {
            assertRecommendationTitleAt(6)
            assertRecommendationAt(7)
        }
    }

    @Test
    fun should_show_product_recom_when_TDN_load_is_error() {
        // Given
        topAdsRepository.isError = true

        // When
        launchActivity()
        Thread.sleep(300)
        generalRobot {
            smoothScrollNotificationTo(8)
        }

        // Then
        recommendationResult {
            assertRecommendationTitleAt(6)
            assertRecommendationAt(7)
        }
    }

    @Test
    fun should_hide_product_recom_when_user_has_notification_filter() {
        // When
        launchActivity()
        filterRobot {
            clickFilterAt(0)
        }
        Thread.sleep(300)

        // Then
        recommendationResult {
            assertNotRecommendationTitle()
        }
    }
}
