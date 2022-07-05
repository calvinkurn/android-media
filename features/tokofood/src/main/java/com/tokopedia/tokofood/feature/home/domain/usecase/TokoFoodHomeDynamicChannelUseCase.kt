package com.tokopedia.tokofood.feature.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeLayoutResponse
import com.tokopedia.tokofood.feature.home.domain.query.TokoFoodHomeDynamicChannelQuery
import javax.inject.Inject

class TokoFoodHomeDynamicChannelUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<TokoFoodHomeLayoutResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TokoFoodHomeDynamicChannelQuery)
        setTypeClass(TokoFoodHomeLayoutResponse::class.java)
    }

    suspend fun execute(
        localCacheModel: LocalCacheModel?
    ): TokoFoodHomeLayoutResponse {
        setRequestParams(TokoFoodHomeDynamicChannelQuery.createRequestParams(localCacheModel))
        return executeOnBackground()
    }
}