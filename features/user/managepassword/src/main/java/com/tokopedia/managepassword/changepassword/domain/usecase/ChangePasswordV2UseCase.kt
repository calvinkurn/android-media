package com.tokopedia.managepassword.changepassword.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordV2ResponseModel
import com.tokopedia.managepassword.changepassword.domain.queries.ChangePasswordQueries
import javax.inject.Inject

class ChangePasswordV2UseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ChangePasswordV2ResponseModel>
) {
    lateinit var params: Map<String, Any>

    suspend fun executeOnBackground(): ChangePasswordV2ResponseModel {
        graphqlUseCase.apply {
            setTypeClass(ChangePasswordV2ResponseModel::class.java)
            setGraphqlQuery(ChangePasswordQueries.resetPasswordV2)
            setRequestParams(params)
            return executeOnBackground()
        }
    }

    fun setParams(new: String, confirmation: String, hash: String){
        params = mapOf(
                PARAM_ENCODE to PARAM_ENCODE,
                PARAM_NEW_PASS to new,
                PARAM_REPEAT_PASS to confirmation,
                PARAM_VALIDATE_TOKEN to VALIDATION_TOKEN,
                PARAM_HASH to hash
        )
    }

    fun cancelJobs() {
        graphqlUseCase.cancelJobs()
    }

    companion object {
        private const val PARAM_ENCODE = "encode"
        private const val PARAM_NEW_PASS = "new_password"
        private const val PARAM_REPEAT_PASS = "repeat_password"
        private const val PARAM_VALIDATE_TOKEN = "validatetoken"
        private const val PARAM_HASH = "h"
        private const val VALIDATION_TOKEN = "token"
    }
}