package com.tokopedia.otp.silentverification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 17/10/21.
 */

class ValidateSilentVerificationUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
    : CoroutineUseCase<Map<String, Any>, OtpValidatePojo>(Dispatchers.IO) {

    override suspend fun execute(params: Map<String, Any>): OtpValidatePojo {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query otp_validate(
            ${'$'}code: String,
            ${'$'}otpType: String,
            ${'$'}msisdn: String,
            ${'$'}fpData: String,
            ${'$'}getSL: String,
            ${'$'}email: String,
            ${'$'}mode: String,
            ${'$'}signature: String,
            ${'$'}timeUnix: String,
            ${'$'}userId: Int,
        ){
            OTPValidate(
                code: ${'$'}code, 
                otpType: ${'$'}otpType, 
                msisdn: ${'$'}msisdn, 
                fpData: ${'$'}fpData, 
                getSL: ${'$'}getSL, 
                email: ${'$'}email, 
                mode: ${'$'}mode, 
                signature: ${'$'}signature, 
                time_unix: ${'$'}timeUnix, 
                UserID: ${'$'}userId
            ) {
                success
                message
                errorMessage
                validateToken
                cookieList {
                    key
                    value
                    expire
                }
            }
        }
    """.trimIndent()


    companion object {
        const val PARAM_CODE = "code"
        const val PARAM_OTP_TYPE = "otpType"
        const val PARAM_EMAIL = "email"
        const val PARAM_MODE = "mode"
        const val PARAM_USERID = "userId"
        const val PARAM_MSISDN = "msisdn"
        const val PARAM_ASSOCIATION_ID = "association_key"
    }
}