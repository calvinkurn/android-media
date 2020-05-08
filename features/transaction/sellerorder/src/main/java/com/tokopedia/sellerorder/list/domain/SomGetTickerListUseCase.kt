package com.tokopedia.sellerorder.list.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.data.model.SomListTickerParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * Created by fwidjaja on 05/05/20.
 */

class SomGetTickerListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomListTicker.Data>) {

    /*suspend fun execute(rawQuery: String, param: SomListTickerParam,
                        fromCloud: Boolean): Result<SomListTicker.Data>{
        val params = generateParam(param)

        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(rawQuery, SomListTicker.Data::class.java, params)
            useCase.addRequest(graphqlRequest)

            val hotelOrderDetail = useCase.executeSync().getSuccessData<SomListTicker.Data>().orderTickers
            return Success(hotelOrderDetail)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }*/

    /*fun execute(param: SomListTickerParam, query: String, onSuccess: (SomListTicker.Data) -> Unit, onError: (Throwable) -> Unit) {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomListTicker.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        useCase.execute({ resultSomListTicker: SomListTicker.Data ->
            onSuccess(resultSomListTicker)
        }, { throwable: Throwable ->
            onError(throwable)
        })

        return try {
            val resultTicker = useCase.executeOnBackground()
            Success(resultTicker)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }*/

    suspend fun execute(param: SomListTickerParam, query: String): Result<MutableList<SomListTicker.Data.OrderTickers.Tickers>> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomListTicker.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val resultTicker = useCase.executeOnBackground().orderTickers.listTicker.toMutableList()
            Success(resultTicker)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: SomListTickerParam): Map<String, Any?> {
        return mapOf(PARAM_INPUT to param)
    }
}