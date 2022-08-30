package com.tokopedia.tokopedianow.recipebookmark.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.query.GetRecipeBookmarksQuery
import com.tokopedia.tokopedianow.recipebookmark.domain.query.GetRecipeBookmarksQuery.PARAM_PAGE
import com.tokopedia.tokopedianow.recipebookmark.domain.query.GetRecipeBookmarksQuery.PARAM_PER_PAGE
import com.tokopedia.tokopedianow.recipebookmark.domain.query.GetRecipeBookmarksQuery.PARAM_WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Recipe Bookmarks Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1939183376/Query+TokonowGetRecipeBookmarks+GQL
 */

class GetRecipeBookmarksUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) {
    private val graphql by lazy { GraphqlUseCase<GetRecipeBookmarksResponse>(gqlRepository) }

    init {
        graphql.setGraphqlQuery(GetRecipeBookmarksQuery)
        graphql.setTypeClass(GetRecipeBookmarksResponse::class.java)
    }

    suspend fun execute(
        warehouseId: String,
        page: Int,
        limit: Int
    ): GetRecipeBookmarksResponse {
        graphql.setRequestParams(RequestParams.create().apply {
            putString(PARAM_WAREHOUSE_ID, warehouseId)
            putInt(PARAM_PAGE, page)
            putInt(PARAM_PER_PAGE, limit)
        }.parameters)
        return graphql.executeOnBackground()
    }
}