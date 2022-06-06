package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.AgreeConsentResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AgreeConsentUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<Unit, AgreeConsentResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        mutation AgreeConsent {
          tokofoodSubmitUserConsent() {
            message
            success
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Flow<AgreeConsentResponse> {
        return repository.requestAsFlow(graphqlQuery(), params)
    }

}