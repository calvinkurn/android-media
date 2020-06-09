package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShippingStatus
import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShippingStatus.*
import com.tokopedia.shop.common.data.source.cloud.query.GetShopFreeShippingStatus
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetShopFreeShippingStatusUseCase@Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopFreeShippingStatus>(graphqlRepository) {

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_SHOP_ID = "shopIDs"

        fun createRequestParams(shopIds: List<Int>): RequestParams {
            return RequestParams().apply {
                putObject(PARAM_SHOP_ID, shopIds)
            }
        }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetShopFreeShippingStatus.QUERY)
        setTypeClass(ShopFreeShippingStatus::class.java)
    }

    suspend fun execute(requestParams: RequestParams): List<Shop> {
        val userId = userSession.userId.toInt()
        requestParams.putInt(PARAM_USER_ID, userId)
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response.shops
    }
}