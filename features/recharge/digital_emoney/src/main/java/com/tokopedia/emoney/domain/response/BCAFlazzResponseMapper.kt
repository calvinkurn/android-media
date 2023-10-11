package com.tokopedia.emoney.domain.response

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils

object BCAFlazzResponseMapper {
    private const val ISSUER_ID_BCA = 5
    fun bcaMapper(cardNo: String, balance: Int, imageUrl: String, isBCAGenOne: Boolean, pendingBalance: Int,
                  status: Int, message: String): EmoneyInquiry {
        return EmoneyInquiry(
            attributesEmoneyInquiry = AttributesEmoneyInquiry(
                buttonText = "Top Up Sekarang",
                cardNumber = cardNo,
                imageIssuer = imageUrl,
                lastBalance = balance,
                payload = "",
                status = status,
                formattedCardNumber = NFCUtils.formatCardUID(cardNo),
                issuer_id = ISSUER_ID_BCA,
                pendingBalance = pendingBalance,
                extraPendingBalance = pendingBalance > 0,
                showAdditionalBalance = false,
            ),
            isBCAGenOne = isBCAGenOne,
            error = EmoneyInquiryError(
                "",
                message,
                status
            )
        )
    }

    fun bcaMTId(merchantId: String, terminalId: String): String {
        return "$terminalId,$merchantId"
    }
}
