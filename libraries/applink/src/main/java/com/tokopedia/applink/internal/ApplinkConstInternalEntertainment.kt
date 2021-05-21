package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalEntertainment {

    const val HOST_EVENT = "event"
    const val INTERNAL_EVENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_EVENT"
    const val EVENT_HOME = "$INTERNAL_EVENT/home"
    const val EVENT_FAVORITE = "$INTERNAL_EVENT/favorite"
    const val EVENT_CATEGORY = "$INTERNAL_EVENT/category?category_id={category_id}&id_city={id_city}&query_text={query_text}"
    const val EVENT_SEARCH = "$INTERNAL_EVENT/search"
    const val EVENT_LOCATION = "$INTERNAL_EVENT/location"
    const val EVENT_PDP = "$INTERNAL_EVENT/detail"
    const val EVENT_FORM = "$INTERNAL_EVENT/form"
    const val EVENT_PACKAGE = "$INTERNAL_EVENT/choose-package"
    const val EVENT_REDEEM = "$INTERNAL_EVENT/redeem"


}
