package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 07/10/21.
 */

class RegisterFingerprintUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository)
    : CoroutineUseCase<Map<String, Any>, RegisterFingerprintPojo>(Dispatchers.IO) {

    override suspend fun execute(params: Map<String, Any>): RegisterFingerprintPojo {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
            query register_fingerprint (${'$'}publicKey: String!, ${'$'}signature: String!, ${'$'}datetime: String!, ${'$'}device_biometrics: String!){
                RegisterFingerprint(publicKey: ${'$'}publicKey, signature: ${'$'}signature, datetime: ${'$'}datetime, device_biometrics: ${'$'}device_biometrics) {
                    success
                    message
                    errorMessage
               }
            }""".trimIndent()
}