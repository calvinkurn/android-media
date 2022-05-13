package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.AgreeConsentData
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.AgreeConsentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AgreeConsentUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<Unit, AgreeConsentResponse>(dispatchers.io) {

    private val isDebug = true

    override fun graphqlQuery(): String = """
        query AgreeConsent {
          agree_consent() {
            message
            status
            data {
              success
            	message
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Flow<AgreeConsentResponse> {
        if (isDebug) {
            return flow {
                kotlinx.coroutines.delay(1000)
                emit(getDummyResponse())
            }
        } else {
            return repository.requestAsFlow(graphqlQuery(), params)
        }
    }

    private fun getDummyResponse(): AgreeConsentResponse {
        return AgreeConsentResponse(
            data = AgreeConsentData(
                success = 1,
                message = ""
            )
        )
    }

}