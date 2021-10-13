package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VerifyFingerprintUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<VerifyFingerprintPojo>,
    private var dispatchers: CoroutineDispatchers,
    private val fingerprintPreferenceManager: FingerprintPreference
): CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun verifyFingerprint(
        signature: SignatureData,
        onSuccess: (VerifyFingerprintPojo) -> Unit,
        onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(VerifyFingerprintPojo::class.java)
                    setGraphqlQuery(query)
                    setRequestParams(createRequestParams(signature))
                }.executeOnBackground()
            withContext(dispatchers.main) {
                onSuccess(data)
            }
        }, {
            withContext(dispatchers.main) {
                onError(it)
            }
        })
    }

    fun createRequestParams(signature: SignatureData): Map<String, Any>{
        return mapOf(
            LoginFingerprintQueryConstant.PARAM_OTP_TYPE to LoginFingerprintQueryConstant.VALIDATE_OTP_TYPE,
            LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
            LoginFingerprintQueryConstant.PARAM_DATETIME to signature.datetime,
            BiometricConstant.PARAM_BIOMETRIC_ID to fingerprintPreferenceManager.getUniqueId()
        )
    }

    companion object {
        val query: String = """
            query validate_fingerprint(${'$'}otpType: Int!, ${'$'}signature: String!, ${'$'}datetime: String!, ${'$'}device_biometrics: String!){
                OTPVerifyBiometric(otpType: ${'$'}otpType, signature: ${'$'}signature, datetime: ${'$'}datetime, device_biometrics: ${'$'}device_biometrics) {
                        isSuccess
                        status
                        validateToken
                        errorMessage
                }
            }""".trimIndent()
    }
}