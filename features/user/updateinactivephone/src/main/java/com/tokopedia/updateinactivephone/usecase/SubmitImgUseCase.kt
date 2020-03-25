package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SubmitImgUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GraphqlResponse>,
        private val rawQueries: Map<String, String>
){
    fun submitImage(
            onSuccess: (GraphqlResponse) -> Unit,
            onError: (Throwable) -> Unit,
            requestParams: RequestParams
    ){
        val params = generateParams(requestParams)
        val rawQuery = rawQueries[UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_UPDATE_PHONE_EMAIL]
        if(!rawQuery.isNullOrEmpty()){
            gqlUseCase.apply {
                setTypeClass(GraphqlResponse::class.java)
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
                UpdateInactivePhoneConstants.QueryConstants.FILE_UPLOADED to requestParams.getObject(PARAM_FILE_UPLOADED)
        )
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }

    companion object {
        const val PARAM_FILE_UPLOADED = "file_uploaded"
    }
}