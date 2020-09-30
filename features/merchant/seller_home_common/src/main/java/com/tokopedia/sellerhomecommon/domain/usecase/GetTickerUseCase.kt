package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.TickerMapper
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 02/09/20
 */

class GetTickerUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: TickerMapper
) : BaseGqlUseCase<List<TickerItemUiModel>>() {

    override suspend fun executeOnBackground(): List<TickerItemUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetTickerResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetTickerResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTickerResponse>()
            return mapper.mapRemoteModelToUiModel(data.ticker?.tickers.orEmpty())
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        fun createParams(page: String): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_PAGE, page)
            }
        }

        private const val KEY_PAGE = "page"

        private val QUERY = """
            query (${'$'}page: String!) {
              ticker {
                tickers(page: ${'$'}page) {
                  id
                  title
                  ticker_type
                  message
                  color
                }
              }
            }
        """.trimIndent()
    }
}