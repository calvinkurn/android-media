package com.tokopedia.tokopedianow.recipelist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.recipelist.domain.model.GetRecipeListResponse
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList
import javax.inject.Inject

/**
 * Get Recipe GQL Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1973782733/Query+TokonowGetRecipes+GQL+FE
 */
class GetRecipeListUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<TokoNowGetRecipes>(gqlRepository) }

    suspend fun execute(param: RecipeListParam): GetRecipeListResponse {
        graphql.apply {
            setGraphqlQuery(GetRecipeList)
            setTypeClass(TokoNowGetRecipes::class.java)

            val requestParams = param.create()
            setRequestParams(requestParams)

            val request = executeOnBackground()
            return request.response
        }
    }
}