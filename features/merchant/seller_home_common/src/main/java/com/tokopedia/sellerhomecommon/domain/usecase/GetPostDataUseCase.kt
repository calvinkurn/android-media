package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetPostDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val postMapper: PostMapper
) : BaseGqlUseCase<List<PostListDataUiModel>>() {

    override suspend fun executeOnBackground(): List<PostListDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetPostDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetPostDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPostDataResponse>()
            val widgetDataList = data.getPostWidgetData?.data.orEmpty()
            return postMapper.mapRemoteDataModelToUiDataModel(widgetDataList)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val SHOP_ID = "shopId"
        private const val DATA_KEY = "dataKey"
        private const val START_DATE = "startDate"
        private const val END_DATE = "endDate"

        fun getRequestParams(
                shopId: Int,
                dataKey: List<String>,
                startDate: String,
                endDate: String
        ): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopId)
            putObject(DATA_KEY, dataKey)
            putString(START_DATE, startDate)
            putString(END_DATE, endDate)
        }

        private const val QUERY = """query getPostWidgetData(${'$'}dataKey: [String!]!, ${'$'}shopId: Int!, ${'$'}startDate: String!, ${'$'}endDate: String!) {
                   getPostWidgetData(dataKey: ${'$'}dataKey, shopID: ${'$'}shopId, startDate: ${'$'}startDate, endDate: ${'$'}endDate) {
                     data {
                       datakey
                       list {
                         title
                         url
                         applink
                         subtitle
                         featuredMediaURL
                       }
                       errorMsg
                     }
                   }
                }"""
    }
}