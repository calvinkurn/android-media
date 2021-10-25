package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 07/10/21.
 */

class VerifyFingerprintUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val fingerprintPreference: FingerprintPreference
) : CoroutineUseCase<SignatureData, VerifyFingerprintPojo>(Dispatchers.IO){

    override suspend fun execute(params: SignatureData): VerifyFingerprintPojo {
        val mappedParams = createRequestParams(params)
        return repository.request(graphqlQuery(), mappedParams)
    }

    private fun createRequestParams(signature: SignatureData): Map<String, Any>{
        return mapOf(
            LoginFingerprintQueryConstant.PARAM_OTP_TYPE to LoginFingerprintQueryConstant.VALIDATE_OTP_TYPE,
            LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
            LoginFingerprintQueryConstant.PARAM_DATETIME to signature.datetime,
            BiometricConstant.PARAM_BIOMETRIC_ID to fingerprintPreference.getUniqueId()
        )
    }

    override fun graphqlQuery(): String = """
            query validate_fingerprint(${'$'}otpType: Int!, ${'$'}signature: String!, ${'$'}datetime: String!, ${'$'}device_biometrics: String!){
                OTPVerifyBiometric(otpType: ${'$'}otpType, signature: ${'$'}signature, datetime: ${'$'}datetime, device_biometrics: ${'$'}device_biometrics) {
                        isSuccess
                        status
                        validateToken
                        errorMessage
                }
            }""".trimIndent()
}