package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalDigital {

    const val HOST_DIGITAL = "digital"
    const val HOST_RECHARGE = "recharge"
    const val HOME_RECHARGE = "home"

    const val PARAM_SMARTCARD = "calling_page_check_saldo"
    const val PARAM_BRIZZI = "brizzi_page"

    const val INTERNAL_DIGITAL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DIGITAL"
    const val INTERNAL_RECHARGE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_RECHARGE"

    //new cart checkout applink
    const val CHECKOUT_DIGITAL = "$INTERNAL_DIGITAL/checkout"
    //old cart checkout applink
    const val CART_DIGITAL = "$INTERNAL_DIGITAL/cart"

    const val TELCO_PREPAID_DIGITAL = "$INTERNAL_DIGITAL/telcopre"
    const val TELCO_POSTPAID_DIGITAL = "$INTERNAL_DIGITAL/telcopost"
    const val VOUCHER_GAME = "$INTERNAL_DIGITAL/vouchergame"
    const val GENERAL_TEMPLATE = "$INTERNAL_DIGITAL/general"
    const val CAMERA_OCR = "$INTERNAL_RECHARGE/ocr"
    const val CREDIT_CARD_TEMPLATE = "$INTERNAL_RECHARGE/cc"
    const val SMART_BILLS = "$INTERNAL_RECHARGE/bayarsekaligus"
    const val SUBHOMEPAGE = "$INTERNAL_RECHARGE/home"
    const val DYNAMIC_SUBHOMEPAGE = "$INTERNAL_RECHARGE/home/dynamic"

    const val SMARTCARD = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_DIGITAL/smartcard?$PARAM_SMARTCARD={type}&$PARAM_BRIZZI={status}"
    const val INTERNAL_SMARTCARD_EMONEY = "$INTERNAL_DIGITAL/smartcard/emoney?$PARAM_SMARTCARD={type}"
    const val INTERNAL_SMARTCARD_BRIZZI = "$INTERNAL_DIGITAL/smartcard/brizzi?$PARAM_SMARTCARD={type}"

    const val DIGITAL_PRODUCT_FORM = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DIGITAL/form"
    const val DIGITAL_PRODUCT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DIGITAL/form?category_id={category_id}&operator_id={operator_id}"
    const val PRODUCT_TEMPLATE = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_DIGITAL/form?operator={operator_id}&menu_id={menu_id}&template={template}"

    const val APPLINK_RECHARGE_SLICE = "tokopedia-android-internal://recharge_slice/main"
}