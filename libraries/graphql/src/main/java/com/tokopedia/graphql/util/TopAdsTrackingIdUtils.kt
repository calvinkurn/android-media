package com.tokopedia.graphql.util

const val RESPONSE_HEADER_KEY = "Tkp-Enc-Sessionid"
const val TOP_ADS_SHARED_PREF_KEY = "TopAdsSharedPreference"
const val TOP_ADS_TRACKING_KEY = "Tkpd-Tracking-ID"
const val STATUS_QUERY = "status"

val registeredGqlForTopAds = listOf(
    "login_token",
    "register",
    "login_token_v2",
    "register_v2",
    "pdpGetLayout"
)
