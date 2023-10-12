package com.tokopedia.notifcenter.test.seller

import com.tokopedia.notifcenter.test.base.BaseNotificationSellerTest
import com.tokopedia.notifcenter.test.robot.filterResult
import com.tokopedia.notifcenter.test.robot.recommendationResult
import com.tokopedia.notifcenter.test.robot.topAdsResult
import org.junit.Test

class NotificationSellerTest : BaseNotificationSellerTest() {

    @Test
    fun should_hide_TDN_and_product_recom_when_user_role_is_seller() {
        // Given
        topAdsRepository.response = topAdsRepository.defaultResponse

        // When
        launchActivity()

        // Then
        topAdsResult {
            assertNotTopAdsBanner()
        }
        recommendationResult {
            assertNotRecommendationTitle()
            assertNotRecommendation()
        }
    }

    @Test
    fun should_hide_order_list_when_app_is_seller() {
        // When
        launchActivity()

        // Then
        filterResult {
            assertNotNotificationOrderList()
        }
    }
}
