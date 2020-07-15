package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.PostMapper
import com.tokopedia.centralizedpromo.view.model.PostListUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.GetPostDataResponse
import com.tokopedia.sellerhome.domain.usecase.BaseGqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val postMapper: PostMapper
) : BaseGqlUseCase<PostListUiModel>() {

    override suspend fun executeOnBackground(): PostListUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetPostDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetPostDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPostDataResponse>()
            val widgetDataList = data.getPostWidgetData?.data.orEmpty()
            return postMapper.mapDomainDataModelToUiDataModel(widgetDataList).firstOrNull()
                    ?: PostListUiModel(emptyList(), "")
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val DATA_KEY_SELLER_INFO_POST = "sellerInfo"

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

        const val QUERY = "query getPostWidgetData(\$dataKey: [String!]!, \$shopId: Int!, \$startDate: String!, \$endDate: String!) {\n" +
                "  getPostWidgetData(dataKey: \$dataKey, shopID: \$shopId, startDate: \$startDate, endDate: \$endDate) {\n" +
                "    data {\n" +
                "      datakey\n" +
                "      list {\n" +
                "        title\n" +
                "        url\n" +
                "        applink\n" +
                "        subtitle\n" +
                "        featuredMediaURL\n" +
                "      }\n" +
                "      errorMsg\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}