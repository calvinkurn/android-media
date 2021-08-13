package com.tokopedia.saldodetails.commom.analytics.analytics

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
    val KEY_CURRENT_SITE = "currentSite"
    val VALUE_CURRENT_SITE = "tokopediamarketplace"
    val KEY_BUSINESS_UNIT = "businessUnit"
    val VALUE_BUSINESS_UNIT = "Payment"
    val KEY_USER_ID = "userId"

    internal interface Event {
        companion object {
            val EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite"
            val EVENT_CLICK_SALDO = "clickSaldo"
            val EVENT_SALDO_IMPRESSION = "viewSaldoIris"
        }
    }

    internal interface Category {
        companion object {
            val SALDO_MAIN_SCREEN = "fin - android main screen"
            val FIN_SALDO_PAGE = "fin - saldo page"
            val SALDO_PAGE = "saldo page"
        }
    }

    internal interface Action {
        companion object {
            val SALDO_ANCHOR_EVENT_ACTION = "sal - %s click"
            val SALDO_MODAL_TOKO_IMP = "saldo - modaltoko impression"
            val SALDO_MODAL_TOKO_CLICK = "saldo - modaltoko click"
            val SALDO_MODAL_TOKO_ACTION_CLICK = "saldo - modaltoko %s click"
            val SALDO_PENGHASILAN_TAB_CLICK = "click saldo penghasilan tab"
            val SALDO_REFUND_TAB_CLICK = "click saldo refund tab"
            val SALDO_PENGHASILAN_DETAIL_CLICK = "click detail penarikan saldo penghasilan"
            val SALDO_REFUND_DETAIL_CLICK = "click detail penarikan saldo refund"
            val SALDO_SALES_DETAIL_CLICK = "click detail hasil penjualan"
            val SALDO_INVOICE_NUMBER_CLICK = "click invoice number"
            val SALDO_INVOICE_DETAIL_CLICK = "click detail hasil pesanan"
            val SALDO_API_FAILED = "API failed"
        }
    }

    interface EventLabel {
        companion object {
            val SALDO_PAGE = "saldo page"
            val SALDO_FETCH_SALES_DETAIL = "fetch sales detail"
            val SALDO_FETCH_SALES_LIST = "fetch sales list"
            val SALDO_FETCH_WITHDRAWAL_DETAIL = "fetch withdrawal detail"
            val SALDO_FETCH_WITHDRAWAL_LIST = "fetch withdrawal list"
            val SALDO_FETCH_BALANCE = "fetch info saldo balance"
        }
    }

    interface DetailScreenParams {
        companion object {
            val WITHDRAWAL_ID = "withdrawalID"
            val SUMMARY_ID = "summaryID"
        }
    }
}
