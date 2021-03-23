package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.list.domain.mapper.WaitingPaymentMapper
import com.tokopedia.sellerorder.list.domain.model.SomListWaitingPaymentResponse
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import javax.inject.Inject

class SomListGetWaitingPaymentUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: WaitingPaymentMapper
) : BaseGraphqlUseCase<WaitingPaymentCounter>(gqlRepository) {

    override suspend fun executeOnBackground(): WaitingPaymentCounter {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): WaitingPaymentCounter {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListWaitingPaymentResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListWaitingPaymentResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListWaitingPaymentResponse.Data>()
            return mapper.mapResponseToUiModel(response.orderFilterSom.waitingPaymentCounter)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private val QUERY = """
            query GetWaitingPaymentCounter {
              orderFilterSom {
                waiting_payment_counter {
                  text
                  amount
                }
              }
            }
        """.trimIndent()
    }
}