package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetTickerData
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
    gqlRepository, mapper, dispatchers, GqlGetTickerData.QUERY, false
) {

    companion object {
        private const val KEY_PAGE = "page"

        fun createParams(page: String): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_PAGE, page)
            }
        }
    }

    override val classType: Class<GetTickerResponse>
        get() = GetTickerResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<TickerItemUiModel> {
        val gqlRequest = GraphqlRequest(GqlGetTickerData, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetTickerResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message)
        }
    }
}