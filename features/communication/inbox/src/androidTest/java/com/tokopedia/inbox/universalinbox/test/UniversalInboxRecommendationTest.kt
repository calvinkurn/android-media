package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.generalResult
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.test.robot.recommendationResult
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxRecommendationTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_recommendation_widget_and_recommendation_items() {
        // When
        launchActivity()
        generalRobot {
            scrollToPosition(11)
        }

        // Then
        recommendationResult {
            assertWidgetRecommendation(8) // Post Purchase
            assertWidgetRecommendation(9) // Pre Purchase
            // 10 is title product recommendation
            assertProductRecommendation(11)
        }
    }

    @Test
    fun should_not_show_recommendation_item_when_error() {
        // Given
        GqlResponseStub.productRecommendationResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(10)
        }

        // Then
        generalResult {
            assertInboxRvTotalItem(10)
        }
    }

    @Test
    fun should_not_show_postpurchase_widget_when_error() {
        // Given
        GqlResponseStub.postPurchaseProductRecommendationResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(8)
        }

        // Then
        recommendationResult {
            assertWidgetRecommendation(8)
            assertWidgetRecommendationGone(8)
        }
    }

    @Test
    fun should_not_show_prepurchase_widget_when_error() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(9, 50) // offset for post purchase
        }

        // Then
        recommendationResult {
            assertWidgetRecommendation(9)
            assertWidgetRecommendationGone(9)
        }
    }

    @Test
    fun should_not_show_postpurchase_widget_when_empty() {
        // Given
        GqlResponseStub.postPurchaseProductRecommendationResponse
            .responseObject = RecommendationEntity()

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(8)
        }

        // Then
        recommendationResult {
            assertWidgetRecommendation(8)
            assertWidgetRecommendationGone(8)
        }
    }

    @Test
    fun should_not_show_prepurchase_widget_when_empty() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse
            .responseObject = RecommendationEntity()

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(9, 50) // offset for post purchase
        }

        // Then
        recommendationResult {
            assertWidgetRecommendation(9)
            assertWidgetRecommendationGone(9)
        }
    }
}
