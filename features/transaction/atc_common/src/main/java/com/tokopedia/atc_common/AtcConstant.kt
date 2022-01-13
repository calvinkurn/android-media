package com.tokopedia.atc_common

object AtcConstant {
    const val ATC_ERROR_GLOBAL = "Maaf, terjadi sedikit kendala. Coba ulangi beberapa saat lagi ya"

    const val MUTATION_UPDATE_CART_COUNTER = "update_cart_counter"
    const val MUTATION_ATC_EXTERNAL = "MUTATION_ATC_EXTERNAL"

    const val ERROR_PARAMETER_NOT_INITIALIZED = "Parameters has not been initialized!"
}

object AtcFromExternalSource {
    const val ATC_FROM_WISHLIST = "wishlist_list"
    const val ATC_FROM_RECENT_VIEW = "last_seen_list"
    const val ATC_FROM_RECOMMENDATION = "recommendation_list"
    const val ATC_FROM_TOPCHAT = "topchat"
    const val ATC_FROM_NOTIFCENTER = "notifcenter"
    const val ATC_FROM_DISCOVERY = "discovery_page"
    const val ATC_FROM_PLAY = "play"
    const val ATC_FROM_PDP = "pdp_atc"
    const val ATC_FROM_OTHERS = "others"
}