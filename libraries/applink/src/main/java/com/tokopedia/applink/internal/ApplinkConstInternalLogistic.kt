package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalLogistic {

    const val HOST_LOGISTIC = "logistic"
    const val INTERNAL_LOGISTIC = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_LOGISTIC"

    const val SHIPPING_CONFIRMATION = "$INTERNAL_LOGISTIC/shipping-confirmation/{mode}"

    const val ADD_ADDRESS_V1 = "$INTERNAL_LOGISTIC/addaddress/v1/{ref}/"

    const val ADD_ADDRESS_V2 = "$INTERNAL_LOGISTIC/addaddress/v2/"

    const val DROPOFF_PICKER = "$INTERNAL_LOGISTIC/dropoff/"

    const val ORDER_TRACKING = "$INTERNAL_LOGISTIC/shipping/tracking/"

    const val MANAGE_ADDRESS = "$INTERNAL_LOGISTIC/manageaddress/"

    const val SHOP_EDIT_ADDRESS = "$INTERNAL_LOGISTIC/editaddress/"

    const val EDIT_ADDRESS_REVAMP = "$INTERNAL_LOGISTIC/editaddressrevamp/"

    const val CUSTOM_PRODUCT_LOGISTIC = "$INTERNAL_LOGISTIC/customproductlogistic"

    @JvmField
    val ADD_ADDRESS_V3 = "$INTERNAL_LOGISTIC/addaddress/v3/"

    const val TNC_WEBVIEW = "$INTERNAL_LOGISTIC/logistictnc"

    const val PINPOINT_WEBVIEW = "$INTERNAL_LOGISTIC/pin-point-picker-result"

    const val RESCHEDULE_PICKUP = "$INTERNAL_LOGISTIC/reschedulepickup?order_id={order_id}"

    const val PROOF_OF_DELIVERY = "$INTERNAL_LOGISTIC/shipping/pod/{orderId}"

    const val PARAM_SOURCE = "source"

    const val MANAGE_ADDRESS_FROM_ACCOUNT = "$MANAGE_ADDRESS?$PARAM_SOURCE=account"

    const val RETURN_TO_SHIPPER = "$INTERNAL_LOGISTIC/returntoshipper?order_id={order_id}"
}
