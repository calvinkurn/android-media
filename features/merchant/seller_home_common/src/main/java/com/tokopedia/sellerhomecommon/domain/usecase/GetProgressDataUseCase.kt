package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhomecommon.domain.mapper.ProgressMapper
import com.tokopedia.sellerhomecommon.domain.model.GetProgressDataResponse
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
        private const val SHOP_ID = "shopID"
        private const val DATE = "date"
        private const val DATA_KEY = "dataKey"

        fun getRequestParams(
                shopId: String,
                date: String,
                dataKey: List<String>
        ): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopId.toIntOrZero())
            putString(DATE, date)
            putObject(DATA_KEY, dataKey)
        }

        private const val QUERY = """query getProgressData(${'$'}shopID: Int!, ${'$'}dataKey: [String!]!, ${'$'}date: String!) {
                 getProgressBarData(shopID: ${'$'}shopID, dataKey: ${'$'}dataKey, date: ${'$'}date) {
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
               }"""
    }
}