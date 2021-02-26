package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.TickerMapper
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 02/09/20
 */

class GetTickerUseCase(
        gqlRepository: GraphqlRepository,
        mapper: TickerMapper
) : CloudAndCacheGraphqlUseCase<GetTickerResponse, List<TickerItemUiModel>>(gqlRepository, mapper, true, GetTickerResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
    }

    companion object {

        fun createParams(page: String): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_PAGE, page)
            }
        }

        private const val KEY_PAGE = "page"

        private val QUERY = """
            query getTicker(${'$'}page: String!) {
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