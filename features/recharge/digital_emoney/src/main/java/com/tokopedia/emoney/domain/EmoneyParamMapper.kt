package com.tokopedia.emoney.domain

import com.tokopedia.common_electronic_money.data.EmoneyInquiryLogRequest
import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogRequest
import com.tokopedia.emoney.data.AttributesTapcash

object EmoneyParamMapper {

    private const val ISSUER_TAPCASH = 3

    fun mapParamLog(
        attributesTapcash: AttributesTapcash,
        errorMessage: String
    ): RechargeEmoneyInquiryLogRequest {
        return RechargeEmoneyInquiryLogRequest(
            log = EmoneyInquiryLogRequest(
                issueId = ISSUER_TAPCASH.toLong(),
                inquiryId = 0,
                cardNumber = attributesTapcash.cardNumber,
                rc = errorMessage,
                lastBalance = 0.0 //Hardcoded last balance
            )
        )
    }

    fun mapParamLogErrorNetwork(
        cardNumber: String,
        errorMessage: String
    ): RechargeEmoneyInquiryLogRequest {
        return RechargeEmoneyInquiryLogRequest(
            log = EmoneyInquiryLogRequest(
                issueId = ISSUER_TAPCASH.toLong(),
                inquiryId = 0,
                cardNumber = cardNumber,
                rc = errorMessage,
                lastBalance = 0.0 //Hardcoded last balance
            )
        )
    }
}
