package com.tokopedia.inbox.universalinbox.util

import com.tokopedia.topads.sdk.utils.PARAM_DEVICE
import com.tokopedia.topads.sdk.utils.PARAM_EP
import com.tokopedia.topads.sdk.utils.PARAM_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.PARAM_ITEM
import com.tokopedia.topads.sdk.utils.PARAM_PAGE
import com.tokopedia.topads.sdk.utils.PARAM_SRC
import com.tokopedia.topads.sdk.utils.PARAM_TEMPLATE_ID
import com.tokopedia.topads.sdk.utils.PARAM_USER_ID
import com.tokopedia.topads.sdk.utils.UrlParamHelper
import com.tokopedia.topads.sdk.utils.VALUE_DEVICE
import com.tokopedia.topads.sdk.utils.VALUE_EP
import com.tokopedia.topads.sdk.utils.VALUE_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.VALUE_ITEM
import com.tokopedia.topads.sdk.utils.VALUE_TEMPLATE_ID
import com.tokopedia.user.session.UserSessionInterface

object UniversalInboxValueUtil {

    const val PAGE_NAME = "inbox"

    /**
     * Rollence
     */
    const val VAR_B = "var_b" // for analytics only
    const val INBOX_ADS_REFRESH_KEY = "inbox_ads_refresh"
    const val INBOX_SCROLL_VALUE = 4

    /**
     * User Session
     */
    private const val ROLE_BUYER = "buyer"
    private const val ROLE_BOTH = "both"
    const val VALUE_X = "x"
    fun getRoleUser(userSession: UserSessionInterface): String {
        return if (userSession.hasShop()) {
            ROLE_BOTH
        } else {
            ROLE_BUYER
        }
    }
    fun getShopIdTracker(userSession: UserSessionInterface): String {
        return if (userSession.hasShop()) {
            userSession.shopId
        } else {
            VALUE_X
        }
    }

    /**
     * Widget
     */
    const val CHATBOT_TYPE = 1
    const val GOJEK_TYPE = 101

    const val GOJEK_REPLACE_TEXT = "{order_counter}"

    /**
     * Static Menu
     */
    // Review
    const val PAGE_SOURCE_KEY: String = "pageSource"
    const val PAGE_SOURCE_REVIEW_INBOX = "review inbox"

    /**
     * Recommendation
     */
    // PDP
    const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"

    // TopAds
    const val COMPONENT_NAME_TOP_ADS = "Inbox Recommendation Top Ads"

    // TopAds - Banner
    const val TOP_ADS_BANNER_COUNT = 2
    const val TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED = 22 // Probably random number, value same as old inbox

    // TopAds - Headline
    const val HEADLINE_ADS_BANNER_COUNT = 2
    const val HEADLINE_POS_NOT_TO_BE_ADDED = 11 // Probably random number, value same as old inbox
    fun getHeadlineAdsParam(topAdsHeadLinePage: Int, userId: String): String {
        return UrlParamHelper.generateUrlParamString(
            mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to topAdsHeadLinePage,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to PAGE_NAME,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userId
            )
        )
    }

    // Wishlist
    const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"
    const val WISHLIST_STATUS_IS_WISHLIST = "isWishlist"

    // Widget
    const val WIDGET_PAGE_NAME_PRE_PURCHASE = "inbox_pre-purchase"
    const val WIDGET_PAGE_NAME_POST_PURCHASE = "inbox_post-purchase"
}
