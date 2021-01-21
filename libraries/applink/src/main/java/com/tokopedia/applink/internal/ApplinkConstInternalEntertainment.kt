package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalEntertainment {

    @JvmField
    val HOST_EVENT = "event"
    @JvmField
    val INTERNAL_EVENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_EVENT"
    @JvmField
    val EVENT_HOME = "$INTERNAL_EVENT/home"
    @JvmField
    val EVENT_FAVORITE = "$INTERNAL_EVENT/favorite"
    @JvmField
    val EVENT_CATEGORY = "$INTERNAL_EVENT/category?category_id={category_id}&id_city={id_city}&query_text={query_text}"
    @JvmField
    val EVENT_SEARCH = "$INTERNAL_EVENT/search"
    @JvmField
    val EVENT_LOCATION = "$INTERNAL_EVENT/location"
    @JvmField
    val EVENT_PDP = "$INTERNAL_EVENT/detail"
    @JvmField
    val EVENT_FORM = "$INTERNAL_EVENT/form"
    @JvmField
    val EVENT_PACKAGE = "$INTERNAL_EVENT/choose-package"
    @JvmField
    val EVENT_REDEEM = "$INTERNAL_EVENT/redeem"


}
