package com.tokopedia.vouchercreation.common.consts

import com.tokopedia.url.TokopediaUrl

object VoucherUrl {

    @JvmField
    val HOST = TokopediaUrl.getInstance().MOBILEWEB
    const val HELP = "help/article/voucher-toko-saya-tidak-dapat-dibuat"
    val HELP_URL = "$HOST$HELP"
}