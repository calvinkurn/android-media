package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.OfferInterruptResponse
import javax.inject.Inject

class OfferInterruptUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, OfferInterruptResponse>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): OfferInterruptResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query offerInterrupt(${'$'}supportBiometric: Boolean!, ${'$'}device_biometrics: String!) {
            status
            offer_interrupt(supportBiometric: ${'$'}supportBiometric, device_biometrics: ${'$'}device_biometrics) {
                status
                errorMessage
                interval
                offers {
                    name
                    enableSkip
                }
            }
        }
    """.trimIndent()

    companion object {
        const val PARAM_SUPPORT_BIOMETRIC = "supportBiometric"
        const val PARAM_DEVICE_BIOMETRIC = "device_biometrics"
    }
}
