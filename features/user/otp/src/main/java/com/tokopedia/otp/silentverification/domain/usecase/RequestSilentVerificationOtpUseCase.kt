package com.tokopedia.otp.silentverification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 17/10/21.
 */

class RequestSilentVerificationOtpUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
    : CoroutineUseCase<Map<String, Any>, RequestSilentVerificationResponse>(Dispatchers.IO) {

    override suspend fun execute(params: Map<String, Any>): RequestSilentVerificationResponse {
        return repository.request(graphqlQuery(), params)
    }

    @JvmOverloads
    fun getParams(
        otpType: String,
        mode: String,
        msisdn: String = "",
        email: String = "",
        otpDigit: Int = 4
    ): Map<String, Any> = mapOf(
        PARAM_OTP_TYPE to otpType,
        PARAM_MODE to mode,
        PARAM_MSISDN to msisdn,
        PARAM_EMAIL to email,
        PARAM_OTP_DIGIT to otpDigit
    )

    override fun graphqlQuery(): String = """
        query otp_request(${'$'}otpType: String!, ${'$'}mode: String, ${'$'}msisdn: String, ${'$'}email: String, ${'$'}otpDigit: Int){
            OTPRequest(otpType: ${'$'}otpType, mode: ${'$'}mode, msisdn: ${'$'}msisdn, email: ${'$'}email, otpDigit: ${'$'}otpDigit) {
                success
                message
                errorMessage
                prefixMisscall
                message_title
                message_sub_title
                message_img_link
                evurl
                error_code
            }
        }
    """.trimIndent()

    companion object {
        const val PARAM_OTP_TYPE = "otpType"
        const val PARAM_MODE = "mode"
        const val PARAM_MSISDN = "msisdn"
        const val PARAM_EMAIL = "email"
        const val PARAM_OTP_DIGIT = "otpDigit"
    }
}