package com.tokopedia.home_account.stub.domain.usecase

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.SetUserProfileSettingResponse
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase

class SafeSettingProfileUseCaseStub(
    context: Context?,
    graphqlRepository: GraphqlRepository
) : SafeSettingProfileUseCase(context, graphqlRepository) {

    var response = SetUserProfileSettingResponse()

    override fun executeQuerySetSafeMode(
        onSuccess: (SetUserProfileSettingResponse) -> Unit,
        onError: (Throwable) -> Unit,
        savedValue: Boolean
    ) {
        onSuccess(response)
    }
}