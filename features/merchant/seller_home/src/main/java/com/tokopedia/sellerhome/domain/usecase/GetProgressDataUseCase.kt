package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.domain.mapper.ProgressMapper
import com.tokopedia.sellerhome.domain.model.ProgressDataResponse
import com.tokopedia.sellerhome.view.model.ProgressDataUiModel
import com.tokopedia.usecase.RequestParams

class GetProgressDataUseCase constructor(
        private val graphqlRepository: GraphqlRepository,
        private val progressMapper: ProgressMapper
) : BaseGqlUseCase<List<ProgressDataUiModel>>() {

    override suspend fun executeOnBackground(): List<ProgressDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, ProgressDataResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(ProgressDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<ProgressDataResponse>()
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

        const val QUERY = "query getProgressData(\$shopID: Int!, \$dataKey: [String!]!, \$date: String!) {\n" +
                "getProgressBarData(shopID: \$shopID, dataKey: \$dataKey, date: \$date){\n" +
                "    data {\n" +
                "      dataKey\n" +
                "      valueTxt\n" +
                "      maxValueTxt\n" +
                "      value\n" +
                "      maxValue\n" +
                "      state\n" +
                "      subtitle\n" +
                "      error\n" +
                "      errorMsg\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}