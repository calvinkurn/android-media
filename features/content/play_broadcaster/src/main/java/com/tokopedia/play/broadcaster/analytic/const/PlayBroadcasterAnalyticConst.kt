package com.tokopedia.play.broadcaster.analytic.const

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
internal object Label {
    const val TRACKER_ID_LABEL = "trackerId"
    const val SESSION_IRIS_LABEL = "sessionIris"
    const val SCREEN_NAME_LABEL = "screenName"
    const val IS_LOGGED_IN_STATUS_LABEL = "isLoggedInStatus"
    const val BUSINESS_UNIT_LABEL = "businessUnit"
    const val CURRENT_SITE_LABEL = "currentSite"
}

internal object Value {
    const val BROADCASTER_OPEN_SCREEN = "openScreen"
    const val BROADCASTER_VIEW_CONTENT = "viewContentIris"
    const val BROADCASTER_CLICK_CONTENT = "clickContent"
    const val BROADCASTER_EVENT_CATEGORY = "play broadcast"
    const val BROADCASTER_BUSINESS_UNIT = "play"
    const val BROADCASTER_TYPE_USER = "user"
    const val BROADCASTER_TYPE_SELLER = "seller"
    const val BROADCASTER_CURRENT_SITE_SELLER = "tokopediaseller"
    const val BROADCASTER_CURRENT_SITE_MAIN = "tokopediamarketplace"
}
