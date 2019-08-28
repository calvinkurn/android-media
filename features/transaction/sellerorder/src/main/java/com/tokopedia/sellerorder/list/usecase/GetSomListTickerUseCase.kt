package com.tokopedia.sellerorder.list.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.data.model.SomListTickerParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-27.
 */
class GetSomListTickerUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    fun createRequestParam(requestBy: String, client: String): SomListTickerParam {
        val somListTickerParam = SomListTickerParam()
        somListTickerParam.requestBy = requestBy
        somListTickerParam.client = client
        return somListTickerParam
    }

    suspend fun execute(rawQuery: String, requestBy: String, client: String,
                        fromCloud: Boolean = true): Result<MutableList<SomListTicker.Data.OrderTickers.Tickers>> {
        val requestParams = createRequestParam(requestBy, client)
        val params = mapOf(PARAM_SOM_LIST to requestParams)

        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, SomListTicker.Data::class.java, params)
            useCase.addRequest(graphqlRequest)

            val dataOrderTickers = useCase.executeOnBackground().getSuccessData<SomListTicker.Data>().orderTickers
            Success(dataOrderTickers.listTicker.toMutableList())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val PARAM_SOM_LIST = "input"
        const val INPUT_SELLER = "seller"
        const val INPUT_CLIENT = "desktop"
    }
}