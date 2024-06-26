package com.tokopedia.tokopedianow.seeallcategory.analytic

import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenu
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenuPromotion
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_CARD
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics.ACTION.EVENT_ACTION_IMPRESSION_CATEGORY_CARD
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics.CATEGORY.EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics.TRACKER_ID.TRACKER_ID_CLICK_CATEGORY_CARD
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_CATEGORY_CARD
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SeeAllCategoryAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    /**
     * NOW! SeeAllCategory Page Tracker
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

    fun onCategoryImpressed(categoryId: String, categoryName: String, warehouseId: String, position: Int) {
        val newPosition = position.getTrackerPosition()
        val dataLayer = getEcommerceDataLayerCategoryMenu(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_CATEGORY_CARD,
            category = EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE,
            label = categoryId,
            trackerId = TRACKER_ID_IMPRESSION_CATEGORY_CARD,
            promotions = arrayListOf(
                getEcommerceDataLayerCategoryMenuPromotion(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = newPosition,
                    itemName = getCategoryMenuItemName(
                        position = newPosition,
                        categoryName = categoryName
                    )
                )
            ),
            warehouseId = warehouseId,
            userId = userSession.userId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun onCategoryClicked(categoryId: String, categoryName: String, warehouseId: String, position: Int) {
        val newPosition = position.getTrackerPosition()
        val dataLayer = getEcommerceDataLayerCategoryMenu(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CATEGORY_CARD,
            category = EVENT_CATEGORY_TOKONOW_CATEGORY_MENU_PAGE,
            label = categoryId,
            trackerId = TRACKER_ID_CLICK_CATEGORY_CARD,
            promotions = arrayListOf(
                getEcommerceDataLayerCategoryMenuPromotion(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = newPosition,
                    itemName = getCategoryMenuItemName(
                        position = newPosition,
                        categoryName = categoryName
                    )
                )
            ),
            warehouseId = warehouseId,
            userId = userSession.userId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    private fun getCategoryMenuItemName(position: Int, categoryName: String): String {
        return "/ - p$position - category menu - card - $categoryName"
    }

}
