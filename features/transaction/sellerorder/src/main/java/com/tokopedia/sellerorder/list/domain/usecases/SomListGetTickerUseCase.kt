package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.mapper.TickerMapper
import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerResponse
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomListGetTickerUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomListGetTickerResponse.Data>,
        private val mapper: TickerMapper
) : BaseGraphqlUseCase() {

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(): Success<List<TickerData>> {
        useCase.setTypeClass(SomListGetTickerResponse.Data::class.java)
        useCase.setRequestParams(params.parameters)

        val result = useCase.executeOnBackground()
        return Success(mapper.mapResponseToUiModel(result.orderTickers))
    }

    fun setParam(param: SomListGetTickerParam) {
        params = RequestParams.create().apply {
            putObject(SomConsts.PARAM_INPUT, param)
        }
    }

    companion object {
        val QUERY = """
            query GetOrderTickers(${'$'}input: OrderTickersArgs!) {
              orderTickers(input: ${'$'}input) {
                total
                tickers {
                  id
                  body
                  short_desc
                  is_active
                }
              }
            }
        """.trimIndent()
    }
}