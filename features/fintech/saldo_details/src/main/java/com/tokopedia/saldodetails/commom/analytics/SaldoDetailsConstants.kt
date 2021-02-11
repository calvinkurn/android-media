package com.tokopedia.saldodetails.commom.analytics

import com.tokopedia.url.TokopediaUrl

import java.util.concurrent.TimeUnit

object SaldoDetailsConstants {

    private val WEB_DOMAIN_URL = TokopediaUrl.getInstance().WEB

    @JvmField
    val SALDO_HELP_URL = "https://www.tokopedia.com/help/browse/t-0057-saldo-tarik-dana?refid=st-1005"
    @JvmField
    val SALDOLOCK_PAYNOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran"
    val SALDO_HOLD_HELP_URL = "https://www.tokopedia.com/help/article/t-0057-saldo-tidak-dapat-digunakan?refid=st-1235"
    val SALDO_HOLD_HELP_URL_TWO =  "https://www.tokopedia.com/help/article/t-0057-saldo-direview?refid=st-1235"


    @JvmField
    val cacheDuration = TimeUnit.HOURS.toSeconds(1)

    internal interface Event {
        companion object {
            val EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite"
        }
    }

    internal interface Category {
        companion object {
            val SALDO_MAIN_SCREEN = "fin - android main screen"
            val FIN_SALDO_PAGE = "fin - saldo page"
        }
    }

    internal interface Action {
        companion object {
            val SALDO_ANCHOR_EVENT_ACTION = "sal - %s click"
            val SALDO_MODAL_TOKO_IMP = "saldo - modaltoko impression"
            val SALDO_MODAL_TOKO_CLICK = "saldo - modaltoko click"
            val SALDO_MODAL_TOKO_ACTION_CLICK = "saldo - modaltoko %s click"
        }
    }

    interface EventLabel {
        companion object {
            val SALDO_PAGE = "saldo page"
        }
    }
}
