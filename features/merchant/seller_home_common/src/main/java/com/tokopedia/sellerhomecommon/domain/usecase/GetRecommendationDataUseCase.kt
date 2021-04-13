package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.RecommendationMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetRecommendationDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 06/04/21
 */

class GetRecommendationDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: RecommendationMapper
) : BaseGqlUseCase<List<RecommendationDataUiModel>>() {

    override suspend fun executeOnBackground(): List<RecommendationDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetRecommendationDataResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GetRecommendationDataResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val response: GetRecommendationDataResponse? = gqlResponse.getData<GetRecommendationDataResponse>(GetRecommendationDataResponse::class.java)
            response?.let {
                val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
                return mapper.mapRemoteModelToUiModel(it.recommendationWidgetData.data, isFromCache)
            }
            throw NullPointerException("recommendation data can not be null")
        } else {
            throw RuntimeException(gqlErrors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        private val QUERY = """
            query fetchRecommendationWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchRecommendationWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  errorMsg
                  showWidget
                  data {
                    ticker {
                      type
                      text
                    }
                    progressBar1 {
                      show
                      text
                      bar {
                        value
                        maxValue
                      }
                    }
                    progressBar2 {
                      show
                      text
                      bar {
                        value
                        maxValue
                      }
                    }
                    recommendation {
                      title
                      list {
                        text
                        applink
                        type
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()

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
}