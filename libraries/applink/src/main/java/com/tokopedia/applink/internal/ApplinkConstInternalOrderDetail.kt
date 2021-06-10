package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://order_list"
 */

object ApplinkConstInternalOrderDetail {

    const val HOST_ORDER_LIST = "order_list"

    const val INTERNAL_ORDER_LIST = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_ORDER_LIST}"


    const val ORDER_LIST_URL = "$INTERNAL_ORDER_LIST?url={URL}"


}
