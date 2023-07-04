package com.tokopedia.loginregister.login.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprint
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import javax.inject.Inject

class RegisterCheckFingerprintUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val fingerprintPreferenceManager: FingerprintPreference,
    dispatcher: CoroutineDispatchers,
) : CoroutineUseCase<Unit, Boolean>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query register_check_fp(${'$'}device_biometrics: String!) {
            OTPBiometricCheckRegistered(device_biometrics: ${'$'}device_biometrics) {
                registered
                errorMessage
            }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Boolean {
        val param = mapOf(PARAM_BIOMETRIC_ID to fingerprintPreferenceManager.getUniqueId())
        val result: RegisterCheckFingerprint = repository.request(graphqlQuery(), param)
        return result.data.isRegistered
    }

    companion object {
        const val PARAM_BIOMETRIC_ID = "device_biometrics"
    }
}
