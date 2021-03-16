package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.mapper.TickerMapper
import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerResponse
import com.tokopedia.sellerorder.list.domain.model.SomListOrderListResponse
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomListGetTickerUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: TickerMapper
) : BaseGraphqlUseCase<List<TickerData>>(gqlRepository) {

    override suspend fun executeOnBackground(): List<TickerData> {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): List<TickerData> {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListGetTickerResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListGetTickerResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListGetTickerResponse.Data>()
            return mapper.mapResponseToUiModel(response.orderTickers)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
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