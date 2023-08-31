package com.tokopedia.emoney.domain.response

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.integration.data.JNIResult
import com.tokopedia.kotlin.extensions.view.ZERO

object BCAResponseMapper {
    private const val ISSUER_ID_BCA = 5
    fun bcaMapper(result: JNIResult, isBCAGenOne: Boolean, isExtraPendingBalance: Boolean = false, pendingBalance: Int): EmoneyInquiry {
        return EmoneyInquiry(
            attributesEmoneyInquiry = AttributesEmoneyInquiry(
                buttonText = "Top Up Sekarang",
                cardNumber = result.cardNo,
                imageIssuer = "https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png",
                lastBalance = result.balance,
                payload = "",
                status = 1,
                formattedCardNumber = NFCUtils.formatCardUID(result.cardNo),
                issuer_id = ISSUER_ID_BCA,
                pendingBalance = pendingBalance,
                extraPendingBalance = isExtraPendingBalance,
                showAdditionalBalance = true
            ),
            isBCAGenOne = isBCAGenOne
        )
    }

    fun bcaMTId(merchantId: String, terminalId: String): String {
        return "$terminalId,$merchantId"
    }
}
