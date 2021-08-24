package com.tokopedia.troubleshooter.notification.data.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import kotlinx.coroutines.CoroutineDispatcher

open class GetTroubleshootStatusUseCase constructor(
        repository: GraphqlRepository,
        private val graphQuery: String,
        dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, NotificationTroubleshoot>(repository, dispatcher) {

    override fun graphqlQuery(): String {
        return graphQuery
    }

    override suspend fun execute(params: Unit): NotificationTroubleshoot {
        return request(params)
    }

}