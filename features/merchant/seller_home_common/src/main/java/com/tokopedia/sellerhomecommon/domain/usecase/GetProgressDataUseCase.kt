package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.ProgressMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetProgressDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetProgressDataUseCase constructor(
        private val graphqlRepository: GraphqlRepository,
        private val progressMapper: ProgressMapper
) : BaseGqlUseCase<List<ProgressDataUiModel>>() {

    override suspend fun executeOnBackground(): List<ProgressDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetProgressDataResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetProgressDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetProgressDataResponse>()
            val widgetData = data.getProgressBarData?.progressData.orEmpty()
            return progressMapper.mapResponseToUi(widgetData)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
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
            query (${'$'}dataKeys: [dataKey!]!) {
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