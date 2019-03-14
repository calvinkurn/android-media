package com.tokopedia.common_digital.common.constant

/**
 * Created by Rizky on 13/08/18.
 */
object DigitalUrl {

    var DIGITAL_API_DOMAIN = "https://pulsa-api.tokopedia.com/"

    val VERSION = "v1.4/"
    val HMAC_KEY = "web_service_v4"

    val PATH_STATUS = "status"
    val PATH_CATEGORY_LIST = "category/list"
    val PATH_CATEGORY = "category"
    val PATH_OPERATOR = "operator/list"
    val PATH_PRODUCT = "product/list"
    val PATH_FAVORITE_LIST = "favorite/list"
    val PATH_SALDO = "saldo"
    val PATH_GET_CART = "cart"
    val PATH_PATCH_OTP_SUCCESS = "cart/otp-success"
    val PATH_ORDER = "order"
    val PATH_ADD_TO_CART = "cart"
    val PATH_CHECKOUT = "checkout"
    val PATH_CHECK_VOUCHER = "voucher/check"
    val PATH_CANCEL_VOUCHER = "voucher/cancel"
    val PATH_USSD = "ussd/balance"
    val PATH_SMARTCARD_INQUIRY = "smartcard/inquiry"
    val PATH_SMARTCARD_COMMAND = "smartcard/command"

    val BASE_URL = DIGITAL_API_DOMAIN + VERSION

    var WEB_DOMAIN = "https://www.tokopedia.com/"
    var DIGITAL_BANTUAN = WEB_DOMAIN + "bantuan/produk-digital/"

    object HelpUrl {
        var PULSA = DIGITAL_BANTUAN + "pulsa/"
        var PAKET_DATA = DIGITAL_BANTUAN + "pulsa/"
        var PLN = DIGITAL_BANTUAN + "listrik-pln/"
        var BPJS = DIGITAL_BANTUAN + "bpjs/"
        var PDAM = DIGITAL_BANTUAN + "air-pdam/"
        var GAME = DIGITAL_BANTUAN + "voucher-game/"
        var CREDIT = DIGITAL_BANTUAN + "angsuran-kredit/"
        var TV = DIGITAL_BANTUAN + "tv-kabel/"
        var POSTPAID = DIGITAL_BANTUAN + "seluler-pascabayar/"
        var TELKOM = DIGITAL_BANTUAN + "telepon/"
        var STREAMING = DIGITAL_BANTUAN + "voucher-streaming/"
        var PGN = DIGITAL_BANTUAN + "gas/"
        var ROAMING = DIGITAL_BANTUAN + "roaming/"
        var TAX = DIGITAL_BANTUAN + "pajak/"
        var GIFT_CARD = DIGITAL_BANTUAN + "voucher-gift-card/"
        var RETRIBUTION = DIGITAL_BANTUAN + "retribusi/"
        var MTIX = DIGITAL_BANTUAN + "m-tix-xxi/"
        var CREDIT_CARD = DIGITAL_BANTUAN + "tagihan-kartu-kredit/"
        var ETOLL = DIGITAL_BANTUAN + "e-money/#cara-update-saldo-kartu"
    }


}
