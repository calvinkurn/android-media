package com.tokopedia.checkout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.response.releasebookingstock.ReleaseBookingResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class ReleaseBookingUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Long, ReleaseBookingResponse>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: Long): ReleaseBookingResponse {
        val param = mapOf(
            "params" to arrayOf(
                mapOf(
                    "product_id" to params
                )
            )
        )
        val gqlRequest = GraphqlRequest(QUERY, ReleaseBookingResponse::class.java, param)
        val response = repository.response(listOf(gqlRequest))
        return response.getSuccessData()
    }
}

private val QUERY = """
    mutation release_booking_stock_ocs(${'$'}params:[OCSReleaseBookingStockParam]){
        release_booking_stock_ocs(params:${'$'}params){
            status
            error_message
            data{
                success
                message
            }
        }
    }
""".trimIndent()
