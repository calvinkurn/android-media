package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.MilestoneMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.usecase.RequestParams

class GetMilestoneDataUseCase(
    private val gqlRepository: GraphqlRepository,
    private val mapper: MilestoneMapper
): BaseGqlUseCase<List<MilestoneDataUiModel>>() {

    override suspend fun executeOnBackground(): List<MilestoneDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetMilestoneDataResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GetMilestoneDataResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val response: GetMilestoneDataResponse? = gqlResponse.getData<GetMilestoneDataResponse>(
                GetMilestoneDataResponse::class.java)
            response?.let {
                val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
                return mapper.mapMilestoneResponseToUiModel(it.fetchMilestoneWidgetData?.data.orEmpty(), isFromCache)
            } ?: throw NullPointerException("milestone widget data can not be null")
        } else {
            throw RuntimeException(gqlErrors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"
        private const val SHOP_QUEST_KEY = "shopQuest"
        private val QUERY = """
            query fetchMilestoneWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchMilestoneWidgetData(dataKeys: ${'$'}dataKeys) {
                data{
                    dataKey
                    title
                    subtitle
                    backgroundColor
                    backgroundImageUrl
                    showNumber
                    progressBar{
                      description
                      percentage
                      percentageFormatted
                      taskCompleted
                      totalTask
                    }
                    mission{
                      imageUrl
                      title
                      subtitle
                      missionCompletionStatus
                      button{
                        title
                        urlType
                        url
                        applink
                        buttonStatus
                      }
                    }
                    finishMission{
                      imageUrl
                      title
                      subtitle
                      button{
                        title
                        urlType
                        url
                        applink
                        buttonStatus
                      }
                    }
                    cta{
                      text
                      url
                      applink
                    }
                    error
                    errorMsg
                    showWidget
                  }
                }
              }
            }
        """.trimIndent()

        fun createParams(dataKeys: List<String>): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKeys.map {
                DataKeyModel(
                    key = it,
                    jsonParams = "{}"
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}