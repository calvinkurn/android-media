package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.ProgressMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetProgressDataResponse
import com.tokopedia.sellerhomecommon.domain.model.ParamProgressWidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

@GqlQuery("GetProgressDataGqlQuery", GetProgressDataUseCase.QUERY)
class GetProgressDataUseCase constructor(
    graphqlRepository: GraphqlRepository,
    progressMapper: ProgressMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetProgressDataResponse, List<ProgressDataUiModel>>(
    graphqlRepository, progressMapper, dispatchers, GetProgressDataGqlQuery()
) {

    override val classType: Class<GetProgressDataResponse>
        get() = GetProgressDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<ProgressDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetProgressDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
            query getProgressData(${'$'}dataKeys: [dataKey!]!) {
              fetchProgressBarWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  valueTxt
                  maxValueTxt
                  value
                  maxValue
                  state
                  subtitle
                  error
                  errorMsg
                  showWidget
                  updateInfo
                }
              }
            }
        """
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            date: String,
            dataKey: List<String>
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it,
                    jsonParams = ParamProgressWidgetModel(date = date).toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}
