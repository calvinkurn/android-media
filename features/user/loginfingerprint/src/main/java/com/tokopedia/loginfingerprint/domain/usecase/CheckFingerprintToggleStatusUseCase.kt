package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 07/10/21.
 */

class CheckFingerprintToggleStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val fingerprintPreference: FingerprintPreference
): CoroutineUseCase<String, CheckFingerprintPojo>(Dispatchers.IO) {

    override suspend fun execute(params: String): CheckFingerprintPojo {
        return repository.request(graphqlQuery(), createRequestParams(params))
    }

    private fun createRequestParams(userId: String): Map<String, String> {
        return  mapOf(
            BiometricConstant.PARAM_USER_ID to userId,
            BiometricConstant.PARAM_BIOMETRIC_ID to fingerprintPreference.getUniqueId()
        )
    }

    override fun graphqlQuery(): String = """
            query check_otp_toggle(${'$'}userID: String!, ${'$'}device_biometrics: String!) {
                OTPBiometricCheckToggleStatus(userID: ${'$'}userID, device_biometrics: ${'$'}device_biometrics) {
                    is_active
                    is_success
                    error_message
               }
            }""".trimIndent()
}