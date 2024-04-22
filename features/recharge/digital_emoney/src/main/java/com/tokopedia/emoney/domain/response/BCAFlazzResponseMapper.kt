package com.tokopedia.emoney.domain.response

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.domain.request.BCAFlazzStatus
import com.tokopedia.kotlin.extensions.view.ZERO

object BCAFlazzResponseMapper {
    private const val ISSUER_ID_BCA = 5
    fun bcaMapper(cardNo: String, balance: Int, imageUrl: String, isBCAGenOne: Boolean, pendingBalance: Int,
                  status: Int, message: String, hasMorePendingBalance: Boolean, ackStatusOverride: Boolean = false,
                  messageBCAGen1: String = "", messageTopUp2: String = "", isErrorTopUp2: Boolean = false
    ): EmoneyInquiry {
        return EmoneyInquiry(
            attributesEmoneyInquiry = AttributesEmoneyInquiry(
                buttonText = "Top-Up Sekarang",
                cardNumber = cardNo,
                imageIssuer = imageUrl,
                lastBalance = balance,
                payload = "",
                status = if (ackStatusOverride) BCAFlazzStatus.DONE.status else status,
                formattedCardNumber = NFCUtils.formatCardUID(cardNo),
                issuer_id = ISSUER_ID_BCA,
                pendingBalance = pendingBalance,
                extraPendingBalance = hasMorePendingBalance,
                showAdditionalBalance = (status == BCAFlazzStatus.DONE.status && pendingBalance > Int.ZERO),
            ),
            isBCAGenOne = isBCAGenOne,
            messageBCAGen1 = if (isBCAGenOne) messageBCAGen1 else "",
            messageTopUp2 = messageTopUp2,
            isErrorTopUp2 = isErrorTopUp2,
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
