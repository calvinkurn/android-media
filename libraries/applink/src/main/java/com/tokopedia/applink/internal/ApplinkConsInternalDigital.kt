package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalDigital {

    const val HOST_DIGITAL = "digital"
    const val HOST_RECHARGE = "recharge"
    const val HOME_RECHARGE = "home"

    const val PARAM_SMARTCARD = "calling_page_check_saldo"

    const val INTERNAL_DIGITAL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DIGITAL"
    const val INTERNAL_RECHARGE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_RECHARGE"

    const val CART_DIGITAL = "$INTERNAL_DIGITAL/cart"
    const val TELCO_DIGITAL = "$INTERNAL_DIGITAL/telco"
    const val VOUCHER_GAME = "$INTERNAL_DIGITAL/vouchergame"
    const val GENERAL_TEMPLATE = "$INTERNAL_DIGITAL/general"
    const val CAMERA_OCR = "$INTERNAL_RECHARGE/ocr"

    const val INTERNAL_SMARTCARD = "$INTERNAL_DIGITAL/smartcard?$PARAM_SMARTCARD={type}"

    const val DIGITAL_PRODUCT_FORM = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DIGITAL/form"
    const val DIGITAL_PRODUCT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DIGITAL/form?category_id={category_id}&operator_id={operator_id}"
    const val PRODUCT_TEMPLATE = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_DIGITAL/form?category_id={category_id}&menu_id={menu_id}&template={template}"
}