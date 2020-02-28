package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhomedrawer.data.SellerHomeStatus
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class FlashSaleGetSellerStatusUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase): UseCase<Boolean>() {

    companion object {
        private const val PARAM_QUERY = "query"
        private const val PARAM_SHOP_ID = "shop_id"

        @JvmStatic
        fun createRequestParams(query: String, shopId: String)= RequestParams.create().apply {
            putInt(PARAM_SHOP_ID, shopId.toInt())
            putString(PARAM_QUERY, query)
        }
    }

    var isCached = false

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val query = requestParams.getString(PARAM_QUERY, "")
        requestParams.clearValue(PARAM_QUERY)


        val graphqlRequest = com.tokopedia.graphql.data.model.GraphqlRequest(query, SellerHomeStatus.Response::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        if (isCached){
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`())
                    .setSessionIncluded(true)
                    .build()
            graphqlUseCase.setCacheStrategy(graphqlCacheStrategy)
        } else {
            graphqlUseCase.setCacheStrategy(null)
        }

        return graphqlUseCase.createObservable(null).flatMap { graphqlResponse ->
            val errors = graphqlResponse.getError(SellerHomeStatus.Response::class.java)

            if (errors != null && !errors.isEmpty()){
                Observable.error(MessageErrorException(errors.joinToString(", ")))
            } else {
                val response = graphqlResponse.getData<SellerHomeStatus.Response>(SellerHomeStatus.Response::class.java).getCampaignSellerStatus.sellerStatus
                Observable.just(response)
            }
        }.map { it.isVisible }
    }
}