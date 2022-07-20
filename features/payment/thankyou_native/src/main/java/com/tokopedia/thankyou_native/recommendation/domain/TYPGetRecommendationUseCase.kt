package com.tokopedia.thankyou_native.recommendation.domain

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendation.data.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.data.mapper.ProductRecommendationDataMapper
import com.tokopedia.thankyou_native.recommendation.di.qualifier.IODispatcher
import com.tokopedia.thankyou_native.recommendation.di.qualifier.RecommendationApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TYPGetRecommendationUseCase @Inject constructor(
        @RecommendationApplicationContext context: Context,
        private val mapper: dagger.Lazy<ProductRecommendationDataMapper>,
        @IODispatcher private val ioDispatcher: dagger.Lazy<CoroutineDispatcher>,
        graphqlRepository: GraphqlRepository)
    : GetRecommendationUseCase(context, graphqlRepository) {

    suspend fun getProductRecommendationData(thanksPageData: ThanksPageData): ProductRecommendationData? = withContext(ioDispatcher.get()) {
        // get product Id list for all products in each shop
        val productIdList = thanksPageData.shopOrder.map {
            it.purchaseItemList.map { pItem -> pItem.productId }
        }.flatten()
        val recommendationWidgetList = getData(GetRecommendationRequestParam(
                pageName = PAGE_NAME,
                xSource = X_SOURCE,
                productIds = productIdList))
        if (recommendationWidgetList.isNullOrEmpty())
            return@withContext null
        return@withContext mapper.get()
                .getProductRecommendationData(recommendationWidgetList.first())
    }

    companion object {
        const val X_SOURCE = "recom_widget"
        const val PAGE_NAME = "thankyou_page"
    }
}