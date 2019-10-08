package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class UpsertContactListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String, travelUpsertContactModel: TravelUpsertContactModel): Result<TravelUpsertContactModel.Response> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()
        try {
            val params = mapOf(PARAM_UPSERT_CONTACT to travelUpsertContactModel)
            val graphqlRequest = GraphqlRequest(query, TravelUpsertContactModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

            val successStatus = useCase.executeOnBackground().getSuccessData<TravelUpsertContactModel.Response>()
            return Success(successStatus)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        const val PARAM_UPSERT_CONTACT = "data"

        const val PARAM_TRAVEL_HOTEL = "hotel"
        const val PARAM_TRAVEL_FLIGHT = "flight"
    }
}