package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
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
        mapper: LayoutMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetLayoutResponse, List<BaseWidgetUiModel<*>>>(
        gqlRepository, mapper, dispatchers, GetLayoutResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {
        val gqlRequest = GraphqlRequest(QUERY, GetLayoutResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GetLayoutResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLayoutResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
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