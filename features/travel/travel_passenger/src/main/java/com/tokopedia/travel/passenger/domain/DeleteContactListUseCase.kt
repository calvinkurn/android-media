package com.tokopedia.travel.passenger.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel.passenger.data.entity.TravelDeleteContactModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class DeleteContactListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: GqlQueryInterface, deleteContactModel: TravelDeleteContactModel): Result<TravelDeleteContactModel.Response> {
        useCase.clearRequest()
        return try {
            val params = mapOf(PARAM_DELETE_CONTACT to deleteContactModel)
            val graphqlRequest = GraphqlRequest(query, TravelDeleteContactModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)
            val successStatus = useCase.executeOnBackground().getSuccessData<TravelDeleteContactModel.Response>()
            Success(successStatus)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val PARAM_DELETE_CONTACT = "data"
    }

}