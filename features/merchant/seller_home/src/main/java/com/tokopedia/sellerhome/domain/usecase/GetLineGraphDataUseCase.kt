package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhome.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhome.domain.model.GetLineGraphDataResponse
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

class GetLineGraphDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val lineGraphMapper: LineGraphMapper
) : BaseGqlUseCase<List<LineGraphDataUiModel>>() {

    override suspend fun executeOnBackground(): List<LineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetLineGraphDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetLineGraphDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLineGraphDataResponse>()
            val widgetDataList = data.getLineGraphData?.widgetData.orEmpty()
            return lineGraphMapper.mapRemoteDataModelToUiDataModel(widgetDataList)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val SHOP_ID = "shopID"
        private const val DATA_KEY = "dataKey"
        private const val START_DATE = "startDate"
        private const val END_DATE = "endDate"

        fun getRequestParams(
                shopId: String,
                dataKey: List<String>,
                startDate: String,
                endDate: String
        ): RequestParams = RequestParams.create().apply {
            putString(SHOP_ID, shopId)
            putObject(DATA_KEY, dataKey)
            putString(START_DATE, startDate)
            putString(END_DATE, endDate)
        }

        const val QUERY = "query getLineGraphData(\$shopID: String!, \$dataKey: [String!]!, \$startDate: String!, \$endDate: String!) {\n" +
                "  getLineGraphData(shopID: \$shopID, dataKey: \$dataKey, startDate: \$startDate, endDate: \$endDate) {\n" +
                "    data {\n" +
                "      dataKey\n" +
                "      header\n" +
                "      description\n" +
                "      yLabels {\n" +
                "        yVal\n" +
                "        yLabel\n" +
                "      }\n" +
                "      list {\n" +
                "        yVal\n" +
                "        yLabel\n" +
                "        xLabel\n" +
                "      }\n" +
                "      error\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}