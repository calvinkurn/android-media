package com.tokopedia.withdraw.saldowithdrawal.constant

import com.tokopedia.url.TokopediaUrl

object WithdrawConstant {
    private val WEB_DOMAIN_URL = TokopediaUrl.getInstance().WEB

    val SALDO_LOCK_PAY_NOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran"
    const val WEB_TNC_URL = "https://m.tokopedia.com/terms/withdrawal-sla"

    const val MAX_WITHDRAWAL_INPUT_LENGTH = 14
}