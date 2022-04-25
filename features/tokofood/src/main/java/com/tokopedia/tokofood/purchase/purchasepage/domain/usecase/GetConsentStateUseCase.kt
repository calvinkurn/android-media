package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.GetConsentStateResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConsentStateUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<Unit, GetConsentStateResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        query GetConsentState {
          get_consent_state() {
            message
            success
            data {
              success
              message
              is_agreed
              bottomsheet {
                is_show_bottomsheet
                image_url
                title
                description
                tnc_link
                }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Flow<GetConsentStateResponse> {
        return repository.requestAsFlow(graphqlQuery(), params)
    }

}