package com.tokopedia.sellerorder.list.domain.list

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
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