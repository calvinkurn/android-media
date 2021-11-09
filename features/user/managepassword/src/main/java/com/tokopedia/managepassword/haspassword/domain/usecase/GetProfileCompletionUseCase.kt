package com.tokopedia.managepassword.haspassword.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.di.ManagePasswordContext
import javax.inject.Inject

class GetProfileCompletionUseCase @Inject constructor(
        @ManagePasswordContext private val context: Context,
        private val useCase: GraphqlUseCase<ProfileDataModel>
) {

    fun getData(onSuccess: (ProfileDataModel) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.query_is_created_password)
        useCase.apply {
            setTypeClass(ProfileDataModel::class.java)
            setGraphqlQuery(rawQuery)
            execute({ result ->
                onSuccess(result)
            }, { throwable ->
                onError(throwable)
            })
        }
    }
}