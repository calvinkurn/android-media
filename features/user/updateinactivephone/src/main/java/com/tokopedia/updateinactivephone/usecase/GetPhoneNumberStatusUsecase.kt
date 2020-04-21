package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.PHONE
import com.tokopedia.updateinactivephone.data.model.response.GqlCheckPhoneStatusResponse
import javax.inject.Inject

class GetPhoneNumberStatusUsecase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GqlCheckPhoneStatusResponse>,
        private val rawQueries: Map<String, String>
){
    fun getPhoneNumberStatus(
            onSuccess: (GqlCheckPhoneStatusResponse) -> Unit,
            onError: (Throwable) -> Unit,
            phoneNumber: String
    ){
        val params = generateParams(phoneNumber)
        val rawQuery = rawQueries[UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_CHECK_PHONE_NUMBER_STATUS]
        if(!rawQuery.isNullOrEmpty()){
            gqlUseCase.apply {
                setTypeClass(GqlCheckPhoneStatusResponse::class.java)
                setRequestParams(params)
                setGraphqlQuery(rawQuery)
                execute({ result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                })
            }
        }
    }

    private fun generateParams(phoneNumber: String): Map<String, Any> {
        return mapOf(PHONE to phoneNumber)
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }
}