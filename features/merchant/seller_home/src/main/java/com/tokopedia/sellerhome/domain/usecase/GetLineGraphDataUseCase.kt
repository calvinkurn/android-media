package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhome.GraphqlQuery
import com.tokopedia.sellerhome.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhome.domain.model.LineGraphDataResponse
import com.tokopedia.sellerhome.util.getData
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-27
 */

class GetLineGraphDataUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        private val lineGraphMapper: LineGraphMapper
) : UseCase<List<LineGraphDataUiModel>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<LineGraphDataUiModel> {
        val gqlRequest = GraphqlRequest(GraphqlQuery.GET_LINE_GRAPH_DATA, LineGraphDataResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val gqlResponse: GraphqlResponse = graphqlUseCase.executeOnBackground()

        val errors: List<GraphqlError>? = gqlResponse.getError(LineGraphDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<LineGraphDataResponse>()
            val widgetDataList = data.responseData?.getLineGraphData?.widgetData.orEmpty()
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
    }
}