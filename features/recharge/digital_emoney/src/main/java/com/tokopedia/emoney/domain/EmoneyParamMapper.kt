package com.tokopedia.emoney.domain

import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogRequest
import com.tokopedia.emoney.data.AttributesTapcash

object EmoneyParamMapper {

    private const val ISSUER_TAPCASH = 3

    fun mapParamLog(attributesTapcash: AttributesTapcash, errorMessage: String): RechargeEmoneyInquiryLogRequest {
        return RechargeEmoneyInquiryLogRequest(
            issueId = ISSUER_TAPCASH.toLong(),
            inquiryId = 0,
            cardNumber = attributesTapcash.cardNumber,
            rc = errorMessage,
            lastBalance = attributesTapcash.amount.toDouble()
        )
    }
}
