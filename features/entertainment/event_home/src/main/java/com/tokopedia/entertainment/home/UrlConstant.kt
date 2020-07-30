package com.tokopedia.entertainment.home

import com.tokopedia.url.TokopediaUrl

/**
 * Author errysuprayogi on 12,February,2020
 */
object UrlConstant {

    @JvmField
    var BASE_REST_URL = TokopediaUrl.getInstance().BOOKING
    const val PATH_EVENTS_LIKES = "/v1/api/rating"
    const val PATH_USER_LIKES = "/v1/api/rating/user"

}