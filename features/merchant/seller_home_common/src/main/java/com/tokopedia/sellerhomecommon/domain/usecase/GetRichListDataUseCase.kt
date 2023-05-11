package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.RichListMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetRichListDataResponse
import com.tokopedia.sellerhomecommon.domain.model.ParamRichListWidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListDataUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

@GqlQuery("GetRichListDataGqlQuery", GetRichListDataUseCase.QUERY)
class GetRichListDataUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
    richListMapper: RichListMapper
) : CloudAndCacheGraphqlUseCase<GetRichListDataResponse, List<RichListDataUiModel>>(
    gqlRepository, richListMapper, dispatchers, GetRichListDataGqlQuery()
) {

    override val classType: Class<GetRichListDataResponse>
        get() = GetRichListDataResponse::class.java

    override suspend fun executeOnBackground(): List<RichListDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetRichListDataResponse>(classType)
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw RuntimeException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {

        internal const val QUERY = """
            query getRichListWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchRichListWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  updateInfoUnix
                  title
                  subtitle
                  headerType
                  sections {
                    type
                    caption
                    items {
                      title
                      subtitle
                      stateText
                      stateTooltip {
                        show
                        title
                        description
                        iconUrl
                      }
                      rankTrend
                      rankValue
                      rankNote
                    }
                  }
                  errorMsg
                  showWidget
                }
              }
            }
        """

        private const val DATA_KEYS = "dataKeys"

        fun createParam(
            dataKeys: List<String>,
            shopId: String,
            pageSource: String
        ): RequestParams {
            return RequestParams.create().apply {
                val dataKeyParams = dataKeys.map {
                    DataKeyModel(
                        key = it,
                        jsonParams = ParamRichListWidgetModel(
                            shopId = shopId,
                            pageSource = pageSource
                        ).toJsonString()
                    )
                }
                putObject(DATA_KEYS, dataKeyParams)
            }
        }
    }
}