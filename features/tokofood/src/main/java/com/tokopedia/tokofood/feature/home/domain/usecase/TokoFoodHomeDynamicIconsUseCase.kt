package com.tokopedia.tokofood.feature.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeDynamicIconsResponse
import com.tokopedia.tokofood.feature.home.domain.query.TokoFoodHomeDynamicIconsQuery
import javax.inject.Inject

class TokoFoodHomeDynamicIconsUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<TokoFoodHomeDynamicIconsResponse>(graphqlRepository) {

    init {
       setGraphqlQuery(TokoFoodHomeDynamicIconsQuery)
       setTypeClass(TokoFoodHomeDynamicIconsResponse::class.java)
    }

    suspend fun execute(param: String): TokoFoodHomeDynamicIconsResponse {
        setRequestParams(TokoFoodHomeDynamicIconsQuery.createRequestParams(param))
        return executeOnBackground()
    }
}