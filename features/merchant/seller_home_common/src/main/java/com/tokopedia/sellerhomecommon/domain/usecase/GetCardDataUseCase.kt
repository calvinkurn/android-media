package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.CardMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetCardDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetCardDataUseCase(
        gqlRepository: GraphqlRepository,
        cardMapper: CardMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetCardDataResponse, List<CardDataUiModel>>(
        gqlRepository, cardMapper, dispatchers, GetCardDataResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<CardDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetCardDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GetCardDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetCardDataResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
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

        private val QUERY = """
            query getCardWidgetData(${'$'}dataKeys : [dataKey!]!) {
              fetchCardWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  value
                  state
                  description
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """.trimIndent()
    }
}