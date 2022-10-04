package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import javax.inject.Inject

/**
 * Created by jegul on 02/06/20
 */
@GqlQuery(GetProductsInEtalaseUseCase.QUERY_NAME, GetProductsInEtalaseUseCase.QUERY)
class GetProductsInEtalaseUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetProductsByEtalaseResponse>(graphqlRepository) {

    var params: Map<String, Any> = emptyMap()

    init {
        setGraphqlQuery(GetProductsInEtalaseUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetProductsByEtalaseResponse::class.java)
    }

    suspend fun executeWithParam(
        authorId: String,
        authorType: Int,
        cursor: String,
        limit: Int,
        keyword: String,
        sort: SortUiModel,
        etalaseId: String,
    ): GetProductsByEtalaseResponse {
        setRequestParams(
            mapOf(
                PARAM_AUTHOR to mapOf(
                    PARAM_AUTHOR_ID to authorId,
                    PARAM_AUTHOR_TYPE to authorType,
                ),
                PARAM_FILTER to mapOf(
                    PARAM_FILTER_KEYWORD to keyword,
                    PARAM_FILTER_ETALASE to if(etalaseId.isEmpty()) emptyList<String>() else listOf(etalaseId),
                ),
                PARAM_SORT to mapOf(
                    PARAM_SORT_ID to sort.key,
                    PARAM_SORT_VALUE to sort.direction.value,
                ),
                PARAM_PAGER_CURSOR to mapOf(
                    PARAM_CURSOR to cursor,
                    PARAM_LIMIT to limit,
                ),
            )
        )

        return this.executeOnBackground()
    }

    companion object {

        private const val PARAM_REQ = "req"
        private const val PARAM_AUTHOR = "author"
        private const val PARAM_AUTHOR_ID = "ID"
        private const val PARAM_AUTHOR_TYPE = "type"

        private const val PARAM_FILTER = "filter"
        private const val PARAM_FILTER_KEYWORD = "keyword"
        private const val PARAM_FILTER_ETALASE = "storefrontIDs"

        private const val PARAM_SORT = "sort"
        private const val PARAM_SORT_ID = "ID"
        private const val PARAM_SORT_VALUE = "value"

        private const val PARAM_PAGER_CURSOR = "pagerCursor"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"

        const val QUERY_NAME = "GetProductsInEtalaseUseCaseQuery"
        const val QUERY = """
            query BroadcasterGetProductList(
                ${"$$PARAM_AUTHOR"}: BroadcasterAuthorRequest!,
                ${"$$PARAM_FILTER"}: BroadcasterProductListFilter,
                ${"$$PARAM_SORT"}: BroadcasterProductListSort,
                ${"$$PARAM_PAGER_CURSOR"}: BroadcasterPagerCursor
            ) {
                broadcasterGetProductList(
                    $PARAM_REQ: {
                        $PARAM_AUTHOR: ${"$$PARAM_AUTHOR"},
                        $PARAM_FILTER: ${"$$PARAM_FILTER"},
                        $PARAM_SORT: ${"$$PARAM_SORT"},
                        $PARAM_PAGER_CURSOR: ${"$$PARAM_PAGER_CURSOR"}
                    }
                ) {
                    products {
                        pictures {
                            urlThumbnail
                        }
                        ID
                        name
                        stock
                        price {
                            min
                            max
                            minFmt
                            maxFmt
                        }
                    }
                    pagerCursor {
                        limit
                        cursor
                        hasNext
                    }
                }
            }
        """
    }
}
