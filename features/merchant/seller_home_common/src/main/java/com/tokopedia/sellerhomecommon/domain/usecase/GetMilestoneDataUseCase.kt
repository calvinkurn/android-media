package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetMilestoneData
import com.tokopedia.sellerhomecommon.domain.mapper.MilestoneMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.usecase.RequestParams

class GetMilestoneDataUseCase(
    private val gqlRepository: GraphqlRepository,
    milestoneMapper: MilestoneMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetMilestoneDataResponse, List<MilestoneDataUiModel>>(
    gqlRepository, milestoneMapper, dispatchers, GqlGetMilestoneData.QUERY, false
) {

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun createParams(dataKeys: List<String>): RequestParams = RequestParams.create().apply {
            val mDataKeys = dataKeys.map {
                DataKeyModel(key = it)
            }
            putObject(DATA_KEYS, mDataKeys)
        }
    }

    override val classType: Class<GetMilestoneDataResponse>
        get() = GetMilestoneDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<MilestoneDataUiModel> {
        val gqlRequest = GraphqlRequest(
            GqlGetMilestoneData, GetMilestoneDataResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(classType)
        if (gqlErrors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetMilestoneDataResponse>(classType)
            response?.let {
                val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
                return mapper.mapRemoteDataToUiData(it, isFromCache)
            }
            throw NullPointerException("milestone widget data can not be null")
        } else {
            throw RuntimeException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }
}
