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
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RETRY_FAILED_UNBOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_FAILED_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_FAILED_UNBOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment
import com.tokopedia.tokopedianow.recipelist.util.LoadPageStatus
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

class RecipeListAnalytics (
    private val userSession: UserSessionInterface,
    private val pageName: String,
    private val warehouseId: String
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
        const val EVENT_ACTION_IMPRESS_FAILED_UNBOOKMARK_TOASTER = "impression failed unbookmark"
        const val EVENT_ACTION_CLICK_RETRY_FAILED_BOOKMARK_TOASTER = "click retry bookmark"
        const val EVENT_ACTION_CLICK_RETRY_FAILED_UNBOOKMARK_TOASTER = "click retry unbookmark"
    }

    private val category: String
        get() = if (pageName == BaseTokoNowRecipeListFragment.HOME_PAGE_NAME) CATEGORY.EVENT_CATEGORY_RECIPE_HOME else CATEGORY.EVENT_CATEGORY_RECIPE_SEARCH

    var pageStatus: LoadPageStatus = LoadPageStatus.EMPTY

    fun clickBackButton() {
        when (pageStatus) {
            LoadPageStatus.SUCCESS -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_BACK,
                        category = category
                    )
                )
            }
            LoadPageStatus.ERROR -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_BACK_FAILED_LOAD_PAGE,
                        category = category
                    )
                )
            }
            LoadPageStatus.EMPTY -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_BACK_NO_SEARCH_RESULT,
                        category = category
                    )
                )
            }
        }
    }

    fun clickSearchBar() {
        when (pageStatus) {
            LoadPageStatus.SUCCESS -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_SEARCH_BAR,
                        category = category
                    )
                )
            }
            LoadPageStatus.ERROR -> { /* nothing to do */ }
            LoadPageStatus.EMPTY -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT,
                        category = category
                    )
                )
            }
        }
    }

    fun clickBookmarkList() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BOOKMARK_LIST,
                category = category
            )
        )
    }

    fun clickFilter() {
        when(pageStatus) {
            LoadPageStatus.SUCCESS -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_FILTER,
                        category = category
                    )
                )
            }
            LoadPageStatus.ERROR -> { /* nothing to do */ }
            LoadPageStatus.EMPTY -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_FILTER_NO_SEARCH_RESULT,
                        category = category
                    )
                )
            }
        }
    }

    fun clickRecipeCard(
        recipeId: String,
        recipeTitle: String,
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
        position: Int
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
            promotions = arrayListOf(promotion)
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun impressFailedLoadPage() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_CLICK_IMPRESS_FAILED_LOAD_PAGE,
                category = category
            )
        )
    }

    fun clickRetryFailedLoadPage() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE,
                category = category
            )
        )
    }

    fun clickBookmarkRecipe(recipeId: String, recipeTitle: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BOOKMARK_RECIPE,
                label = "$recipeId - $recipeTitle",
                category = category
            )
        )
    }

    fun impressBookmarkToasterAdded() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_BOOKMARK_TOASTER,
                category = category
            )
        )
    }

    fun clickSeeBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_TOASTER,
                category = category
            )
        )
    }

    fun clickUnBookmarkRecipe(recipeId: String, recipeTitle: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_UNBOOKMARK,
                category = category,
                label = "$recipeId - $recipeTitle"
            )
        )
    }

    fun impressUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK,
                category = category
            )
        )
    }

    fun impressFailedBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_FAILED_BOOKMARK_TOASTER,
                category = category
            )
        )
    }

    fun impressFailedUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_FAILED_UNBOOKMARK_TOASTER,
                category = category
            )
        )
    }

    fun clickRetryFailedBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_FAILED_BOOKMARK_TOASTER,
                category = category
            )
        )
    }

    fun clickRetryFailedUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_FAILED_UNBOOKMARK_TOASTER,
                category = category
            )
        )
    }

    fun clickCancelUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_TOASTER_CANCEL_UNBOOKMARK,
                category = category
            )
        )
    }

    fun impressNoSearchResult() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT,
                category = category
            )
        )
    }

    fun clickResetFilter() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RESET_FILTER,
                category = category
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
        promotions: ArrayList<Bundle>
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userSession.userId)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
        }
    }
}