package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class GetLayoutUseCase(
        gqlRepository: GraphqlRepository,
        mapper: LayoutMapper
) : CloudAndCacheGraphqlUseCase<GetLayoutResponse, List<BaseWidgetUiModel<*>>>(gqlRepository, mapper, true, GetLayoutResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
    }

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PAGE = "page"

        fun getRequestParams(shopId: String, pageName: String): RequestParams = RequestParams.create().apply {
            putInt(KEY_SHOP_ID, shopId.toIntOrZero())
            putString(KEY_PAGE, pageName)
        }

        private val QUERY = """
            query GetSellerDashboardLayout(${'$'}shopID: Int!, ${'$'}page: String!) {
              GetSellerDashboardPageLayout(shopID: ${'$'}shopID, page: ${'$'}page) {
                widget {
                  ID
                  widgetType
                  title
                  subtitle
                  comparePeriode
                  tooltip {
                    title
                    content
                    show
                    list {
                      title
                      description
                    }
                  }
                  showEmpty
                  postFilter {
                    name
                    value
                  }
                  url
                  applink
                  dataKey
                  ctaText
                  emptyState {
                    imageUrl
                    title
                    description
                    ctaText
                    applink
                  }
                }
              }
            }
        """.trimIndent()
    }
}