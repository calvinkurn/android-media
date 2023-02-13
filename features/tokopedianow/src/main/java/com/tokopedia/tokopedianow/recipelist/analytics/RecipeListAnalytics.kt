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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
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
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RESET_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RETRY_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_RETRY_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_CLICK_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_FAILED_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_FAILED_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_TOASTER_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.ACTION.EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_HOME
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_SEARCH
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_BACK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_BACK_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_BOOKMARK_LIST
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_BOOKMARK_RECIPE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_RESET_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_RETRY_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_RETRY_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_RETRY_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_TOASTER_CANCEL_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_FAILED_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_FAILED_LOAD_PAGE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_FAILED_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_TOASTER_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_BACK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_BACK_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_BOOKMARK_LIST
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_BOOKMARK_RECIPE
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_FILTER_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_RESET_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_RETRY_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_RETRY_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_SEARCH_BAR_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_TOASTER_CANCEL_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_BOOKMARK_TOASTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_FAILED_BOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_FAILED_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_RECIPE_CARD
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_TOASTER_UNBOOKMARK
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment
import com.tokopedia.tokopedianow.recipelist.util.LoadPageStatus
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

/**
 * Recipe Search
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3407
 *
 * Recipe Home
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3411
 */

class RecipeListAnalytics (
    private val userSession: UserSessionInterface,
    private val pageName: String,
    private val warehouseId: String
) {
    object CATEGORY {
        const val EVENT_CATEGORY_RECIPE_SEARCH = "tokonow - recipe search"
        const val EVENT_CATEGORY_RECIPE_HOME = "tokonow - recipe"
    }

    private object ACTION {
        const val EVENT_ACTION_CLICK_BACK = "click back button"
        const val EVENT_ACTION_CLICK_TOASTER = "click toaster"
        const val EVENT_ACTION_CLICK_FILTER = "click filter"
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar"
        const val EVENT_ACTION_CLICK_BOOKMARK_LIST = "click bookmark list"
        const val EVENT_ACTION_CLICK_BOOKMARK_RECIPE = "click bookmark recipe"
        const val EVENT_ACTION_IMPRESS_TOASTER_BOOKMARK = "impression toaster bookmark added"
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
        const val EVENT_ACTION_IMPRESS_FAILED_LOAD_PAGE = "impression failed load page"
        const val EVENT_ACTION_CLICK_BACK_FAILED_LOAD_PAGE = "click back failed load page"
        const val EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE = "click retry failed load page"
        const val EVENT_ACTION_IMPRESS_FAILED_BOOKMARK = "impression failed bookmark"
        const val EVENT_ACTION_IMPRESS_FAILED_UNBOOKMARK = "impression failed unbookmark"
        const val EVENT_ACTION_CLICK_RETRY_BOOKMARK = "click retry bookmark"
        const val EVENT_ACTION_CLICK_RETRY_UNBOOKMARK = "click retry unbookmark"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_SEARCH_CLICK_BACK = "36434"
        const val TRACKER_ID_SEARCH_CLICK_SEARCH_BAR = "36435"
        const val TRACKER_ID_SEARCH_CLICK_BOOKMARK_LIST = "36436"
        const val TRACKER_ID_SEARCH_CLICK_FILTER = "36437"
        const val TRACKER_ID_SEARCH_IMPRESS_RECIPE_CARD = "36438"
        const val TRACKER_ID_SEARCH_CLICK_RECIPE_CARD = "36439"
        const val TRACKER_ID_SEARCH_CLICK_BOOKMARK_RECIPE = "36440"
        const val TRACKER_ID_SEARCH_IMPRESS_BOOKMARK_TOASTER = "36441"
        const val TRACKER_ID_SEARCH_CLICK_TOASTER = "36442"
        const val TRACKER_ID_SEARCH_CLICK_UNBOOKMARK = "36443"
        const val TRACKER_ID_SEARCH_IMPRESS_TOASTER_UNBOOKMARK = "36444"
        const val TRACKER_ID_SEARCH_CLICK_TOASTER_CANCEL_UNBOOKMARK = "36445"
        const val TRACKER_ID_SEARCH_IMPRESS_NO_SEARCH_RESULT = "36523"
        const val TRACKER_ID_SEARCH_CLICK_BACK_NO_SEARCH_RESULT = "36524"
        const val TRACKER_ID_SEARCH_CLICK_SEARCH_BAR_NO_SEARCH_RESULT = "36525"
        const val TRACKER_ID_SEARCH_CLICK_RESET_FILTER = "36526"
        const val TRACKER_ID_SEARCH_CLICK_FILTER_NO_SEARCH_RESULT = "36527"
        const val TRACKER_ID_SEARCH_IMPRESS_FAILED_BOOKMARK = "37277"
        const val TRACKER_ID_SEARCH_CLICK_RETRY_BOOKMARK = "37278"
        const val TRACKER_ID_SEARCH_IMPRESS_FAILED_UNBOOKMARK = "37279"
        const val TRACKER_ID_SEARCH_CLICK_RETRY_UNBOOKMARK = "37280"

        const val TRACKER_ID_HOME_CLICK_BACK = "36503"
        const val TRACKER_ID_HOME_CLICK_SEARCH_BAR = "36504"
        const val TRACKER_ID_HOME_CLICK_BOOKMARK_LIST = "36505"
        const val TRACKER_ID_HOME_CLICK_FILTER = "36506"
        const val TRACKER_ID_HOME_IMPRESS_RECIPE_CARD = "36507"
        const val TRACKER_ID_HOME_CLICK_RECIPE_CARD = "36508"
        const val TRACKER_ID_HOME_CLICK_BOOKMARK_RECIPE = "36509"
        const val TRACKER_ID_HOME_IMPRESS_BOOKMARK_TOASTER = "36510"
        const val TRACKER_ID_HOME_CLICK_TOASTER = "36511"
        const val TRACKER_ID_HOME_CLICK_UNBOOKMARK = "36512"
        const val TRACKER_ID_HOME_IMPRESS_TOASTER_UNBOOKMARK = "36513"
        const val TRACKER_ID_HOME_CLICK_TOASTER_CANCEL_UNBOOKMARK = "36514"
        const val TRACKER_ID_HOME_IMPRESS_NO_SEARCH_RESULT = "36515"
        const val TRACKER_ID_HOME_CLICK_RESET_FILTER = "36517"
        const val TRACKER_ID_HOME_IMPRESS_FAILED_BOOKMARK = "36518"
        const val TRACKER_ID_HOME_CLICK_RETRY_BOOKMARK = "36519"
        const val TRACKER_ID_HOME_IMPRESS_FAILED_LOAD_PAGE = "36520"
        const val TRACKER_ID_HOME_CLICK_BACK_FAILED_LOAD_PAGE = "36521"
        const val TRACKER_ID_HOME_CLICK_RETRY_FAILED_LOAD_PAGE = "36522"
        const val TRACKER_ID_HOME_IMPRESS_FAILED_UNBOOKMARK = "37245"
        const val TRACKER_ID_HOME_CLICK_RETRY_UNBOOKMARK = "37246"
    }

    private val category: String
        get() = getValue(
            homeValue = EVENT_CATEGORY_RECIPE_HOME,
            searchValue = EVENT_CATEGORY_RECIPE_SEARCH
        )

    var pageStatus: LoadPageStatus = LoadPageStatus.EMPTY

    fun clickBackButton() {
        when (pageStatus) {
            LoadPageStatus.SUCCESS -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_BACK,
                        category = category,
                        trackerId = getValue(
                            homeValue = TRACKER_ID_HOME_CLICK_BACK,
                            searchValue = TRACKER_ID_SEARCH_CLICK_BACK
                        )
                    )
                )
            }
            LoadPageStatus.ERROR -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_BACK_FAILED_LOAD_PAGE,
                        category = category,
                        trackerId = getValue(
                            homeValue = TRACKER_ID_HOME_CLICK_BACK_FAILED_LOAD_PAGE,
                            searchValue = ""
                        )
                    )
                )
            }
            LoadPageStatus.EMPTY -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_BACK_NO_SEARCH_RESULT,
                        category = category,
                        trackerId = getValue(
                            homeValue = "",
                            searchValue = TRACKER_ID_SEARCH_CLICK_BACK_NO_SEARCH_RESULT
                        )
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
                        category = category,
                        trackerId = getValue(
                            homeValue = TRACKER_ID_HOME_CLICK_SEARCH_BAR,
                            searchValue = TRACKER_ID_SEARCH_CLICK_SEARCH_BAR
                        )
                    )
                )
            }
            LoadPageStatus.ERROR -> { /* nothing to do */ }
            LoadPageStatus.EMPTY -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_SEARCH_BAR_NO_SEARCH_RESULT,
                        category = category,
                        trackerId = getValue(
                            homeValue = "",
                            searchValue = TRACKER_ID_SEARCH_CLICK_SEARCH_BAR_NO_SEARCH_RESULT
                        )
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
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_BOOKMARK_LIST,
                    searchValue = TRACKER_ID_SEARCH_CLICK_BOOKMARK_LIST
                )
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
                        category = category,
                        trackerId = getValue(
                            homeValue = TRACKER_ID_HOME_CLICK_FILTER,
                            searchValue = TRACKER_ID_SEARCH_CLICK_FILTER
                        )
                    )
                )
            }
            LoadPageStatus.ERROR -> { /* nothing to do */ }
            LoadPageStatus.EMPTY -> {
                TokoNowCommonAnalytics.hitCommonTracker(
                    TokoNowCommonAnalytics.getDataLayer(
                        event = EVENT_CLICK_PG,
                        action = EVENT_ACTION_CLICK_FILTER_NO_SEARCH_RESULT,
                        category = category,
                        trackerId = getValue(
                            homeValue = "",
                            searchValue = TRACKER_ID_SEARCH_CLICK_FILTER_NO_SEARCH_RESULT
                        )
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
            promotions = arrayListOf(promotion),
            trackerId = getValue(
                homeValue = TRACKER_ID_HOME_CLICK_RECIPE_CARD,
                searchValue = TRACKER_ID_SEARCH_CLICK_RECIPE_CARD
            )
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
            promotions = arrayListOf(promotion),
            trackerId = getValue(
                homeValue = TRACKER_ID_HOME_IMPRESS_RECIPE_CARD,
                searchValue = TRACKER_ID_SEARCH_IMPRESS_RECIPE_CARD
            )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun impressFailedLoadPage() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_FAILED_LOAD_PAGE,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_FAILED_LOAD_PAGE,
                    searchValue = ""
                )
            )
        )
    }

    fun clickRetryFailedLoadPage() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_FAILED_LOAD_PAGE,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_RETRY_FAILED_LOAD_PAGE,
                    searchValue = ""
                )
            )
        )
    }

    fun clickBookmarkRecipe(recipeId: String, recipeTitle: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_BOOKMARK_RECIPE,
                label = "$recipeId - $recipeTitle",
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_BOOKMARK_RECIPE,
                    searchValue = TRACKER_ID_SEARCH_CLICK_BOOKMARK_RECIPE
                )
            )
        )
    }

    fun impressBookmarkToasterAdded() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_TOASTER_BOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_BOOKMARK_TOASTER,
                    searchValue = TRACKER_ID_SEARCH_IMPRESS_BOOKMARK_TOASTER
                )
            )
        )
    }

    fun clickSeeBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_TOASTER,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_TOASTER,
                    searchValue = TRACKER_ID_SEARCH_CLICK_TOASTER
                )
            )
        )
    }

    fun clickUnBookmarkRecipe(recipeId: String, recipeTitle: String) {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_UNBOOKMARK,
                category = category,
                label = "$recipeId - $recipeTitle",
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_UNBOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_CLICK_UNBOOKMARK
                )
            )
        )
    }

    fun impressUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_TOASTER_UNBOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_TOASTER_UNBOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_IMPRESS_TOASTER_UNBOOKMARK
                )
            )
        )
    }

    fun impressFailedBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_FAILED_BOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_FAILED_BOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_IMPRESS_FAILED_BOOKMARK
                )
            )
        )
    }

    fun impressFailedUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_FAILED_UNBOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_FAILED_UNBOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_IMPRESS_FAILED_UNBOOKMARK
                )
            )
        )
    }

    fun clickRetryFailedBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_BOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_RETRY_BOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_CLICK_RETRY_BOOKMARK
                )
            )
        )
    }

    fun clickRetryFailedUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RETRY_UNBOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_RETRY_UNBOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_CLICK_RETRY_UNBOOKMARK
                )
            )
        )
    }

    fun clickCancelUnBookmarkToaster() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_TOASTER_CANCEL_UNBOOKMARK,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_TOASTER_CANCEL_UNBOOKMARK,
                    searchValue = TRACKER_ID_SEARCH_CLICK_TOASTER_CANCEL_UNBOOKMARK
                )
            )
        )
    }

    fun impressNoSearchResult() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_NO_SEARCH_RESULT,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_NO_SEARCH_RESULT,
                    searchValue = TRACKER_ID_SEARCH_IMPRESS_NO_SEARCH_RESULT
                )
            )
        )
    }

    fun clickResetFilter() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_RESET_FILTER,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_RESET_FILTER,
                    searchValue = TRACKER_ID_SEARCH_CLICK_RESET_FILTER
                )
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
        trackerId: String
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_TRACKER_ID, trackerId)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userSession.userId)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
        }
    }

    private fun getValue(
        homeValue: String,
        searchValue: String
    ) = if (pageName == BaseTokoNowRecipeListFragment.HOME_PAGE_NAME) homeValue else searchValue
}
