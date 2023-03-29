package com.tokopedia.emoney.domain.response

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils

object JakCardResponseMapper {
    fun jakCardResponseMapper(jakCardResponse: JakCardResponse): EmoneyInquiry {
        val attributes = jakCardResponse.data.attributes
        return EmoneyInquiry(
            attributesEmoneyInquiry = AttributesEmoneyInquiry(
                buttonText = attributes.buttonText,
                cardNumber = attributes.cardNumber,
                imageIssuer = attributes.imageIssuer,
                lastBalance = attributes.lastBalance,
                payload = "",
                status = jakCardResponse.data.status,
                formattedCardNumber = NFCUtils.formatCardUID(attributes.cardNumber),
                issuer_id = 4,
                pendingBalance = 0

            ),
            error = EmoneyInquiryError(
                "",
                attributes.message,
                jakCardResponse.data.status
            )
        )
    }
}
