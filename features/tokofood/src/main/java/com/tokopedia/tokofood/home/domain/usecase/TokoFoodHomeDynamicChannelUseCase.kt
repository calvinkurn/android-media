package com.tokopedia.tokofood.home.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.home.domain.data.TokoFoodHomeLayoutResponse
import com.tokopedia.tokofood.home.domain.query.TokoFoodHomeDynamicChannelQuery
import com.tokopedia.tokofood.home.presentation.viewmodel.DummyData
import kotlinx.coroutines.delay
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
        delay(1000)
        return Gson().fromJson(DummyData.dummyHomeData, TokoFoodHomeLayoutResponse::class.java)
    }
}