package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.DEFAULT_NULL_VALUE
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.PAGE_NAME_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_FILES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHARING_CHANNEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_SHARING_CHANNEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_COMMUNICATION
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_TOKONOW_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PAGE_SOURCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_SHARING_EXPERIENCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString

/**
 * Sharing Experience Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/1963
 **/

class CategorySharingExperienceAnalytic(
    private val userId: String
) {
    // - 1
    fun trackClickShareButtonTopNav(
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String,
        currentCategoryId: String
    ) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"
        val pageSource = "${PAGE_NAME_TOKOPEDIA_NOW}.${DEFAULT_NULL_VALUE}.${DEFAULT_NULL_VALUE}.$currentCategoryId"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON,
            EVENT_CATEGORY_TOP_NAV_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_PAGE_SOURCE] = pageSource
        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 2
    fun trackClickCloseShareBottomSheet(
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 3
    fun trackClickChannelShareBottomSheet(
        channel: String,
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "$channel - ${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_SHARING_CHANNEL,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 4
    fun trackImpressChannelShareBottomSheet(
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_VIEW_TOKONOW_IRIS,
            EVENT_ACTION_IMPRESSION_SHARING_CHANNEL,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 5
    fun trackImpressChannelShareBottomSheetScreenShot(
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_VIEW_TOKONOW_IRIS,
            EVENT_ACTION_IMPRESSION_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 6
    fun trackClickCloseScreenShotShareBottomSheet(
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 7
    fun trackClickChannelShareBottomSheetScreenshot(
        channel: String,
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "$channel - ${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }

    // - 8
    fun trackClickAccessMediaAndFiles(
        accessText: String,
        categoryIdLvl1: String,
        categoryIdLvl2: String,
        categoryIdLvl3: String
    ) {
        val label = "$accessText - ${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_FILES,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        TokoNowCommonAnalytics.getTracker().sendGeneralEvent(dataLayer)
    }
}
