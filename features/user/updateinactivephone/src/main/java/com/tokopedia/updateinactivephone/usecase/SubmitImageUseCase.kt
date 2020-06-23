package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.data.model.response.GqlUpdatePhoneStatusResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SubmitImageUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GqlUpdatePhoneStatusResponse>,
        private val rawQueries: Map<String, String>
){
    fun submitImage(
            onSuccess: (GqlUpdatePhoneStatusResponse) -> Unit,
            onError: (Throwable) -> Unit,
            requestParams: RequestParams
    ){
        val params = generateParams(requestParams)
        val rawQuery = rawQueries[UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_UPDATE_PHONE_EMAIL]
        if(!rawQuery.isNullOrEmpty()){
            gqlUseCase.apply {
                setTypeClass(GqlUpdatePhoneStatusResponse::class.java)
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

    private fun generateParams(requestParams: RequestParams): Map<String, Any> {
        return mapOf(
                UpdateInactivePhoneConstants.QueryConstants.PHONE to requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.PHONE, ""),
                UpdateInactivePhoneConstants.QueryConstants.EMAIL to requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.EMAIL, ""),
                UpdateInactivePhoneConstants.QueryConstants.USER_ID to requestParams.getInt(UpdateInactivePhoneConstants.QueryConstants.USER_ID, 0),
                UpdateInactivePhoneConstants.QueryConstants.FILE_UPLOADED to "",
                UpdateInactivePhoneConstants.QueryConstants.ID_CARD_IMAGE to requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.ID_CARD_IMAGE, ""),
                UpdateInactivePhoneConstants.QueryConstants.SAVING_BOOK_IMAGE to requestParams.getString(UpdateInactivePhoneConstants.QueryConstants.SAVING_BOOK_IMAGE, "")
        )
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }
}