package com.tokopedia.managepassword.addpassword.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.di.ManagePasswordContext

class AddPasswordUseCase constructor(
        @ManagePasswordContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase<AddPasswordResponseModel>
) {
    lateinit var params: Map<String, Any>

    fun submit(onSuccess: (AddPasswordResponseModel) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.query_add_password)
        graphqlUseCase.apply {
            setTypeClass(AddPasswordResponseModel::class.java)
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