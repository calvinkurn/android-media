package com.tokopedia.troubleshooter.notification.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.base.BaseUseCase
import com.tokopedia.troubleshooter.notification.entity.UpdateFcmTokenResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

open class UpdateTokenUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String
) : BaseUseCase<RequestParams, UpdateFcmTokenResponse>() {

    override suspend fun execute(params: RequestParams): UpdateFcmTokenResponse {
        return execute(graphQuery, repository, params)
    }

}