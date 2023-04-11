package com.tokopedia.vouchercreation.common.consts

import com.tokopedia.imageassets.TokopediaImageUrl

import com.tokopedia.url.TokopediaUrl

object VoucherUrl {

    @JvmField
    val HOST = TokopediaUrl.getInstance().MOBILEWEB
    const val HELP = "help/article/voucher-toko-saya-tidak-dapat-dibuat"
    val HELP_URL = "$HOST$HELP"

    //These are default url for banners that we will use in case the server returned error
    const val BANNER_BASE_URL = TokopediaImageUrl.BANNER_BASE_URL
    const val FREE_DELIVERY_URL = TokopediaImageUrl.FREE_DELIVERY_URL
    const val CASHBACK_URL = TokopediaImageUrl.CASHBACK_URL
    const val CASHBACK_UNTIL_URL = TokopediaImageUrl.CASHBACK_UNTIL_URL
    const val POST_IMAGE_URL = TokopediaImageUrl.POST_IMAGE_URL
    const val NO_VOUCHER_RESULT_URL = "https://images.tokopedia.net/android/merchant_voucher/il_mvc_no_result.webp"
}
