package com.tokopedia.tokofood.home.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.home.domain.query.TokoFoodHomeUSPQuery
import com.tokopedia.tokofood.home.presentation.viewmodel.DummyData
import kotlinx.coroutines.delay
import javax.inject.Inject

class TokoFoodHomeUSPUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
 GraphqlUseCase<TokoFoodHomeUSPResponse>(graphqlRepository){

     init {
         setGraphqlQuery(TokoFoodHomeUSPQuery)
         setTypeClass(TokoFoodHomeUSPResponse::class.java)
     }

    suspend fun execute(): TokoFoodHomeUSPResponse {
        delay(1000)
        return Gson().fromJson(DummyData.dummyUSP, TokoFoodHomeUSPResponse::class.java)
    }
}