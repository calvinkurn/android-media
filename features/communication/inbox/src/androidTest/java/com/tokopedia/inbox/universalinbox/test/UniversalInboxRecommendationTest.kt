package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralResult.assertInboxRvTotalItem
import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralRobot.scrollToPosition
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertPrePurchaseRecommendation
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertPrePurchaseRecommendationGone
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertProductRecommendation
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxRecommendationTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_recommendation_items() {
        // When
        launchActivity()
        scrollToPosition(11)

        // Then
        assertProductRecommendation(11)
    }

    @Test
    fun should_not_show_recommendation_item_when_error() {
        // Given
        GqlResponseStub.productRecommendationResponse.isError = true

        // When
        launchActivity()
        scrollToPosition(10)

        // Then
        assertInboxRvTotalItem(10)
    }

    @Test
    fun should_show_prepurchase_widget() {
        // When
        launchActivity()
        scrollToPosition(9)

        // Then
        assertPrePurchaseRecommendation(9)
    }

    @Test
    fun should_not_show_prepurchase_widget_when_error() {
        // Given
        GqlResponseStub.prePurchaseProductRecommendationResponse.isError = true

        // When
        launchActivity()
        scrollToPosition(9)

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
        scrollToPosition(9)

        // Then
        assertPrePurchaseRecommendation(9)
        assertPrePurchaseRecommendationGone(9)
    }
}
