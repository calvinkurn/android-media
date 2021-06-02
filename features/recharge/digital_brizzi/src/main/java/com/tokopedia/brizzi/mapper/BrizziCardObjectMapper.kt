package com.tokopedia.brizzi.mapper

import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.brizzi.viewmodel.BrizziBalanceViewModel
import id.co.bri.sdk.BrizziCardObject
import javax.inject.Inject

class BrizziCardObjectMapper @Inject constructor() {

    fun mapperBrizzi(brizziCardObject: BrizziCardObject, error: EmoneyInquiryError): EmoneyInquiry {
        return EmoneyInquiry(
                attributesEmoneyInquiry = AttributesEmoneyInquiry(
                        "Top-up Sekarang",
                        brizziCardObject.cardNumber,
                        "https://ecs7.tokopedia.net/img/recharge/operator/brizzi.png",
                        brizziCardObject.balance.toInt(),
                        "",
                        1,
                        NFCUtils.formatCardUID(brizziCardObject.cardNumber),
                        BrizziBalanceViewModel.ISSUER_ID_BRIZZI,
                        BrizziBalanceViewModel.ETOLL_BRIZZI_OPERATOR_ID,
                        brizziCardObject.pendingBalance.toInt()
                ),
                error = error)
    }
}