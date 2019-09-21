package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.data.entity.TravelDeleteContactModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class DeleteContactListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String, deleteContactModel: TravelDeleteContactModel): Result<TravelDeleteContactModel.Response> {
        useCase.clearRequest()
        try {
            val params = mapOf(PARAM_DELETE_CONTACT to deleteContactModel)
            val graphqlRequest = GraphqlRequest(query, TravelDeleteContactModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)
            val successStatus = useCase.executeOnBackground().getSuccessData<TravelDeleteContactModel.Response>()
            return Success(successStatus)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        val PARAM_DELETE_CONTACT = "data"
    }

}