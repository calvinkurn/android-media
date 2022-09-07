package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.pojo.ParamGetModeList168
import javax.inject.Inject

class GetModeList168UseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ParamGetModeList168, OtpModeListPojo>(dispatcher.io) {
    override fun graphqlQuery(): String =
        """
            query otp_mode_list(${'$'}otpType: String!, ${'$'}msisdn: String, ${'$'}email: String, ${'$'}ValidateToken: String){
                OTPModeList(otpType: ${'$'}otpType, msisdn: ${'$'}msisdn, email: ${'$'}email, ValidateToken: ${'$'}ValidateToken) {
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

    override suspend fun execute(params: ParamGetModeList168): OtpModeListPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}