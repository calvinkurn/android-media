package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.data.model.response.GqlValidateUserDataResponse
import javax.inject.Inject

class GetValidationUserDataUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GqlValidateUserDataResponse>,
        private val rawQueries: Map<String, String>
){
    fun getValidationUserData(
            onSuccess: (GqlValidateUserDataResponse) -> Unit,
            onError: (Throwable) -> Unit,
            phoneNumber: String, email: String, userId: String
    ){
        val params = generateParams(phoneNumber, email, userId)
        val rawQuery = rawQueries[UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_VALIDATE_USER_DATA]
        if(!rawQuery.isNullOrEmpty()){
            gqlUseCase.apply {
                setTypeClass(GqlValidateUserDataResponse::class.java)
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

    private fun generateParams(phoneNumber: String, email: String, userId: String): Map<String, Any> {
        val id = Integer.parseInt(if (userId != "") userId else "0")
        return mapOf(
                UpdateInactivePhoneConstants.QueryConstants.PHONE to phoneNumber,
                UpdateInactivePhoneConstants.QueryConstants.EMAIL to email,
                UpdateInactivePhoneConstants.QueryConstants.USER_ID to id
        )
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }
}