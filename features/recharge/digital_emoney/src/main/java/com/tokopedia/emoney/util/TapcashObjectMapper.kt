package com.tokopedia.emoney.util

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.data.BalanceTapcash

object TapcashObjectMapper {

    fun mapTapcashtoEmoney(tapcash: BalanceTapcash, balance: String = ""): EmoneyInquiry {
        val attributes = tapcash.rechargeUpdateBalance.attributes
        val error = tapcash.rechargeUpdateBalance.error
        val emoneyInquiry =  EmoneyInquiry(
                attributesEmoneyInquiry = AttributesEmoneyInquiry(
                        attributes.buttonText,
                        attributes.cardNumber,
                        attributes.imageIssuer,
                        if(balance.isEmpty()) attributes.amount else balance.toInt(radix = 16),
                        "",
                        1,
                        NFCUtils.formatCardUID(attributes.cardNumber),
                        issuer_id = 2,
                        operatorId = "1015",
                        pendingBalance = 0,
                ),
                error = EmoneyInquiryError(
                        error.id,
                        error.title,
                        error.status,
                        needAction = true
                )
        )

        return emoneyInquiry
    }
}