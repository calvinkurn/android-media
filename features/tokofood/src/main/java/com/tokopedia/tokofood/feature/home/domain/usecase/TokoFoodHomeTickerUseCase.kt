package com.tokopedia.tokofood.feature.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeTickerResponse
import com.tokopedia.tokofood.feature.home.domain.query.TokoFoodHomeTickerQuery
import javax.inject.Inject

class TokoFoodHomeTickerUseCase  @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<TokoFoodHomeTickerResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TokoFoodHomeTickerQuery)
        setTypeClass(TokoFoodHomeTickerResponse::class.java)
    }

    suspend fun execute(
        localCacheModel: LocalCacheModel?
    ): TokoFoodHomeTickerResponse {
        setRequestParams(TokoFoodHomeTickerQuery.createRequestParams(localCacheModel))
        return executeOnBackground()
    }
}