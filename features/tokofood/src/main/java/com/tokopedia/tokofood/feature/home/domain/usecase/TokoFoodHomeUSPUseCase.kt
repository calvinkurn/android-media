package com.tokopedia.tokofood.feature.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.feature.home.domain.query.TokoFoodHomeUSPQuery
import javax.inject.Inject

class TokoFoodHomeUSPUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
 GraphqlUseCase<TokoFoodHomeUSPResponse>(graphqlRepository){

     init {
         setGraphqlQuery(TokoFoodHomeUSPQuery)
         setTypeClass(TokoFoodHomeUSPResponse::class.java)
     }

    suspend fun execute(): TokoFoodHomeUSPResponse {
        return executeOnBackground()
    }
}