package com.tokopedia.saldodetails.commom.analytics

import com.tokopedia.url.TokopediaUrl

import java.util.concurrent.TimeUnit

object SaldoDetailsConstants {

    private val WEB_DOMAIN_URL = TokopediaUrl.getInstance().WEB

    const val SALDO_HELP_URL = "https://www.tokopedia.com/help/browse/t-0057-saldo-tarik-dana?refid=st-1005"
    @JvmField
    val SALDOLOCK_PAYNOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran"
    const val SALDO_HOLD_HELP_URL = "https://www.tokopedia.com/help/article/t-0057-saldo-tidak-dapat-digunakan?refid=st-1235"
    const val SALDO_HOLD_HELP_URL_TWO =  "https://www.tokopedia.com/help/article/t-0057-saldo-direview?refid=st-1235"


    @JvmField
    val cacheDuration = TimeUnit.HOURS.toSeconds(1)
    const val KEY_CURRENT_SITE = "currentSite"
    const val VALUE_CURRENT_SITE = "tokopediamarketplace"
    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val VALUE_BUSINESS_UNIT = "Payment"
    const val KEY_USER_ID = "userId"

    internal interface Event {
        companion object {
            const val EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite"
            const val EVENT_CLICK_SALDO = "clickSaldo"
            const val EVENT_CLICK_PAYMENT = "clickPayment"
            const val EVENT_SALDO_IMPRESSION = "viewSaldoIris"
            const val EVENT_SALDO_OPEN_SCREEN= "openScreen"
            const val EVENT= "event"
        }
    }

    internal interface Category {
        companion object {
            const val SALDO_MAIN_SCREEN = "fin - android main screen"
            const val FIN_SALDO_PAGE = "fin - saldo page"
            const val SALDO_PAGE = "saldo page"
        }
    }

    internal interface Action {
        companion object {
            const val SALDO_ANCHOR_EVENT_ACTION = "sal - %s click"
            const val SALDO_HOLD_STATUS_CLICK = "click status on hold"
            const val SALDO_HELP_HOLD_CLICK = "click bantuan on hold page"
            const val SALDO_WITHDRAWAL_CLICK = "click tarik saldo"
            const val SALDO_MODAL_TOKO_IMP = "saldo - modaltoko impression"
            const val SALDO_MODAL_TOKO_CLICK = "saldo - modaltoko click"
            const val SALDO_MODAL_TOKO_ACTION_CLICK = "saldo - modaltoko %s click"
            const val SALDO_PENGHASILAN_TAB_CLICK = "click saldo penghasilan tab"
            const val SALDO_REFUND_TAB_CLICK = "click saldo refund tab"
            const val SALDO_PENJUALAN_TAB_CLICK = "click tab penjualan"
            const val SALDO_SEMUA_FILTER_CLICK = "click sub tab semua"
            const val SALDO_SEMUA_TAB_CLICK = "click tab semua transaksi"
            const val SALDO_PENGHASILAN_DETAIL_CLICK = "click detail penarikan saldo penghasilan"
            const val SALDO_REFUND_DETAIL_CLICK = "click detail penarikan saldo refund"
            const val SALDO_SALES_DETAIL_CLICK = "click detail hasil penjualan"
            const val SALDO_INVOICE_NUMBER_CLICK = "click invoice number"
            const val SALDO_INVOICE_DETAIL_CLICK = "click detail hasil pesanan"
            const val SALDO_API_FAILED = "API failed"
        }
    }

    interface EventLabel {
        companion object {
            const val SALDO_PAGE = "saldo page"
            const val SALDO_FETCH_SALES_DETAIL = "fetch sales detail"
            const val SALDO_FETCH_SALES_LIST = "fetch sales list"
            const val SALDO_FETCH_WITHDRAWAL_DETAIL = "fetch withdrawal detail"
            const val SALDO_FETCH_WITHDRAWAL_LIST = "fetch withdrawal list"
            const val SALDO_FETCH_BALANCE = "fetch info saldo balance"
        }
    }

    interface DetailScreenParams {
        companion object {
            const val WITHDRAWAL_ID = "withdrawalID"
            const val SUMMARY_ID = "summaryID"
        }
    }
}
