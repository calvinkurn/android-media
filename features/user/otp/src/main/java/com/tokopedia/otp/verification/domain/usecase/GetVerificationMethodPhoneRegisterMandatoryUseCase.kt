package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.pojo.GetVerificationMethodPhoneRegisterMandatoryParam
import javax.inject.Inject

class GetVerificationMethodPhoneRegisterMandatoryUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetVerificationMethodPhoneRegisterMandatoryParam, OtpModeListPojo>(dispatcher.io) {
    override fun graphqlQuery(): String =
        """
            query otp_mode_list(
                ${'$'}otpType: String!,
                ${'$'}msisdn: String,
                ${'$'}email: String,
                ${'$'}ValidateToken:
                String
            ) {
                OTPModeList(
                    otpType: ${'$'}otpType,
                    msisdn: ${'$'}msisdn,
                    email: ${'$'}email,
                    ValidateToken: ${'$'}ValidateToken
                ) {
                    success
                    message
                    errorMessage
                    otpDigit
                    linkType
                    enableTicker
                    tickerTrouble
                    modeLists {
                        modeCode
                        modeText
                        otpListText
                        afterOtpListText
                        afterOtpListTextHtml
                        otpListImgUrl
                        usingPopUp
                        popUpHeader
                        popUpBody
                        countdown
                        otpDigit
                    }
                }
            }
        """.trimIndent()

    override suspend fun execute(params: GetVerificationMethodPhoneRegisterMandatoryParam): OtpModeListPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}
