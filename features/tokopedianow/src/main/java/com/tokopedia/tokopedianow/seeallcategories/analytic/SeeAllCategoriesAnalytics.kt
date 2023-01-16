package com.tokopedia.tokopedianow.seeallcategories.analytic

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_56
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.seeallcategories.analytic.SeeAllCategoriesAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_CARD
import com.tokopedia.tokopedianow.seeallcategories.analytic.SeeAllCategoriesAnalytics.ACTION.EVENT_ACTION_IMPRESSION_CATEGORY_CARD
import com.tokopedia.tokopedianow.seeallcategories.analytic.SeeAllCategoriesAnalytics.CATEGORY.EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE
import com.tokopedia.tokopedianow.seeallcategories.analytic.SeeAllCategoriesAnalytics.TRACKER_ID.TRACKER_ID_CLICK_CATEGORY_CARD
import com.tokopedia.tokopedianow.seeallcategories.analytic.SeeAllCategoriesAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_CATEGORY_CARD
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SeeAllCategoriesAnalytics@Inject constructor(private val userSession: UserSessionInterface) {

    /**
     * NOW! SeeAllCategories Page Tracker
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3695
     */

    object ACTION {
        const val EVENT_ACTION_IMPRESSION_CATEGORY_CARD = "impression category card"
        const val EVENT_ACTION_CLICK_CATEGORY_CARD = "click category card"
    }

    object CATEGORY {
        const val EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE = "tokonow category menu page"
    }

    object TRACKER_ID {
        const val TRACKER_ID_IMPRESSION_CATEGORY_CARD = "40746"
        const val TRACKER_ID_CLICK_CATEGORY_CARD = "40747"
    }

    private fun ecommerceDataLayerCategoryClicked(position: Int, categoryName: String, categoryId: String, warehouseId: String): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, categoryName)
            putString(KEY_CREATIVE_SLOT, position.toString())
            putString(KEY_DIMENSION_56, warehouseId)
            putString(KEY_ITEM_ID, categoryId)
            putString(KEY_ITEM_NAME, getItemName(position, categoryName))
        }
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        trackerId: String,
        promotions: ArrayList<Bundle>,
        warehouseId: String
    ): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, category)
            putString(EVENT_LABEL, label)
            putString(KEY_TRACKER_ID, trackerId)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
            putString(KEY_WAREHOUSE_ID, warehouseId)
        }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun getItemName(position: Int, categoryName: String): String {
        return "/ - p$position - category menu - card - $categoryName"
    }

    fun onCategoryImpressed(categoryId: String, categoryName: String, warehouseId: String, position: Int) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_CATEGORY_CARD,
            category = EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE,
            label = categoryId,
            trackerId = TRACKER_ID_IMPRESSION_CATEGORY_CARD,
            promotions = arrayListOf(
                ecommerceDataLayerCategoryClicked(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = position.getTrackerPosition()
                )
            ),
            warehouseId = warehouseId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun onCategoryClicked(categoryId: String, categoryName: String, warehouseId: String, position: Int) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CATEGORY_CARD,
            category = EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE,
            label = categoryId,
            trackerId = TRACKER_ID_CLICK_CATEGORY_CARD,
            promotions = arrayListOf(
                ecommerceDataLayerCategoryClicked(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = position.getTrackerPosition()
                )
            ),
            warehouseId = warehouseId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

}
