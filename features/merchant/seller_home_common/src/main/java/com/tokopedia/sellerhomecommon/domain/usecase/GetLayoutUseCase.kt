package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.presentation.model.WidgetLayoutUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/06/20
 */

@GqlQuery("GetLayoutGqlQuery", GetLayoutUseCase.QUERY)
class GetLayoutUseCase(
    gqlRepository: GraphqlRepository,
    mapper: LayoutMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetLayoutResponse, WidgetLayoutUiModel>(
    gqlRepository,
    mapper,
    dispatchers,
    GetLayoutGqlQuery()
) {

    override val classType: Class<GetLayoutResponse>
        get() = GetLayoutResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): WidgetLayoutUiModel {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.response(
            listOf(gqlRequest),
            cacheStrategy
        )

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLayoutResponse>(GetLayoutResponse::class.java)
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            val filterPage = params.parameters[KEY_PAGE]
            val tableSort = customExtras[KEY_TABLE_SORT]
            return mapper.mapRemoteDataToUiData(
                data,
                isFromCache,
                mapOf(
                    KEY_PAGE to filterPage,
                    KEY_TABLE_SORT to tableSort
                )
            )
        } else {
            throw RuntimeException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
            query GetSellerDashboardLayout(${'$'}shopID: Int!, ${'$'}page: String!, ) {
              GetSellerDashboardPageLayout(shopID: ${'$'}shopID, page: ${'$'}page) {
                widget {
                  ID
                  sectionID
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
                  tag
                  showEmpty
                  postFilter {
                    name
                    value
                  }
                  url
                  applink
                  dataKey
                  ctaText
                  gridSize
                  maxData
                  maxDisplay
                  emptyState {
                    imageUrl
                    title
                    description
                    ctaText
                    applink
                  }
                  searchTableColumnFilter{
                    name
                    value
                  }
                  isDismissible
                  dismissibleState
                  useRealtime
                }
                tabs {
                  tabTitle
                  tabName 
                  page
                  tag
                }
                shopState
                personaStatus
              }
            }
        """
        private const val KEY_SHOP_ID = "shopID"
        const val KEY_PAGE = "page"
        const val KEY_TABLE_SORT = "table_sort"
        private const val KEY_EVENT = "event"

        fun getRequestParams(
            shopId: String,
            pageName: String,
            trigger: String = String.EMPTY
        ): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_PAGE, pageName)
                if (trigger.isNotBlank()) {
                    putString(KEY_EVENT, trigger)
                }
            }
        }
    }
}
