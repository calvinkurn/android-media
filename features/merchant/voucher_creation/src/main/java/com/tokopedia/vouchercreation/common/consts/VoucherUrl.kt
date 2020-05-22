package com.tokopedia.vouchercreation.common.consts

import com.tokopedia.url.TokopediaUrl

object VoucherUrl {

    @JvmField
    val HOST = TokopediaUrl.getInstance().MOBILEWEB
    const val HELP = "help/browse/t-1004-promosi"
    val HELP_URL = "$HOST$HELP"
}