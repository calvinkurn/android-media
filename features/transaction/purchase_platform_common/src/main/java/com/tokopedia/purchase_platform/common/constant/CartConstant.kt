package com.tokopedia.purchase_platform.common.constant

import com.tokopedia.imageassets.ImageUrl

/**
 * @author okasurya on 8/10/18.
 */
object CartConstant {
    const val CART_ERROR_GLOBAL = "Maaf, terjadi sedikit kendala. Coba ulangi beberapa saat lagi ya"
    const val IS_HAS_CART = "IS_HAS_CART"
    const val CART = "CART"
    const val CACHE_TOTAL_CART = "CACHE_TOTAL_CART"
    const val TERM_AND_CONDITION_URL = "https://www.tokopedia.com/asuransi/purchase-protection/syarat-dan-ketentuan/"
    const val PRIORITY_TNC_URL = "https://www.tokopedia.com/tnc-order-prioritas"
    const val SCREEN_NAME_CART_NEW_USER = "/user/address/create/cart"
    const val SCREEN_NAME_CART_EXISTING_USER = "/cart/address/create"
    const val CHECKOUT_LEASING_ID = "vehicle_leasing_id"
    const val CHECKOUT_IS_PLUS_SELECTED = "is_plus_selected"
    const val CART_EMPTY_DEFAULT_IMG_URL = ImageUrl.CART_EMPTY_DEFAULT_IMG_URL
    const val CART_EMPTY_WITH_PROMO_IMG_URL = ImageUrl.CART_EMPTY_WITH_PROMO_IMG_URL
    const val STATE_RED = "red"
    const val PARAM_DEFAULT = "default"
    const val PARAM_CART = "cart"
    const val IS_TESTING_FLOW = "isTesting"
}
