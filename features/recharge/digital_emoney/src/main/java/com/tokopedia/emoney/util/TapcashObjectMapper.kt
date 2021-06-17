package com.tokopedia.emoney.util

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.data.BalanceTapcash

object TapcashObjectMapper {

    fun mapTapcashtoEmoney(tapcash: BalanceTapcash): EmoneyInquiry {
        val attributes = tapcash.rechargeUpdateBalance.attributes
        val error = tapcash.rechargeUpdateBalance.error
        val emoneyInquiry =  EmoneyInquiry(
                attributesEmoneyInquiry = AttributesEmoneyInquiry(
                        attributes.buttonText,
                        attributes.cardNumber,
                        attributes.imageIssuer,
                        attributes.amount,
                        "",
                        1,
                        NFCUtils.formatCardUID(attributes.cardNumber),
                        2, //todo issuerid
                "1015", //todo etoll operator id
                0, //todo pending balance
                ),
                error = EmoneyInquiryError(
                        error.id,
                        error.title,
                        error.status,
                        needAction = true // todo need action
                )
        )

        return emoneyInquiry
    }
}