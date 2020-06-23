package com.tokopedia.managepassword.changepassword.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordResponseModel
import com.tokopedia.managepassword.di.ManagePasswordContext
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
        @ManagePasswordContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase<ChangePasswordResponseModel>
) {
    lateinit var params: Map<String, Any>

    fun submit(onSuccess: (ChangePasswordResponseModel) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.query_change_password)
        graphqlUseCase.apply {
            setTypeClass(ChangePasswordResponseModel::class.java)
            setGraphqlQuery(rawQuery)
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