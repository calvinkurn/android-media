package com.tokopedia.troubleshooter.notification.data.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import kotlinx.coroutines.CoroutineDispatcher

open class GetTroubleshootStatusUseCase constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String,
        dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, NotificationTroubleshoot>(dispatcher) {

    override fun graphqlQuery(): String {
        return graphQuery
    }

    override suspend fun execute(params: Unit): NotificationTroubleshoot {
        return repository.request(graphqlQuery(), params)
    }

}