package com.tokopedia.tokopedianow.recipebookmark.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.query.GetHomeLayoutData
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.query.RemoveRecipeBookmarkQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Remove Bookmarks Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1986497828/Mutation+TokonowAddRecipeBookmark+GQL
 */

class AddRecipeBookmarkUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<AddRecipeBookmarkResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetHomeLayoutData)
        setTypeClass(AddRecipeBookmarkResponse::class.java)
    }

    suspend fun execute(
        userId: String,
        recipeId: String
    ): AddRecipeBookmarkResponse.Data.TokonowAddRecipeBookmark {
        setRequestParams(RequestParams.create().apply {
            putString(RemoveRecipeBookmarkQuery.PARAM_USER_ID, userId)
            putString(RemoveRecipeBookmarkQuery.PARAM_RECIPE_ID, recipeId)
        }.parameters)
        return executeOnBackground().data.tokonowAddRecipeBookmark
    }
}