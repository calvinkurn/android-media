package com.tokopedia.tokopedianow.recipedetail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.recipedetail.domain.model.GetRecipeResponse
import com.tokopedia.tokopedianow.recipedetail.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe.PARAM_RECIPE_ID
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe.PARAM_WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Recipe GQL Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1973782733/GQL+Get+Recipe
 */

class GetRecipeUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GetRecipeResponse>(gqlRepository) }

    suspend fun execute(recipeId: String, warehouseId: String): RecipeResponse {
        graphql.apply {
            setGraphqlQuery(GetRecipe)
            setTypeClass(GetRecipeResponse::class.java)

            setRequestParams(RequestParams.create().apply {
                putString(PARAM_RECIPE_ID, recipeId)
                putString(PARAM_WAREHOUSE_ID, warehouseId)
            }.parameters)

            val response = executeOnBackground()
            val errorMessages = response.header.messages

            if(errorMessages.isNullOrEmpty()) {
                return response.data
            } else {
                throw MessageErrorException(errorMessages.firstOrNull().orEmpty())
            }
        }
    }
}