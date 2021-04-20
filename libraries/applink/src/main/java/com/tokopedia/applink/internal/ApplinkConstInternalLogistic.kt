package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalLogistic {

    @JvmField
    val HOST_LOGISTIC = "logistic"
    @JvmField
    val INTERNAL_LOGISTIC = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_LOGISTIC"

    /**
     * @see logistic/tracking/.../ConfirmShippingActivity.java
     * @param {mode} to provide mode for shipping confirmation (confirm) or change courier (change)
     * @param EXTRA_ORDER_DETAIL_DATA [type: OrderDetailData (transaction-common)]
     * (mandatory intent param)
     */
    @JvmField
    val SHIPPING_CONFIRMATION = "$INTERNAL_LOGISTIC/shipping-confirmation/{mode}"

    /**
     * @see logistic/logisticaddaddress/.../AddAddressActivity.java
     * @param {ref} = caller id
     * @param token [type: Token (logisticCommon) ] token for District Recommendation api hit (mandatory)
     * @param PARAM_ADDRESS_MODEL [type: AddressModel (logisticCommon) ] address model if you want
     *             edit mode (optional)
    */
    @JvmField
    val ADD_ADDRESS_V1 = "$INTERNAL_LOGISTIC/addaddress/v1/{ref}/"

    /**
     * @see logistic/logisticaddaddress/../PinpointMapActivity.kt
     * @param token [type: Token (logisticCommon) ] token for Kero api hit (mandatory)
     * */
    @JvmField
    val ADD_ADDRESS_V2 = "$INTERNAL_LOGISTIC/addaddress/v2/"

    @JvmField
    val DROPOFF_PICKER = "$INTERNAL_LOGISTIC/dropoff/"

    @JvmField
    val ORDER_TRACKING = "$INTERNAL_LOGISTIC/shipping/tracking/"

    @JvmField
    val MANAGE_ADDRESS = "$INTERNAL_LOGISTIC/manageaddress/"

    @JvmField
    val SHOP_EDIT_ADDRESS = "$INTERNAL_LOGISTIC/editaddress/"

}