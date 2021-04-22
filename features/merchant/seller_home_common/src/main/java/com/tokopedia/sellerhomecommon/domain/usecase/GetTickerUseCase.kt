package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.TickerMapper
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 02/09/20
 */

class GetTickerUseCase(
        gqlRepository: GraphqlRepository,
        mapper: TickerMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetTickerResponse, List<TickerItemUiModel>>(
        gqlRepository, mapper, dispatchers, GetTickerResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<TickerItemUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetTickerResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(GetTickerResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTickerResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
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