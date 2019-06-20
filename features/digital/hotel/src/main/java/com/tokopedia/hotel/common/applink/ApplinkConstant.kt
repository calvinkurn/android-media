package com.tokopedia.hotel.common.applink

import com.tokopedia.abstraction.constant.TkpdAppLink

/**
 * @author by furqan on 26/03/19
 */
object ApplinkConstant: TkpdAppLink() {
    const val HOTEL = "tokopedia://hotel"
    const val HOTEL_DASHBOARD = "tokopedia://hotel/dashboard"
    const val HOTEL_DETAIL = "tokopedia://hotel/detail/{id}"
    const val HOTEL_ORDER_LIST = "tokopedia://hotel/order/"
    const val HOTEL_ORDER_DETAIL = "tokopedia://hotel/order/{order_id}"
}