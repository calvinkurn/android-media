package com.tokopedia.vouchercreation.common.consts

import com.tokopedia.imageassets.ImageUrl

import com.tokopedia.url.TokopediaUrl

object VoucherUrl {

    @JvmField
    val HOST = TokopediaUrl.getInstance().MOBILEWEB
    const val HELP = "help/article/voucher-toko-saya-tidak-dapat-dibuat"
    val HELP_URL = "$HOST$HELP"

    //These are default url for banners that we will use in case the server returned error
    const val BANNER_BASE_URL = ImageUrl.BANNER_BASE_URL
    const val FREE_DELIVERY_URL = ImageUrl.FREE_DELIVERY_URL
    const val CASHBACK_URL = ImageUrl.CASHBACK_URL
    const val CASHBACK_UNTIL_URL = ImageUrl.CASHBACK_UNTIL_URL
    const val POST_IMAGE_URL = ImageUrl.POST_IMAGE_URL
    const val NO_VOUCHER_RESULT_URL = "https://images.tokopedia.net/android/merchant_voucher/il_mvc_no_result.webp"
}
