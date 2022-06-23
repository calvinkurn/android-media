package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.MilestoneMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.usecase.RequestParams

@GqlQuery("GetMilestoneDataGqlQuery", GetMilestoneDataUseCase.QUERY)
class GetMilestoneDataUseCase(
    private val gqlRepository: GraphqlRepository,
    milestoneMapper: MilestoneMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetMilestoneDataResponse, List<MilestoneDataUiModel>>(
    gqlRepository, milestoneMapper, dispatchers, GetMilestoneDataGqlQuery()
) {

    override val classType: Class<GetMilestoneDataResponse>
        get() = GetMilestoneDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<MilestoneDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
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

    companion object {
        internal const val QUERY = """
            query fetchMilestoneWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchMilestoneWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  title
                  subtitle
                  backgroundColor
                  backgroundImageUrl
                  showNumber
                  timeDeadline
                  progressBar {
                    description
                    percentage
                    percentageFormatted
                    taskCompleted
                    totalTask
                  }
                  mission {
                    imageUrl
                    title
                    subtitle
                    missionCompletionStatus
                    progress {
                      appDescription
                      percentage
                      completed
                      target
                    }
                    button {
                      title
                      urlType
                      url
                      applink
                      buttonStatus
                    }
                  }
                  finishMission {
                    imageUrl
                    title
                    subtitle
                    button {
                      title
                      urlType
                      url
                      applink
                      buttonStatus
                    }
                  }
                  cta {
                    text
                    applink
                  }
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """
        private const val DATA_KEYS = "dataKeys"

        fun createParams(dataKeys: List<String>): RequestParams = RequestParams.create().apply {
            val mDataKeys = dataKeys.map {
                DataKeyModel(key = it)
            }
            putObject(DATA_KEYS, mDataKeys)
        }
    }
}
