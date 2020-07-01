package com.tokopedia.troubleshooter.notification.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.base.BaseUseCase
import com.tokopedia.troubleshooter.notification.entity.UpdateFcmTokenResponse
import com.tokopedia.usecase.RequestParams
import java.lang.Exception
import javax.inject.Inject

open class UpdateTokenUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String
) : BaseUseCase<RequestParams, UpdateFcmTokenResponse>() {

    override suspend fun execute(params: RequestParams): UpdateFcmTokenResponse {
        if (params.paramsAllValueInString.isEmpty()) throw Exception("Param not found")
        return execute(graphQuery, repository, params)
    }

    fun params(currentToken: String, newToken: String): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_OLD_TOKEN, currentToken)
            putString(PARAM_NEW_TOKEN, newToken)
        }
    }

    companion object {
        const val PARAM_OLD_TOKEN = "oldToken"
        const val PARAM_NEW_TOKEN = "newToken"
    }

}