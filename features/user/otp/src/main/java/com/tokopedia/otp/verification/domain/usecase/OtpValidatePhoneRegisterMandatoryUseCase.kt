package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpValidatePhoneRegisterMandatoryParam
import javax.inject.Inject

class OtpValidatePhoneRegisterMandatoryUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<OtpValidatePhoneRegisterMandatoryParam, OtpValidatePojo>(dispatcher.io) {
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

    override suspend fun execute(params: OtpValidatePhoneRegisterMandatoryParam): OtpValidatePojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}
