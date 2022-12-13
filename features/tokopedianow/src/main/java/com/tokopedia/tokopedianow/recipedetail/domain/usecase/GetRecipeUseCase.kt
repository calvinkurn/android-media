package com.tokopedia.tokopedianow.recipedetail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipedetail.domain.model.TokoNowGetRecipe
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe.PARAM_RECIPE_ID
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe.PARAM_SLUG
import com.tokopedia.tokopedianow.recipedetail.domain.query.GetRecipe.PARAM_WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Recipe GQL Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1973782733/GQL+Get+Recipe
 */
class GetRecipeUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    companion object {
        private const val DEFAULT_RECIPE_ID = "0"
        private const val DEFAULT_SLUG = ""
        private const val PARAM_ERROR_MESSAGE = "recipeId and slug cannot be empty"
    }

    private val graphql by lazy { GraphqlUseCase<TokoNowGetRecipe>(gqlRepository) }

    /**
     * @param recipeId id of the recipe
     * @param slug slug obtained from recipe url as identifier
     * @param warehouseId warehouseId obtained from address data
     */
    suspend fun execute(
        recipeId: String = DEFAULT_RECIPE_ID,
        slug: String = DEFAULT_SLUG,
        warehouseId: String
    ): RecipeResponse {
        graphql.apply {
            setGraphqlQuery(GetRecipe)
            setTypeClass(TokoNowGetRecipe::class.java)

            if(recipeId.isEmpty() && slug.isEmpty()) {
                throw MessageErrorException(PARAM_ERROR_MESSAGE)
            }

            setRequestParams(RequestParams.create().apply {
                putString(PARAM_RECIPE_ID, recipeId)
                putString(PARAM_SLUG, slug)
                putString(PARAM_WAREHOUSE_ID, warehouseId)
            }.parameters)

            val getRecipe = executeOnBackground()
            return getRecipe.response.data
        }
    }
}
