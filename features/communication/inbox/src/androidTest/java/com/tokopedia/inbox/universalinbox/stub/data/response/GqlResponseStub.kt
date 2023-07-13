package com.tokopedia.inbox.universalinbox.stub.data.response

import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxCounterWrapperResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetWrapperResponse
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse

object GqlResponseStub {

    lateinit var counterResponse: ResponseStub<UniversalInboxCounterWrapperResponse>
    lateinit var widgetMetaResponse: ResponseStub<UniversalInboxWidgetWrapperResponse>
    lateinit var topAdsHeadlineResponse: ResponseStub<TopAdsHeadlineResponse>
    lateinit var productRecommendationResponse: ResponseStub<RecommendationEntity>
    lateinit var prePurchaseProductRecommendationResponse: ResponseStub<RecommendationEntity>

    init {
        reset()
    }

    fun reset() {
        counterResponse = ResponseStub(
            filePath = "counter/success_get_counter.json",
            type = UniversalInboxCounterWrapperResponse::class.java,
            query = "notifications",
            isError = false
        )

        widgetMetaResponse = ResponseStub(
            filePath = "widget/success_get_widget_meta.json",
            type = UniversalInboxWidgetWrapperResponse::class.java,
            query = "chatInboxWidgetMeta",
            isError = false
        )

        topAdsHeadlineResponse = ResponseStub(
            filePath = "topads/success_get_headline.json",
            type = TopAdsHeadlineResponse::class.java,
            query = "displayAdsV3",
            isError = false
        )

        productRecommendationResponse = ResponseStub(
            filePath = "recommendation/success_get_recommendation.json",
            type = RecommendationEntity::class.java,
            query = "productRecommendationWidget",
            isError = false
        )

        prePurchaseProductRecommendationResponse = ResponseStub(
            filePath = "recommendation/success_get_prepurchase_recommendation.json",
            type = RecommendationEntity::class.java,
            query = "productRecommendationWidget(userID",
            isError = false
        )
    }
}
