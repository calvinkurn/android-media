package com.tokopedia.notifcenter.test.buyer

import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.filterRobot
import com.tokopedia.notifcenter.test.robot.generalResult
import com.tokopedia.notifcenter.test.robot.generalRobot
import com.tokopedia.notifcenter.test.robot.topAdsResult
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotificationTDNTest : BaseNotificationTest() {
    @Test
    fun should_show_TDN_when_ad_exist() {
        // Given
        topAdsRepository.response = topAdsRepository.defaultResponse

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(6)
        }

        // Then
        topAdsResult {
            assertTopAdsBannerAt(6)
        }
    }

    @Test
    fun should_hide_TDN_when_ad_does_not_exist() {
        // Given
        topAdsRepository.response = topAdsRepository.noDataResponse

        // When
        launchActivity()

        // Then
        topAdsResult {
            assertNotTopAdsBanner()
        }
    }

    @Test
    fun should_hide_TDN_when_user_has_notification_filter() {
        // Given
        topAdsRepository.response = topAdsRepository.defaultResponse

        // When
        launchActivity()
        filterRobot {
            clickFilterAt(0)
        }

        // Then
        generalResult {
            assertItemListSize(2)
        }
        topAdsResult {
            assertNotTopAdsBanner()
        }
    }
}
