package com.tokopedia.vouchercreation.common.consts

import com.tokopedia.url.TokopediaUrl

object VoucherUrl {

    @JvmField
    val HOST = TokopediaUrl.getInstance().MOBILEWEB
    const val HELP = "help/article/voucher-toko-saya-tidak-dapat-dibuat"
    val HELP_URL = "$HOST$HELP"

    //These are default url for banners that we will use in case the server returned error
    const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
    const val FREE_DELIVERY_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_gratis_ongkir.png"
    const val CASHBACK_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback.png"
    const val CASHBACK_UNTIL_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback_hingga.png"
    const val POST_IMAGE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/ig_post.jpg"
    const val NO_VOUCHER_RESULT_URL = "https://ecs7.tokopedia.net/android/merchant_voucher/il_mvc_no_result.webp"
}