package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class GetShopInfoUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ShopInfoPojo.Response>
) : UseCase<Result<ShopInfoPojo>>() {

    init {
        val query =
                """query getShopInfo{
                 userShopInfo{
                    info{
                        shop_name
                        shop_id
                    }
                 }
            }
            """.trimIndent()

        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(ShopInfoPojo.Response::class.java)
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<ShopInfoPojo> {
        return try{
            graphqlUseCase.setRequestParams(params.parameters)
            val data = graphqlUseCase.executeOnBackground()
            return Success(data.userShopInfo)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setStrategyCache() {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
    }

    fun setStrategyCloudThenCache() {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
    }
}