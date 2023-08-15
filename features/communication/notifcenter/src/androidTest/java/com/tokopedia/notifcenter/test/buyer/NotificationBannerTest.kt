package com.tokopedia.notifcenter.test.buyer

import com.tokopedia.notifcenter.stub.common.getNewExpiredTime
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.detailResult
import com.tokopedia.notifcenter.test.robot.generalRobot
import org.junit.Test

class NotificationBannerTest : BaseNotificationTest() {
    @Test
    fun should_show_expired_date() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_banner_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            for (i in 0 until it.notifcenterDetail.newList.size) {
                it.notifcenterDetail.newList.forEach { item ->
                    item.expireTimeUnix = getNewExpiredTime(THREE_DAYS)
                }
            }
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(3)
        }

        // Then
        detailResult {
            // Non expired banner
            assertNotificationTypeBanner(2)
            assertNotificationBannerTitle(2, "Banner Pertama")
            assertNotificationBannerExpiredDate(2, false)
        }
        detailResult {
            // Expired banner
            assertNotificationTypeBanner(3)
            assertNotificationBannerTitle(3, "Banner Kedua")
            assertNotificationBannerExpiredDate(3, true)
        }
    }

    @Test
    fun should_not_show_countdown() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_banner_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            for (i in 0 until it.notifcenterDetail.newList.size) {
                it.notifcenterDetail.newList.forEach { item ->
                    item.expireTimeUnix = getNewExpiredTime(THREE_HOURS)
                }
            }
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(3)
        }

        // Then
        detailResult {
            // Non show countdown
            assertNotificationTypeBanner(2)
            assertNotificationBannerTitle(2, "Banner Pertama")
            assertNotificationBannerExpiredDate(2, false)
        }
        detailResult {
            // Show countdown
            assertNotificationTypeBanner(3)
            assertNotificationBannerTitle(3, "Banner Kedua")
            assertNotificationBannerCountDown(3, true)
        }
    }
}
