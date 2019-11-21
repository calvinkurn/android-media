package com.tokopedia.emoney.view.electronicmoney

import android.content.Intent
import com.tokopedia.emoney.NFCUtils
import com.tokopedia.emoney.R
import com.tokopedia.emoney.data.AttributesEmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiryError
import id.co.bri.sdk.Brizzi
import id.co.bri.sdk.BrizziCardObject
import id.co.bri.sdk.Callback
import id.co.bri.sdk.exception.BrizziException

class BrizziCheckBalance(val brizziInstance: Brizzi, val listener: BrizziActionListener) : BriElectronicMoney {

    override fun processTagIntent(intent: Intent) {
        listener.setIssuerId(ISSUER_ID_BRIZZI)
        brizziInstance.getBalanceInquiry(intent, object : Callback {
            override fun OnFailure(brizziException: BrizziException?) {
                brizziException?.let {
                    handleError(it, intent)
                }
            }

            override fun OnSuccess(brizziCardObject: BrizziCardObject) {
                val emoneyInquiry = mapperBrizzi(brizziCardObject, EmoneyInquiryError(title = "Tidak ada pending balance"))
                listener.logBrizziStatus(true, emoneyInquiry)

                if (brizziCardObject.pendingBalance.toInt() == 0) {
                    listener.onSuccess(emoneyInquiry)
                } else {
                    sendCommandToCard(intent)
                }
            }
        })
    }

    private fun mapperBrizzi(brizziCardObject: BrizziCardObject, error: EmoneyInquiryError): EmoneyInquiry {
        return EmoneyInquiry(
                attributesEmoneyInquiry = AttributesEmoneyInquiry(
                        "Topup Sekarang",
                        brizziCardObject.cardNumber,
                        "https://ecs7.tokopedia.net/img/recharge/operator/brizzi.png",
                        brizziCardObject.balance.toInt(),
                        "",
                        1,
                        NFCUtils.formatCardUID(brizziCardObject.cardNumber),
                        ISSUER_ID_BRIZZI,
                        ETOLL_BRIZZI_OPERATOR_ID
                ),
                error = error)
    }

    private fun handleError(brizziException: BrizziException, intent: Intent) {
        when {
            brizziException.errorCode == BRIZZI_TOKEN_EXPIRED -> listener.processGetBalanceBrizzi(true, intent)
            brizziException.errorCode == BRIZZI_CARD_NOT_FOUND -> listener.onErrorCardNotFound(ISSUER_ID_BRIZZI, intent)
            else -> listener.onError(R.string.emoney_update_balance_failed)
        }
    }

    override fun sendCommandToCard(intent: Intent) {
        brizziInstance.doUpdateBalance(intent, System.currentTimeMillis().toString(), object : Callback {
            override fun OnFailure(brizziException: BrizziException?) {
                brizziException?.let {
                    handleError(it, intent)
                }
            }

            override fun OnSuccess(brizziCardObject: BrizziCardObject) {
                val emoneyInquiry = mapperBrizzi(brizziCardObject, EmoneyInquiryError(title = "Informasi saldo berhasil diperbarui"))
                listener.logBrizziStatus(false, emoneyInquiry)
                listener.onSuccess(emoneyInquiry)
            }
        })
    }

    companion object {
        const val ISSUER_ID_BRIZZI = 2
        const val ETOLL_BRIZZI_OPERATOR_ID = "1015"
        const val BRIZZI_TOKEN_EXPIRED = "61"
        const val BRIZZI_CARD_NOT_FOUND = "21"
    }
}