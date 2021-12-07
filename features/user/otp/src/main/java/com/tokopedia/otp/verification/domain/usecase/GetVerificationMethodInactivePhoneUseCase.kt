package com.tokopedia.otp.verification.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import javax.inject.Inject

/**
 * @author by @rival on 10/010/21.
 */

open class GetVerificationMethodInactivePhoneUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<InactivePhoneVerificationMethodeParams, OtpModeListPojo>(dispatcher.io) {

    override suspend fun execute(params: InactivePhoneVerificationMethodeParams): OtpModeListPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query otp_mode_list(${'$'}otpType: String!, ${'$'}userId: String, ${'$'}msisdn: String, ${'$'}email: String, ${'$'}ValidateToken: String, ${'$'}UserIDEnc: String){
                OTPModeList(otpType: ${'$'}otpType, userID: ${'$'}userId, msisdn: ${'$'}msisdn, email: ${'$'}email, ValidateToken: ${'$'}ValidateToken, UserIDEnc: ${'$'}UserIDEnc) {
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
    }
}

data class InactivePhoneVerificationMethodeParams(
    @SerializedName("otpType")
    var otpType: String = "",
    @SerializedName("userId")
    var userId: String = "",
    @SerializedName("msisdn")
    var msisdn: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("ValidateToken")
    var validateToken: String = "",
    @SerializedName("UserIDEnc")
    var userIDEnc: String = "",
): GqlParam