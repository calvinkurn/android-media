package com.tokopedia.revamp_changepassword.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.changepassword.R
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.revamp_changepassword.data.ProfileCompletionData
import com.tokopedia.revamp_changepassword.di.ChangePasswordContext
import javax.inject.Inject

class GetProfileCompletionUseCase @Inject constructor(
        @ChangePasswordContext private val context: Context,
        private val useCase: GraphqlUseCase<ProfileCompletionData>
) {

    fun getData(onSuccess: (ProfileCompletionData) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.query_profile_has_password)
        useCase.apply {
            setTypeClass(ProfileCompletionData::class.java)
            setGraphqlQuery(rawQuery)
            execute({ result ->
                onSuccess(result)
            }, { throwable ->
                onError(throwable)
            })
        }
    }
}