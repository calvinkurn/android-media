package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse
import com.tokopedia.shop.common.data.source.cloud.query.GetShopInfoTopAds
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoTopAdsUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<ShopInfoTopAdsResponse>(gqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetShopInfoTopAds.QUERY)
        setTypeClass(ShopInfoTopAdsResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): ShopInfoTopAdsResponse {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"

        fun createRequestParams(shopId: Int): RequestParams {
            return RequestParams().apply {
                putInt(PARAM_SHOP_ID, shopId)
            }
        }
    }
}