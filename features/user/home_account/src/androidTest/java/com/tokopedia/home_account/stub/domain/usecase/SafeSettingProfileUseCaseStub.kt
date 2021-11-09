package com.tokopedia.home_account.stub.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.SetUserProfileSettingResponse
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase
import javax.inject.Inject

class SafeSettingProfileUseCaseStub @Inject constructor(
    @ApplicationContext context: Context,
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