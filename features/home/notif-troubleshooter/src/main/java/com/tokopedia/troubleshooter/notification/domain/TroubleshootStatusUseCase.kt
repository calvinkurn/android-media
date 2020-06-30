package com.tokopedia.troubleshooter.notification.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.base.BaseUseCase
import com.tokopedia.troubleshooter.notification.entity.PushNotifCheckerResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TroubleshootStatusUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String
) : BaseUseCase<RequestParams, PushNotifCheckerResponse>() {

    override suspend fun execute(params: RequestParams): PushNotifCheckerResponse {
        if (params.paramsAllValueInString.isEmpty()) throw Exception("Not param found")

        return execute(
                query = graphQuery,
                repository = repository,
                requestParams = params
        )
    }

}