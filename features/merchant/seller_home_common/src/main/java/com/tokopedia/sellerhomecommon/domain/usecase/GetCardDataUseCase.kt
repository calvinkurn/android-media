package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
        cardMapper: CardMapper
) : CloudAndCacheGraphqlUseCase<GetCardDataResponse, List<CardDataUiModel>>(gqlRepository, cardMapper, true, GetCardDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
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
                }
              }
            }
        """.trimIndent()
    }
}