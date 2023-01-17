package com.tokopedia.tokopedianow.recipebookmark.analytics

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getDataLayer
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.hitCommonTracker
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.ACTION.EVENT_ACTION_CLICK_BACK
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.ACTION.EVENT_ACTION_CLICK_CANCEL_UNBOOKMARK
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.ACTION.EVENT_ACTION_CLICK_RECIPE_CARD
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.ACTION.EVENT_ACTION_CLICK_UNBOOKMARK
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.ACTION.EVENT_ACTION_IMPRESS_UNBOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.ACTION.EVENT_IMPRESS_RECIPE_CARD
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_BOOKMARK
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class RecipeBookmarkAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {
    /**
     * Recipe Bookmark Tracker
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3391
     */

    object CATEGORY {
        const val EVENT_CATEGORY_RECIPE_BOOKMARK = "tokonow - recipe bookmark page"
    }

    object ACTION {
        const val EVENT_ACTION_CLICK_BACK = "click back"
        const val EVENT_ACTION_CLICK_UNBOOKMARK = "click unbookmark"
        const val EVENT_ACTION_IMPRESS_UNBOOKMARK_TOASTER = "impression unbookmark toaster"
        const val EVENT_ACTION_CLICK_CANCEL_UNBOOKMARK = "click cancel unbookmark"
        const val EVENT_ACTION_CLICK_RECIPE_CARD = "click recipe card"
        const val EVENT_IMPRESS_RECIPE_CARD = "impression recipe card"

    }

    fun clickBackButton() {
        hitCommonTracker(
            getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BACK,
                category = EVENT_CATEGORY_RECIPE_BOOKMARK
            )
        )
    }

    fun clickUnBookmark(
        recipeId: String,
        recipeTitle: String
    ) {
        hitCommonTracker(
            getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_UNBOOKMARK,
                category = EVENT_CATEGORY_RECIPE_BOOKMARK,
                label = "$recipeId - $recipeTitle"
            )
        )
    }

    fun impressUnBookmarkToaster() {
        hitCommonTracker(
            getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_UNBOOKMARK_TOASTER,
                category = EVENT_CATEGORY_RECIPE_BOOKMARK
            )
        )
    }

    fun clickCancelUnBookmarkToaster() {
        hitCommonTracker(
            getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_CANCEL_UNBOOKMARK,
                category = EVENT_CATEGORY_RECIPE_BOOKMARK
            )
        )
    }

    fun clickRecipeCard(
        recipeId: String,
        recipeTitle: String,
        warehouseId: String,
        position: Int
    ) {
        val promotion = getPromotion(
            recipeId = recipeId,
            recipeTitle = recipeTitle,
            position = position,
            warehouseId = warehouseId
        )

        val dataLayer = getDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_RECIPE_CARD,
            label = "$recipeId - $recipeTitle",
            promotions = arrayListOf(promotion)
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun impressRecipeCard(
        recipeId: String,
        recipeTitle: String,
        position: Int,
        warehouseId: String
    ) {
        val promotion = getPromotion(
            recipeId = recipeId,
            recipeTitle = recipeTitle,
            position = position,
            warehouseId = warehouseId
        )

        val dataLayer = getDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_IMPRESS_RECIPE_CARD,
            label = "$recipeId - $recipeTitle",
            promotions = arrayListOf(promotion)
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    private fun getPromotion(
        recipeId: String,
        recipeTitle: String,
        position: Int,
        warehouseId: String
    ): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, "${recipeTitle}_${warehouseId}_${userSession.userId}")
            putInt(KEY_CREATIVE_SLOT, position + 1)
            putString(KEY_ITEM_ID, recipeId)
            putString(KEY_ITEM_NAME, recipeTitle)
        }
    }

    private fun getDataLayer(
        event: String,
        action: String,
        label: String = "",
        promotions: ArrayList<Bundle>
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_RECIPE_BOOKMARK)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userSession.userId)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
        }
    }
}