package com.tokopedia.product.estimasiongkir.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.data.model.ScheduledDeliveryRatesModel
import com.tokopedia.product.estimasiongkir.di.RatesEstimationScope
import javax.inject.Inject

@RatesEstimationScope
class GetScheduledDeliveryRatesUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ScheduledDeliveryRatesModel.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(GetScheduledDeliveryRatesQuery)
        setTypeClass(ScheduledDeliveryRatesModel.Response::class.java)
    }

    suspend fun execute(
        request: RatesEstimateRequest,
        uniqueId: String,
        forceRefresh: Boolean
    ): ScheduledDeliveryRatesModel {
        setRequestParams(
            GetScheduledDeliveryRatesQuery.createParams(
                origin = request.origin ?: "",
                destination = request.destination,
                warehouseId = request.warehouseId.toLongOrZero(),
                weight = request.productWeight.toString(),
                shopId = request.shopId.toLongOrZero(),
                uniqueId = uniqueId,
                productMetadata = request.productMetadata,
                boMetadata = request.boMetadata,
                orderValue = request.orderValue,
                categoryId = request.categoryId
            )
        )
        setCacheStrategy(getCacheStrategy(forceRefresh))
        return executeOnBackground().data.data
    }

    private fun getCacheStrategy(forceRefresh: Boolean): GraphqlCacheStrategy {
        val cacheType = if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST

        return GraphqlCacheStrategy.Builder(cacheType)
            .setSessionIncluded(false)
            .build()
    }
}
