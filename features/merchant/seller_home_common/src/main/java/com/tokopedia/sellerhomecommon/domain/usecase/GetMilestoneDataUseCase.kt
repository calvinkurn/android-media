package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.domain.mapper.MilestoneMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.domain.model.GetRewardDetailByIdResponse
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel
import com.tokopedia.usecase.RequestParams

@GqlQuery("GetMilestoneDataGqlQuery", GetMilestoneDataUseCase.QUERY)
class GetMilestoneDataUseCase(
    private val gqlRepository: GraphqlRepository,
    private val getRewardDetailByIdUseCase: GetRewardDetailByIdUseCase,
    milestoneMapper: MilestoneMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetMilestoneDataResponse, List<MilestoneDataUiModel>>(
    gqlRepository,
    milestoneMapper,
    dispatchers,
    GetMilestoneDataGqlQuery()
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
                val questStatus = it.fetchMilestoneWidgetData?.data?.firstOrNull()?.questStatus
                val rewardId = it.fetchMilestoneWidgetData?.data?.firstOrNull()?.reward?.rewardId.orZero()
                val shouldLoadRewardDetail =
                    questStatus == MilestoneItemRewardUiModel.QuestStatus.NOT_STARTED_OR_ONGOING && !rewardId.isZero()
                return if (shouldLoadRewardDetail) {
                    val rewardDetail = getRewardDetailById(rewardId)
                    mapper.mapRemoteDataToUiData(it, isFromCache, REWARD_KEY to rewardDetail)
                } else {
                    mapper.mapRemoteDataToUiData(it, isFromCache)
                }
            }
            throw NullPointerException("milestone widget data can not be null")
        } else {
            throw RuntimeException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    private suspend fun getRewardDetailById(rewardId: Long): GetRewardDetailByIdResponse {
        return getRewardDetailByIdUseCase.execute(rewardId)
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
                  questStatus
                  reward {
                    rewardID
                    hasReward
                    rewardStatus
                    rewardImg
                    rewardTitle
                    rewardSubtitle
                    button {
                      buttonStatus
                      buttonStyleType
                      title
                      url
                      urlType
                      applink
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

        const val REWARD_KEY = "reward"

        fun createParams(dataKeys: List<String>): RequestParams = RequestParams.create().apply {
            val mDataKeys = dataKeys.map {
                DataKeyModel(key = it)
            }
            putObject(DATA_KEYS, mDataKeys)
        }
    }
}
