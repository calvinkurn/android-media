package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.verification.domain.pojo.GetOtpModeListParam
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import javax.inject.Inject

class   GetOtpModeListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetOtpModeListParam, OtpModeListData>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query otp_mode_list(${'$'}otpType: String!, ${'$'}userId: String, ${'$'}msisdn: String, ${'$'}email: String, ${'$'}Timeunix: String, ${'$'}AuthenticitySignature: String){
            OTPModeList(otpType: ${'$'}otpType, userID: ${'$'}userId, msisdn: ${'$'}msisdn, email:  ${'$'}email, Timeunix: ${'$'}Timeunix, AuthenticitySignature: ${'$'}AuthenticitySignature) {
                success
                message
                errorMessage
                otpDigit
                linkType
                enableTicker
                tickerTrouble
                defaultMode
                defaultBehaviorMode
                switchAlternativeMethodTime
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
                    directRequest
                    otpDigit
                }
            }
        }
    """.trimIndent()

    override suspend fun execute(params: GetOtpModeListParam): OtpModeListData {
        val result: OtpModeListPojo = repository.request(graphqlQuery(), params)
        when {
            result.data.success -> {
                return result.data
            }
            result.data.errorMessage.isNotEmpty() -> {
                throw MessageErrorException(result.data.errorMessage)
            }
        }
        return repository.request(graphqlQuery(), repository)
    }
}
