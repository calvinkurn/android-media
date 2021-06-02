package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VerifyFingerprintUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<VerifyFingerprintPojo>,
    private var dispatchers: CoroutineDispatchers
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
            LoginFingerprintQueryConstant.PARAM_TIME_UNIX to signature.datetime
        )
    }

    companion object {
        val query: String = """
            query validate_fingerprint(${'$'}otpType: String!, ${'$'}signature: String!, ${'$'}time_unix: String){
                OTPValidate(otpType: ${'$'}otpType, signature: ${'$'}signature, time_unix: ${'$'}time_unix) {
                        isSuccess
                        status
                        validateToken
                        errorMessage
                }
            }""".trimIndent()
    }
}