package com.tokopedia.managepassword.forgotpassword.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.di.ManagePasswordContext
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordResponseModel
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
        @ManagePasswordContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase<ForgotPasswordResponseModel>
) {
    lateinit var params: Map<String, Any>

    fun sendRequest(onSuccess: (ForgotPasswordResponseModel) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.query_reset_password)
        graphqlUseCase.run {
            setTypeClass(ForgotPasswordResponseModel::class.java)
            setGraphqlQuery(rawQuery)
            setRequestParams(params)
            execute({ result ->
                onSuccess(result)
            },{throwable ->
                onError(throwable)
            })
        }
    }

    fun cancelJobs() {
        graphqlUseCase.cancelJobs()
    }
}