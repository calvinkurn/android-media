package com.tokopedia.tokopedianow.recipelist.analytics

import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeFilterAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeFilterAnalytics.ACTION.EVENT_ACTION_IMPRESS_APPLY_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeFilterAnalytics.TRACKER_ID.TRACKER_ID_HOME_CLICK_APPLY_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeFilterAnalytics.TRACKER_ID.TRACKER_ID_HOME_IMPRESS_APPLY_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeFilterAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_CLICK_APPLY_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeFilterAnalytics.TRACKER_ID.TRACKER_ID_SEARCH_IMPRESS_APPLY_FILTER
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_HOME
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics.CATEGORY.EVENT_CATEGORY_RECIPE_SEARCH
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_DURATION
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_PORTION
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_SORT_BY
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TAG_ID
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

/**
 * Recipe Search
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3407
 *
 * Recipe Home
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3411
 */

class RecipeFilterAnalytics (
    private val pageName: String
) {
    private object ACTION {
        const val EVENT_ACTION_CLICK_APPLY_FILTER = "click apply filter"
        const val EVENT_ACTION_IMPRESS_APPLY_FILTER = "impression apply filter"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_SEARCH_IMPRESS_APPLY_FILTER = "36999"
        const val TRACKER_ID_SEARCH_CLICK_APPLY_FILTER = "37273"

        const val TRACKER_ID_HOME_IMPRESS_APPLY_FILTER = "37247"
        const val TRACKER_ID_HOME_CLICK_APPLY_FILTER = "37248"
    }

    private val category: String
        get() = getValue(
            homeValue = EVENT_CATEGORY_RECIPE_HOME,
            searchValue = EVENT_CATEGORY_RECIPE_SEARCH
        )

    fun impressApplyFilter() {
        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESS_APPLY_FILTER,
                category = category,
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_IMPRESS_APPLY_FILTER,
                    searchValue = TRACKER_ID_SEARCH_IMPRESS_APPLY_FILTER
                )
            )
        )
    }

    fun clickApplyFilter(filters: List<SelectedFilter>) {
        val map = mutableMapOf<String, String>()
        map[PARAM_SORT_BY] = "null"
        map[PARAM_INGREDIENT_ID] = "null"
        map[PARAM_DURATION] = "null"
        map[PARAM_PORTION] = "null"
        map[PARAM_TAG_ID] = "null"

        filters.forEach {
            map[it.parentId] = it.text
        }

        TokoNowCommonAnalytics.hitCommonTracker(
            TokoNowCommonAnalytics.getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_APPLY_FILTER,
                category = category,
                label = map.values.joinToString(" - "),
                trackerId = getValue(
                    homeValue = TRACKER_ID_HOME_CLICK_APPLY_FILTER,
                    searchValue = TRACKER_ID_SEARCH_CLICK_APPLY_FILTER
                )
            )
        )
    }

    private fun getValue(
        homeValue: String,
        searchValue: String
    ) = if (pageName == BaseTokoNowRecipeListFragment.HOME_PAGE_NAME) homeValue else searchValue
}
