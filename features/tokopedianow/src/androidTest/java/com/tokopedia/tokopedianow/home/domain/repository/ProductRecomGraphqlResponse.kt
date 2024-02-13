package com.tokopedia.tokopedianow.home.domain.repository

import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.tokopedianow.test.R as tokopedianowtestR

object ProductRecomGraphqlResponse {

    val getProductRecommendationResponse = GqlMockUtil
        .createSuccessResponse<RecommendationEntity>(tokopedianowtestR.raw.get_product_recommendation_response)

    val getProductRecommendationTelurResponse = GqlMockUtil
        .createSuccessResponse<RecommendationEntity>(tokopedianowtestR.raw.get_product_recommendation_telur_response)
}
