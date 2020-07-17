package com.tokopedia.home.account.revamp.domain

import com.tokopedia.design.utils.StringUtils
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.account.AccountConstants.Query.NEW_QUERY_SALDO_BALANCE
import com.tokopedia.home.account.AccountConstants.Query.QUERY_SELLER_ACCOUNT_HOME
import com.tokopedia.home.account.AccountConstants.Query.QUERY_SHOP_LOCATION
import com.tokopedia.home.account.AccountConstants.Query.QUERY_TOP_ADS
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.data.model.ShopInfoLocation
import com.tokopedia.navigation_common.model.SaldoModel
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
    private val querySaldoBalance = rawQueries[NEW_QUERY_SALDO_BALANCE] ?: ""
    private val queryShopLocation = rawQueries[QUERY_SHOP_LOCATION] ?: ""

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): GraphqlResponse {
        val shopId: String = params[SHOP_ID].toString()
        return useCase.apply {
            clearCache()
            clearRequest()
            setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            addRequests(listOf(
                    GraphqlRequest(querySellerAccountData, AccountModel::class.java, params),
                    createRequestParamTopAds(queryTopAds, shopId),
                    GraphqlRequest(querySaldoBalance, SaldoModel::class.java),
                    GraphqlRequest(queryShopLocation, ShopInfoLocation::class.java, createShopLocationRequestParam(shopId))
            ))
        }.executeOnBackground()
    }

    private fun createRequestParamTopAds(query: String, shopId: String): GraphqlRequest {
        val param = TopAdsGetShopDepositGraphQLUseCase.createRequestParams(query, shopId)
        return TopAdsGetShopDepositGraphQLUseCase.createGraphqlRequest(query, param)
    }

    private fun createShopLocationRequestParam(shopId: String): Map<String, Any> {
        val param = RequestParams.create()
        val shopIdInt = if (StringUtils.isNotBlank(shopId)) shopId.toInt() else 0
        param.putInt("shopID", shopIdInt)
        return param.parameters
    }

    companion object {
        const val SHOP_ID = "shop_id"
    }
}