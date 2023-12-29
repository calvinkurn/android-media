package com.tokopedia.emoney.domain.response

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.domain.request.JakCardAction
import com.tokopedia.emoney.domain.request.JakCardStatus
import com.tokopedia.kotlin.extensions.view.ZERO

object JakCardResponseMapper {

    private const val ISSUER_ID_JAKCARD = 4
    fun jakCardResponseMapper(jakCardResponse: JakCardData): EmoneyInquiry {
        val attributes = jakCardResponse.attributes
        return EmoneyInquiry(
            attributesEmoneyInquiry = AttributesEmoneyInquiry(
                buttonText = attributes.buttonText,
                cardNumber = attributes.cardNumber,
                imageIssuer = attributes.imageIssuer,
                lastBalance = attributes.lastBalance,
                payload = "",
                status = jakCardResponse.status,
                formattedCardNumber = NFCUtils.formatCardUID(attributes.cardNumber),
                issuer_id = ISSUER_ID_JAKCARD,
                pendingBalance = Int.ZERO

            ),
            error = EmoneyInquiryError(
                "",
                attributes.message,
                jakCardResponse.status
            )
        )
    }

    fun jakCardResponseConfirmationFailed(jakCardResponse: JakCardData, lastBalanceAfterUpdate: Int): JakCardData {
        return jakCardResponse.apply {
            action = JakCardAction.TOP_UP_CONFIRMATION.action
            status = JakCardStatus.DONE.status
            attributes.lastBalance = lastBalanceAfterUpdate
        }
    }
}
