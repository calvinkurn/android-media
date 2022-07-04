package com.tokopedia.tokopedianow.recipebookmark.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.query.GetHomeLayoutData
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.query.RemoveRecipeBookmarkQuery.PARAM_RECIPE_ID
import com.tokopedia.tokopedianow.recipebookmark.domain.query.RemoveRecipeBookmarkQuery.PARAM_USER_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Remove Bookmarks Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1986498304/Mutation+TokonowRemoveRecipeBookmark+GQL
 */

class RemoveRecipeBookmarkUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<RemoveRecipeBookmarkResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetHomeLayoutData)
        setTypeClass(RemoveRecipeBookmarkResponse::class.java)
    }

    suspend fun execute(
        userId: String,
        recipeId: String
    ): RemoveRecipeBookmarkResponse.Data.TokonowRemoveRecipeBookmark {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_USER_ID, userId)
            putString(PARAM_RECIPE_ID, recipeId)
        }.parameters)
        return executeOnBackground().data.tokonowRemoveRecipeBookmark
    }
}