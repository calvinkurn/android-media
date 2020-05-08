package com.tokopedia.managepassword.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.changepassword.R
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.data.ProfileDataModel
import com.tokopedia.managepassword.di.ManagePasswordContext
import javax.inject.Inject

class GetProfileCompletionUseCase @Inject constructor(
        @ManagePasswordContext private val context: Context,
        private val useCase: GraphqlUseCase<ProfileDataModel>
) {

    fun getData(onSuccess: (ProfileDataModel) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.query_profile_has_password)
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