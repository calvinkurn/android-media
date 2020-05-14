package com.tokopedia.emoney.view.mapper

import com.tokopedia.emoney.util.NFCUtils
import com.tokopedia.emoney.data.AttributesEmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiryError
import com.tokopedia.emoney.viewmodel.BrizziBalanceViewModel
import id.co.bri.sdk.BrizziCardObject
import javax.inject.Inject

class BrizziCardObjectMapper @Inject constructor() {

    fun mapperBrizzi(brizziCardObject: BrizziCardObject, error: EmoneyInquiryError): EmoneyInquiry {
        return EmoneyInquiry(
                attributesEmoneyInquiry = AttributesEmoneyInquiry(
                        "Topup Sekarang",
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