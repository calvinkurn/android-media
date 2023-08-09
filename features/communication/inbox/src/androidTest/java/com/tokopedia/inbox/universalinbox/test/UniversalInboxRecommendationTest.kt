package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralResult.assertInboxRvTotalItem
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.test.robot.menuRobot
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertPrePurchaseRecommendation
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertPrePurchaseRecommendationGone
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertProductRecommendation
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertProductRecommendationName
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertProductWidgetRecommendationName
import com.tokopedia.inbox.universalinbox.test.robot.recommendationRobot
import com.tokopedia.inbox.universalinbox.test.robot.widgetRobot
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxRecommendationTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_prepurchase_widget_and_recommendation_items() {
        // When
        launchActivity()
        generalRobot {
            scrollToPosition(9)
        }

        // Then
        assertPrePurchaseRecommendation(9)
        assertProductRecommendation(11)
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
        assertInboxRvTotalItem(10)
    }

    @Test
    fun should_not_show_prepurchase_widget_when_error() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(9)
        }

        // Then
        assertPrePurchaseRecommendation(9)
        assertPrePurchaseRecommendationGone(9)
    }

    @Test
    fun should_not_show_prepurchase_widget_when_empty() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse
            .responseObject = RecommendationEntity()

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(9)
        }

        // Then
        assertPrePurchaseRecommendation(9)
        assertPrePurchaseRecommendationGone(9)
    }

    @Test
    fun should_refresh_when_coming_back_to_inbox_from_product_card_click() {
        // When
        launchActivity()
        stubAllIntents()
        generalRobot {
            scrollToPosition(11)
        }

        // Given
        GqlResponseStub.productRecommendationResponse.apply {
            filePath = "recommendation/success_get_recommendation_refresh.json"
            updateResponseObject()
        }
        GqlResponseStub.prePurchaseProductRecommendationResponse.apply {
            filePath = "recommendation/success_get_prepurchase_recommendation_refresh.json"
            updateResponseObject()
        }

        // When
        recommendationRobot {
            clickProductOnPosition(11) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(11)
        }

        // Then
        assertProductRecommendationName(11, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }

    @Test
    fun should_refresh_when_coming_back_to_inbox_from_recomm_widget_card_click() {
        // When
        launchActivity()
        stubAllIntents()
        generalRobot {
            scrollToPosition(11)
        }

        // Given
        GqlResponseStub.productRecommendationResponse.apply {
            filePath = "recommendation/success_get_recommendation_refresh.json"
            updateResponseObject()
        }
        GqlResponseStub.prePurchaseProductRecommendationResponse.apply {
            filePath = "recommendation/success_get_prepurchase_recommendation_refresh.json"
            updateResponseObject()
        }

        // When
        recommendationRobot {
            clickPrePurchaseProductOnPosition(0) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(11)
        }

        // Then
        assertProductRecommendationName(11, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }

    @Test
    fun should_refresh_when_coming_back_to_inbox_from_widget_meta_click() {
        // When
        launchActivity()
        stubAllIntents()

        // Given
        GqlResponseStub.productRecommendationResponse.apply {
            filePath = "recommendation/success_get_recommendation_refresh.json"
            updateResponseObject()
        }
        GqlResponseStub.prePurchaseProductRecommendationResponse.apply {
            filePath = "recommendation/success_get_prepurchase_recommendation_refresh.json"
            updateResponseObject()
        }

        // When
        widgetRobot {
            clickWidgetOnPosition(1) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(11)
        }

        // Then
        assertProductRecommendationName(11, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }

    @Test
    fun should_refresh_when_coming_back_to_inbox_from_menus_click() {
        // When
        launchActivity()
        stubAllIntents()

        // Given
        GqlResponseStub.productRecommendationResponse.apply {
            filePath = "recommendation/success_get_recommendation_refresh.json"
            updateResponseObject()
        }
        GqlResponseStub.prePurchaseProductRecommendationResponse.apply {
            filePath = "recommendation/success_get_prepurchase_recommendation_refresh.json"
            updateResponseObject()
        }

        // When
        menuRobot {
            clickMenuOnPosition(2) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(11)
        }

        // Then
        assertProductRecommendationName(11, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }

    @Test
    fun should_refresh_when_coming_back_to_inbox_from_icon_bell_click() {
        // When
        launchActivity()
        stubAllIntents()

        // Given
        GqlResponseStub.productRecommendationResponse.apply {
            filePath = "recommendation/success_get_recommendation_refresh.json"
            updateResponseObject()
        }
        GqlResponseStub.prePurchaseProductRecommendationResponse.apply {
            filePath = "recommendation/success_get_prepurchase_recommendation_refresh.json"
            updateResponseObject()
        }

        // When
        generalRobot {
            clickOnBell() // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(11)
        }

        // Then
        assertProductRecommendationName(11, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }
}
