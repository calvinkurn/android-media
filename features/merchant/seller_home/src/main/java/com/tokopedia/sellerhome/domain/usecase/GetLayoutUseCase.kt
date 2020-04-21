package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.domain.mapper.LayoutMapper
import com.tokopedia.sellerhome.domain.model.GetLayoutResponse
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetLayoutUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: LayoutMapper
) : BaseGqlUseCase<List<BaseWidgetUiModel<*>>>() {

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {
        val gqlRequest = GraphqlRequest(QUERY, GetLayoutResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetLayoutResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLayoutResponse>()
            val widgetList = data.layout?.widget.orEmpty()
            return mapper.mapRemoteModelToUiModel(widgetList)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val SHOP_ID = "shopID"

        fun getRequestParams(shopId: String): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopId.toIntOrZero())
        }

        const val QUERY = "query GetSellerDashboardLayout(\$shopID: Int!) {\n" +
                "  GetSellerDashboardLayout(shopID: \$shopID) {\n" +
                "    widget {\n" +
                "      widgetType\n" +
                "      title\n" +
                "      subtitle\n" +
                "      tooltip {\n" +
                "        title\n" +
                "        content\n" +
                "        show\n" +
                "        list {\n" +
                "          title\n" +
                "          description\n" +
                "        }\n" +
                "      }\n" +
                "      url\n" +
                "      applink\n" +
                "      dataKey\n" +
                "      ctaText\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}