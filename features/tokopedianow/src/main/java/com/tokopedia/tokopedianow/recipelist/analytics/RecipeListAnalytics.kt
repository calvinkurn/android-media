package com.tokopedia.tokopedianow.recipelist.analytics

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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeHomeFragment
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_BACK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_BACK_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_BACK_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_BOOKMARK_LIST
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_BOOKMARK_RECIPE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_TOASTER_CANCEL_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_FILTER_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_IMPRESS_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RESET_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RETRY_FAILED_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_FAILED_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_HOME
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_SEARCH
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class RecipeListAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {
    /**
     * Recipe Search
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3407
     *
     * Recipe Home
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3411
     */

    object CATEGORY {
        const val EVENT_CATEGORY_RECIPE_SEARCH = "tokonow - recipe search"
        const val EVENT_CATEGORY_RECIPE_HOME = "tokonow - recipe"
    }

    object ACTION {
        const val EVENT_ACTION_CLICK_BACK = "click back button"
        const val EVENT_ACTION_CLICK_TOASTER = "click toaster"
        const val EVENT_ACTION_CLICK_FILTER = "click filter"
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar"
        const val EVENT_ACTION_CLICK_BOOKMARK_LIST = "click bookmark list"
        const val EVENT_ACTION_CLICK_BOOKMARK_RECIPE = "click bookmark recipe"
        const val EVENT_ACTION_IMPRESS_BOOKMARK_TOASTER = "impression toaster bookmark added"
        const val EVENT_ACTION_CLICK_UNBOOKMARK = "click unbookmark recipe"
        const val EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK = "impression toaster unbookmark"
        const val EVENT_ACTION_CLICK_TOASTER_CANCEL_UNBOOKMARK = "click toaster cancel unbookmark"
        const val EVENT_ACTION_CLICK_RECIPE_CARD = "click recipe card"
        const val EVENT_ACTION_IMPRESS_RECIPE_CARD = "impression recipe card"
        const val EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT = "impression no search result"
        const val EVENT_ACTION_CLICK_BACK_NO_SEARCH_RESULT = "click back no search result"
        const val EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT = "click search bar no search result"
        const val EVENT_ACTION_CLICK_RESET_FILTER = "click reset filter"
        const val EVENT_ACTION_CLICK_FILTER_NO_SEARCH_RESULT = "click filter no search result"
        const val EVENT_ACTION_CLICK_IMPRESS_FAILED_LOAD_PAGE = "impression failed load page"
        const val EVENT_ACTION_CLICK_BACK_FAILED_LOAD_PAGE = "click back failed load page"
        const val EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE = "click retry failed load page"
        const val EVENT_ACTION_IMPRESS_FAILED_BOOKMARK_TOASTER = "impression failed bookmark"
        const val EVENT_ACTION_CLICK_RETRY_FAILED_BOOKMARK_TOASTER = "click retry bookmark"
    }

    private fun getCategory(pageName: String): String {
        return if (pageName == TokoNowRecipeHomeFragment.PAGE_NAME) {
            EVENT_CATEGORY_RECIPE_HOME
        } else {
            EVENT_CATEGORY_RECIPE_SEARCH
        }
    }

    fun clickBackButton(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BACK,
                category = getCategory(pageName)
            )
        )
    }

    fun clickSearchBar(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_SEARCH_BAR,
                category = getCategory(pageName)
            )
        )
    }

    fun clickBookmarkList(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BOOKMARK_LIST,
                category = getCategory(pageName)
            )
        )
    }

    fun clickFilter(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_FILTER,
                category = getCategory(pageName)
            )
        )
    }

    fun clickRecipeCard(
        recipeId: String,
        recipeTitle: String,
        warehouseId: String,
        position: Int,
        pageName: String
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
            promotions = arrayListOf(promotion),
            pageName = pageName
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun impressRecipeCard(
        recipeId: String,
        recipeTitle: String,
        position: Int,
        warehouseId: String,
        pageName: String
    ) {
        val promotion = getPromotion(
            recipeId = recipeId,
            recipeTitle = recipeTitle,
            position = position,
            warehouseId = warehouseId
        )

        val dataLayer = getDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESS_RECIPE_CARD,
            label = "$recipeId - $recipeTitle",
            promotions = arrayListOf(promotion),
            pageName = pageName
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun impressFailedLoadPage(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_CLICK_IMPRESS_FAILED_LOAD_PAGE,
                category = getCategory(pageName)
            )
        )
    }

    fun clickBackFailedLoadPage(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BACK_FAILED_LOAD_PAGE,
                category = getCategory(pageName)
            )
        )
    }

    fun clickRetryFailedLoadPage(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE,
                category = getCategory(pageName)
            )
        )
    }

    fun clickBookmarkRecipe(recipeId: String, recipeTitle: String, pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BOOKMARK_RECIPE,
                label = "$recipeId - $recipeTitle",
                category = getCategory(pageName),
            )
        )
    }

    fun impressBookmarkToasterAdded(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_BOOKMARK_TOASTER,
                category = getCategory(pageName)
            )
        )
    }

    fun clickSeeBookmarkToaster(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_TOASTER,
                category = getCategory(pageName)
            )
        )
    }

    fun clickUnBookmarkRecipe(recipeId: String, recipeTitle: String, pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_UNBOOKMARK,
                category = getCategory(pageName),
                label = "$recipeId - $recipeTitle"
            )
        )
    }

    fun impressUnBookmarkToaster(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK,
                category = getCategory(pageName)
            )
        )
    }

    fun impressFailedBookmarkToaster(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_FAILED_BOOKMARK_TOASTER,
                category = getCategory(pageName)
            )
        )
    }

    fun clickRetryFailedBookmarkToaster(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_FAILED_BOOKMARK_TOASTER,
                category = getCategory(pageName)
            )
        )
    }

    fun clickCancelUnBookmarkToaster(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_TOASTER_CANCEL_UNBOOKMARK,
                category = getCategory(pageName)
            )
        )
    }

    fun impressNoSearchResult(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT,
                category = getCategory(pageName)
            )
        )
    }

    fun clickBackNoSearchResult(pageName: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BACK_NO_SEARCH_RESULT,
                category = getCategory(pageName)
            )
        )
    }

    fun clickSearchBarNoSearchResult() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT,
                category = EVENT_CATEGORY_RECIPE_SEARCH
            )
        )
    }

    fun clickResetFilter() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RESET_FILTER,
                category = EVENT_CATEGORY_RECIPE_SEARCH
            )
        )
    }

    fun clickFilterNoSearchResult() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_FILTER_NO_SEARCH_RESULT,
                category = EVENT_CATEGORY_RECIPE_SEARCH
            )
        )
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
        promotions: ArrayList<Bundle>,
        pageName: String
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, getCategory(pageName))
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userSession.userId)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
        }
    }
}