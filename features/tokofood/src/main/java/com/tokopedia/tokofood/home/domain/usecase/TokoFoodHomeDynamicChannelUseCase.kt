package com.tokopedia.tokofood.home.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.home.domain.data.GetTokoFoodHomeLayoutResponse
import com.tokopedia.tokofood.home.domain.query.TokoFoodHomeDynamicChannelQuery
import com.tokopedia.tokofood.home.presentation.viewmodel.DummyData
import kotlinx.coroutines.delay
import javax.inject.Inject

class TokoFoodHomeDynamicChannelUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<GetTokoFoodHomeLayoutResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TokoFoodHomeDynamicChannelQuery)
        setTypeClass(GetTokoFoodHomeLayoutResponse::class.java)
    }

    suspend fun execute(): GetTokoFoodHomeLayoutResponse {
        delay(1000)
        return Gson().fromJson(DummyData.dummyHomeData, GetTokoFoodHomeLayoutResponse::class.java)
    }
}