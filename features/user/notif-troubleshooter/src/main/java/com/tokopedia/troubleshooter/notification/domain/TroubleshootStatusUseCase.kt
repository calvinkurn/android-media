package com.tokopedia.troubleshooter.notification.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.base.BaseUseCase
import com.tokopedia.troubleshooter.notification.entity.NotificationTroubleshoot
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

open class TroubleshootStatusUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String
) : BaseUseCase<RequestParams, NotificationTroubleshoot>() {

    override suspend fun execute(params: RequestParams): NotificationTroubleshoot {
        return execute(graphQuery, repository, params)
    }

}