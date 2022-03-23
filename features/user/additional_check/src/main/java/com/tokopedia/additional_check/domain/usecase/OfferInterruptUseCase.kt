package com.tokopedia.additional_check.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.additional_check.data.OfferInterruptResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class OfferInterruptUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
): CoroutineUseCase<Map<String, Any>, OfferInterruptResponse>(dispatcher) {

    override suspend fun execute(params: Map<String, Any>): OfferInterruptResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
	query offerInterrupt(${'$'}supportBiometric: Boolean!){
	  offer_interrupt(supportBiometric: ${'$'}supportBiometric){
	    errorMessage
	    offers {
		name
		enableSkip
	    }
	    interval
	  }
	}
    """.trimIndent()

    companion object {
        const val PARAM_SUPPORT_BIOMETRIC = "supportBiometric"
    }
}