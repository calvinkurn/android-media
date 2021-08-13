package com.tokopedia.managepassword.addpassword.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.addpassword.domain.AddPasswordQueries
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordV2Response
import com.tokopedia.managepassword.common.ManagePasswordConstant

class AddPasswordV2UseCase constructor(
        private val graphqlUseCase: GraphqlUseCase<AddPasswordV2Response>
) {
    lateinit var params: Map<String, Any>

    fun setParams(password: String, confirmationPassword: String, hash: String) {
        params = mapOf(
                ManagePasswordConstant.PARAM_PASSWORD to password,
                ManagePasswordConstant.PARAM_PASSWORD_CONFIRMATION to confirmationPassword,
                ManagePasswordConstant.PARAM_HASH to hash
        )
    }

    fun submit(onSuccess: (AddPasswordV2Response) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.apply {
            setTypeClass(AddPasswordV2Response::class.java)
            setGraphqlQuery(AddPasswordQueries.addPasswordV2)
            setRequestParams(params)
            execute(onSuccess = {
                onSuccess(it)
            }, onError = {
                onError(it)
            })
        }
    }

    fun cancelJobs() {
        graphqlUseCase.cancelJobs()
    }
}