package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetRecommendationData
import com.tokopedia.sellerhomecommon.domain.mapper.RecommendationMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetRecommendationDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 06/04/21
 */

class GetRecommendationDataUseCase(
    private val gqlRepository: GraphqlRepository,
    recommendationMapper: RecommendationMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetRecommendationDataResponse, List<RecommendationDataUiModel>>(
    gqlRepository, recommendationMapper, dispatchers, GqlGetRecommendationData.QUERY, false
) {

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun createParams(dataKey: List<String>): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it,
                    jsonParams = "{}"
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }

    override val classType: Class<GetRecommendationDataResponse>
        get() = GetRecommendationDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<RecommendationDataUiModel> {
        val gqlRequest =
            GraphqlRequest(GqlGetRecommendationData, classType, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GetRecommendationDataResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val response: GetRecommendationDataResponse? =
                gqlResponse.getData<GetRecommendationDataResponse>(classType)
            response?.let {
                val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
                return mapper.mapRemoteDataToUiData(it, isFromCache)
            }
            throw NullPointerException("recommendation data can not be null")
        } else {
            throw RuntimeException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }
}