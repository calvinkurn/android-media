package com.tokopedia.hotel.common.applink

import com.tokopedia.abstraction.constant.TkpdAppLink

/**
 * @author by furqan on 26/03/19
 */
object ApplinkConstant: TkpdAppLink() {
    const val HOTEL = "tokopedia://hotel"
    const val HOTEL_DETAIL = "tokopedia://hotel/detail/{id}"
}