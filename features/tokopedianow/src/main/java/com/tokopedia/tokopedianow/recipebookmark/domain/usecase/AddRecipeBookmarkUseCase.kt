package com.tokopedia.tokopedianow.recipebookmark.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.query.AddRecipeBookmarkQuery
import com.tokopedia.tokopedianow.recipebookmark.domain.query.AddRecipeBookmarkQuery.PARAM_RECIPE_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Add Bookmarks Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1986497828/Mutation+TokonowAddRecipeBookmark+GQL
 */

class AddRecipeBookmarkUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) {
    private val graphql by lazy { GraphqlUseCase<AddRecipeBookmarkResponse>(gqlRepository) }

    init {
        graphql.setGraphqlQuery(AddRecipeBookmarkQuery)
        graphql.setTypeClass(AddRecipeBookmarkResponse::class.java)
    }

    suspend fun execute(
        recipeId: String
    ): AddRecipeBookmarkResponse.TokonowAddRecipeBookmark {
        graphql.setRequestParams(RequestParams.create().apply {
            putString(PARAM_RECIPE_ID, recipeId)
        }.parameters)

        val response = graphql.executeOnBackground().tokonowAddRecipeBookmark

        return if (response.header.success) {
            response
        } else {
            throw MessageErrorException(response.header.message)
        }
    }
}