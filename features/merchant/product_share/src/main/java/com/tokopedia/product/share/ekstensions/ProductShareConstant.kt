package com.tokopedia.product.share.ekstensions

import com.tokopedia.abstraction.common.utils.network.AuthUtil

/**
 * Created by Yehezkiel on 05/08/21
 */
object ProductShareConstant {
    const val EVENT_CLICK_PDP_SHARING = "clickCommunication"
    const val EVENT_VIEW_IRIS_PDP_SHARING = "viewCommunicationIris"
    const val EVENT_CATEGORY_PDP_SHARING = "product detail page"


    const val EVENT_ACTION_SHARE_BOTTOMSHEET = "click - close share bottom sheet"
    const val EVENT_ACTION_SCREENSHOT_SHARE_BOTTOMSHEET = "click - close screenshot share bottom sheet"

    const val EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOMSHEET = "click - sharing channel"
    const val EVENT_ACTION_CLICK_CHANNEL_SCREENSHOT_SHARE_BOTTOMSHEET = "click - channel share bottom sheet - screenshot"
    const val EVENT_ACTION_VIEW_SHARE_BOTTOMSHEET = "view on sharing channel"
    const val EVENT_ACTION_VIEW_SCREENSHOT_SHARE_BOTTOMSHEET = "view - screenshot share bottom sheet"

    const val EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_AND_FILES = "click - access photo media and files"

    const val VALUE_BUSINESS_UNIT_SHARING = "sharingexperience"
    const val VALUE_CURRENT_SITE = "tokopediamarketplace"

    const val KEY_BUSINESS_UNIT_SHARING = "businessUnit"
    const val KEY_CURRENT_SITE_SHARING = "currentSite"
    const val KEY_PRODUCT_ID_SHARING = "productId"
    const val KEY_USER_ID_SHARING = "userId"

    const val TRACKER_ID = "trackerId"
    const val TRACKER_ID_CLICK_SHARING_CHANNEL = "10367"
}