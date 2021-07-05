package com.tokopedia.fcmcommon.domain

import com.tokopedia.fcmcommon.data.UpdateFcmTokenResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase

open class UpdateFcmTokenUseCase(
        private val useCase: GraphqlUseCase<UpdateFcmTokenResponse>,
        private val rawQuery: String
) {

    operator fun invoke(
            params: Map<String, String>,
            onSuccess: (UpdateFcmTokenResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(UpdateFcmTokenResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(rawQuery)
            execute(onSuccess, onError)
        }
    }

}