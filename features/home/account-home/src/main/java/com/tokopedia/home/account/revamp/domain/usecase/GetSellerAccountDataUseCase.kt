package com.tokopedia.home.account.revamp.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.account.AccountConstants.Query.QUERY_SELLER_ACCOUNT_HOME
import com.tokopedia.home.account.AccountConstants.Query.QUERY_SHOP_LOCATION
import com.tokopedia.home.account.AccountConstants.Query.QUERY_TOP_ADS
import com.tokopedia.home.account.data.model.ShopInfoLocation
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSellerAccountDataUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase,
        rawQueries: Map<String, String>
) : UseCase<GraphqlResponse>() {

    private val querySellerAccountData = rawQueries[QUERY_SELLER_ACCOUNT_HOME] ?: ""
    private val queryTopAds = rawQueries[QUERY_TOP_ADS] ?: ""
    private val queryShopLocation = rawQueries[QUERY_SHOP_LOCATION] ?: ""

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): GraphqlResponse {
        val shopId = params[SHOP_IDS] as IntArray
        return useCase.apply {
            clearCache()
            clearRequest()
            setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            addRequests(listOf(
                    GraphqlRequest(querySellerAccountData, AccountDataModel::class.java, params),
                    createRequestParamTopAds(queryTopAds, shopId[0].toString()),
                    GraphqlRequest(queryShopLocation, ShopInfoLocation::class.java, createShopLocationRequestParam(shopId[0].toString()))
            ))
        }.executeOnBackground()
    }

    private fun createRequestParamTopAds(query: String, shopId: String): GraphqlRequest {
        val param = TopAdsGetShopDepositGraphQLUseCase.createRequestParams(query, shopId)
        return TopAdsGetShopDepositGraphQLUseCase.createGraphqlRequest(query, param)
    }

    private fun createShopLocationRequestParam(shopId: String): Map<String, Any> {
        val param = RequestParams.create()
        param.putInt(SHOP_LOCATION_ID, shopId.toIntOrNull() ?: 0)
        return param.parameters
    }

    companion object {
        const val SHOP_IDS = "shop_ids"
        const val SHOP_LOCATION_ID = "shopID"
        const val MERCHANT_ID = "merchantID"
        const val PROJECT_ID = "projectId"
    }
}