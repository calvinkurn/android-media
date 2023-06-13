package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.pojo.OtpRequestPhoneRegisterMandatoryParam
import javax.inject.Inject

class SendOtpPhoneRegisterMandatoryUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<OtpRequestPhoneRegisterMandatoryParam, OtpRequestPojo>(dispatcher.io) {
    override fun graphqlQuery(): String =
        """
            query otp_request(
                ${'$'}otpType: String!,
                ${'$'}mode: String,
                ${'$'}msisdn: String,
                ${'$'}email: String,
                ${'$'}otpDigit: Int,
                ${'$'}ValidateToken: String
            ){
                OTPRequest(
                    otpType: ${'$'}otpType,
                    mode: ${'$'}mode,
                    msisdn: ${'$'}msisdn,
                    email: ${'$'}email,
                    otpDigit: ${'$'}otpDigit,
                    ValidateToken: ${'$'}ValidateToken
                ) {
                    success
                    message
                    errorMessage
                    prefixMisscall
                    message_title
                    message_sub_title
                    message_img_link
                    error_code
                }
            }
        """.trimIndent()

    override suspend fun execute(params: OtpRequestPhoneRegisterMandatoryParam): OtpRequestPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}
