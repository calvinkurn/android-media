package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.mapper.ProgressMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetProgressDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetProgressDataUseCase constructor(
        graphqlRepository: GraphqlRepository,
        progressMapper: ProgressMapper
) : CloudAndCacheGraphqlUseCase<GetProgressDataResponse, List<ProgressDataUiModel>>(graphqlRepository, progressMapper, true, GetProgressDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
                date: String,
                dataKey: List<String>
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it,
                        jsonParams = DynamicParameterModel(date = date).toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }

        private val QUERY = """
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
                }
              }
            }
        """.trimIndent()
    }
}