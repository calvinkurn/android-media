package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/06/20
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
            val widgetList: List<WidgetModel> = data.layout?.widget.orEmpty()
            if (widgetList.isNotEmpty()) {
                return mapper.mapRemoteModelToUiModel(widgetList)
            } else {
                throw RuntimeException("no widget found")
            }
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PAGE = "page"

        fun getRequestParams(shopId: String, pageName: String): RequestParams = RequestParams.create().apply {
            putInt(KEY_SHOP_ID, shopId.toIntOrZero())
            putString(KEY_PAGE, pageName)
        }

        private val QUERY = """
            query (${'$'}shopID: Int!, ${'$'}page: String!) {
              GetSellerDashboardPageLayout(shopID: ${'$'}shopID, page: ${'$'}page) {
                widget {
                  widgetType
                  title
                  subtitle
                  tooltip {
                    title
                    content
                    show
                    list {
                      title
                      description
                    }
                  }
                  url
                  applink
                  dataKey
                  ctaText
                }
              }
            }
        """.trimIndent()
    }
}