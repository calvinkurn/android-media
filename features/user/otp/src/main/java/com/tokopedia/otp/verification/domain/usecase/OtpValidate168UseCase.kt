package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.ParamOtpValidate168
import javax.inject.Inject

class OtpValidate168UseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ParamOtpValidate168, OtpValidatePojo>(dispatcher.io)  {
    override fun graphqlQuery(): String =
        """
            query otp_validate(
                ${'$'}code: String,
                ${'$'}otpType: String,
                ${'$'}msisdn: String,
                ${'$'}email: String,
                ${'$'}mode: String,
                ${'$'}ValidateToken: String
            ){
                OTPValidate(
                    code: ${'$'}code,
                    otpType: ${'$'}otpType,
                    msisdn: ${'$'}msisdn,
                    email: ${'$'}email,
                    mode: ${'$'}mode,
                    ValidateToken: ${'$'}ValidateToken
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

    override suspend fun execute(params: ParamOtpValidate168): OtpValidatePojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}