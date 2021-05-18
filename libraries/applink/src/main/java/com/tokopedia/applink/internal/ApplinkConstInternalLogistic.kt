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

}