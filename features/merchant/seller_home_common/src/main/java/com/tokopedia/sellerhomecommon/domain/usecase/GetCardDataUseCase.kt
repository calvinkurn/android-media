package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.CardMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetCardDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

@GqlQuery("GetCardDataGqlQuery", GetCardDataUseCase.QUERY)
class GetCardDataUseCase(
    gqlRepository: GraphqlRepository,
    cardMapper: CardMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetCardDataResponse, List<CardDataUiModel>>(
    gqlRepository, cardMapper, dispatchers, GetCardDataGqlQuery()
) {

    override val classType: Class<GetCardDataResponse>
        get() = GetCardDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<CardDataUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetCardDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
            query fetchCardWidgetData(${'$'}dataKeys : [dataKey!]!) {
              fetchCardWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  value
                  state
                  description
                  descriptionSecondary
                  error
                  errorMsg
                  showWidget
                  badgeImageUrl
                }
              }
            }
        """
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            dataKey: List<String>,
            dynamicParameter: DynamicParameterModel
        ): RequestParams = RequestParams.create().apply {
            val jsonParams = dynamicParameter.toJsonString()
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it,
                    jsonParams = jsonParams
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }
}