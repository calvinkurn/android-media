package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalLogistic {

    @JvmField
    val HOST_LOGISTIC = "logistic"
    @JvmField
    val INTERNAL_LOGISTIC = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_LOGISTIC"

    /**
     * @see logistic/tracking/...ConfirmShippingActivity.java
     * @param mode to provide mode for shipping confirmation (confirm) or change courier (change)
     * @param EXTRA_ORDER_DETAIL_DATA transaction-common/..OrderDetailData model
     * (mandatory intent param)
     */
    @JvmField
    val SHIPPING_CONFIRMATION = "$INTERNAL_LOGISTIC/shipping-confirmation/{mode}"

    /**
     * @see logistic/tracking/...UploadAwbLogisticActivity.java
     * @param EXTRA_URL_UPLOAD upload-awb url (mandatory intent param)
     * */
    @JvmField
    val UPLOAD_AWB = "$INTERNAL_LOGISTIC/upload-awb"

}