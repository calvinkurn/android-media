package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetShopInfoQuery
import com.tokopedia.searchbar.navigation_component.data.notification.Param
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetShopInfoUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ShopData>,
        userSession: UserSessionInterface
) : UseCase<Result<ShopData>>() {

    var params: RequestParams = RequestParams.EMPTY

    init {
        graphqlUseCase.setGraphqlQuery(GetShopInfoQuery())
        graphqlUseCase.setTypeClass(ShopData::class.java)
        params.parameters[PARAM_INPUT] = Param(userSession.shopId)
    }

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
                        .setExpiryTime(EXPIRY_TIMES_MULTIPLIER * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }

    fun setStrategyCloudThenCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(EXPIRY_TIMES_MULTIPLIER * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val EXPIRY_TIMES_MULTIPLIER = 5
    }
}
