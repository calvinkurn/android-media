package com.tokopedia.troubleshooter.notification.data.domain

import com.tokopedia.graphql.coroutines.domain.interactor.CoroutineUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.di.IoDispatcher
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class TroubleshootStatusUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String,
        @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Map<String, String>, NotificationTroubleshoot>(dispatcher) {

    override suspend fun execute(params: Map<String, String>): NotificationTroubleshoot {
        return repository.request(graphQuery, params)
    }

}