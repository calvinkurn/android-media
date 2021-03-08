package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopInfoUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ShopData>
) : UseCase<Result<ShopData>>() {

    init {
        val query =
                """query getShopInfo{
                 userShopInfo{
                    info{
                        shop_name
                        shop_id
                    }
                 }
                 notifications {
                    sellerOrderStatus {
                        newOrder
                        readyToShip
                        inResolution
                    }
                 }
            }
            """.trimIndent()

        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(ShopData::class.java)
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<ShopData> {
        return try{
            graphqlUseCase.setRequestParams(params.parameters)
            val data = graphqlUseCase.executeOnBackground()
            return Success(data)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setStrategyCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }

    fun setStrategyCloudThenCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }
}