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
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.ROLLENCE_REFRESH_RECOMMENDATION
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
            scrollToPosition(7)
        }

        // Then
        assertPrePurchaseRecommendation(7)
        assertProductRecommendation(9)
    }

    @Test
    fun should_not_show_recommendation_item_when_error() {
        // Given
        GqlResponseStub.productRecommendationResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(8)
        }

        // Then
        assertInboxRvTotalItem(8)
    }

    @Test
    fun should_not_show_prepurchase_widget_when_error() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse.isError = true

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(7)
        }

        // Then
        assertPrePurchaseRecommendation(7)
        assertPrePurchaseRecommendationGone(7)
    }

    @Test
    fun should_not_show_prepurchase_widget_when_empty() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse
            .responseObject = RecommendationEntity()

        // When
        launchActivity()
        generalRobot {
            scrollToPosition(7)
        }

        // Then
        assertPrePurchaseRecommendation(7)
        assertPrePurchaseRecommendationGone(7)
    }

    @Test
    fun should_refresh_when_coming_back_to_inbox_from_product_card_click() {
        // When
        launchActivity()
        stubAllIntents()
        generalRobot {
            scrollToPosition(9)
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
            clickProductOnPosition(9) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(9)
        }

        // Then
        assertProductRecommendationName(9, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }

    /**
     * This function was intentionally commented out because the home team hasn't provided support yet.
     * The function will be reactivated once the home team has completed their part.
     */
//    @Test
//    fun should_refresh_when_coming_back_to_inbox_from_recomm_widget_card_click() {
//        // When
//        launchActivity()
//        stubAllIntents()
//        generalRobot {
//            scrollToPosition(9)
//        }
//
//        // Given
//        GqlResponseStub.productRecommendationResponse.apply {
//            filePath = "recommendation/success_get_recommendation_refresh.json"
//            updateResponseObject()
//        }
//        GqlResponseStub.prePurchaseProductRecommendationResponse.apply {
//            filePath = "recommendation/success_get_prepurchase_recommendation_refresh.json"
//            updateResponseObject()
//        }
//
//        // When
//        recommendationRobot {
//            clickPrePurchaseProductOnPosition(0) // trigger refresh
//        }
//        Thread.sleep(300) // wait for rv to populate
//        generalRobot {
//            scrollToPosition(9)
//        }
//
//        // Then
//        assertProductRecommendationName(9, "Product Refresh 1")
//        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
//    }

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
            scrollToPosition(9)
        }

        // Then
        assertProductRecommendationName(9, "Product Refresh 1")
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
            clickMenuOnPosition(1) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(9)
        }

        // Then
        assertProductRecommendationName(9, "Product Refresh 1")
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
            scrollToPosition(9)
        }

        // Then
        assertProductRecommendationName(9, "Product Refresh 1")
        assertProductWidgetRecommendationName(0, "Pre-purchase Product Refresh")
    }

    @Test
    fun should_not_refresh_when_coming_back_to_inbox_if_rollence_is_off() {
        // When
        setABValue(ROLLENCE_REFRESH_RECOMMENDATION, "")
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
            clickMenuOnPosition(1) // trigger refresh
        }
        Thread.sleep(300) // wait for rv to populate
        generalRobot {
            scrollToPosition(9)
        }

        // Then
        assertProductRecommendationName(
            9,
            "Tumbler Japan Hook Termos Travel 500 ml"
        )
        assertProductWidgetRecommendationName(
            0,
            "Celana Chino Panjang Pria Cinos Slim fit Jumbo Katun Twill Adem Casual"
        )

        // Clean-up
        setABValue(ROLLENCE_REFRESH_RECOMMENDATION, ROLLENCE_REFRESH_RECOMMENDATION)
    }
}
