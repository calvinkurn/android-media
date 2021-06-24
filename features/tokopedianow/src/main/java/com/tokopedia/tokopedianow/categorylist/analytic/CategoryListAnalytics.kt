package com.tokopedia.tokopedianow.categorylist.analytic

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics.ACTION.ACTION_CLICK_CLOSE_BUTTON
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics.ACTION.ACTION_CLICK_LEVEL_TWO
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics.ACTION.ACTION_COLLAPSE_LEVEL_ONE
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics.ACTION.ACTION_EXPAND_LEVEL_ONE
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics.CATEGORY.EVENT_CATEGORY_VALUE
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics.MISC.SEE_ALL_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

class CategoryListAnalytics{

    object CATEGORY{
        const val EVENT_CATEGORY_VALUE = "tokonow semua category jumper"
    }

    object ACTION{
        const val ACTION_EXPAND_LEVEL_ONE = "expand level 1 category"
        const val ACTION_CLICK_LEVEL_TWO = "click level 2 category"
        const val ACTION_CLICK_CLOSE_BUTTON = "click close button"
        const val ACTION_COLLAPSE_LEVEL_ONE = "collapse level 1 category"
    }

    object MISC{
        const val SEE_ALL_CATEGORY = "Lihat Semua"
    }

    fun onExpandLeveOneCategory(categoryLevelOne: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, ACTION_EXPAND_LEVEL_ONE,
                TrackAppUtils.EVENT_LABEL, categoryLevelOne)

        data.addCategoryJumperGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickLevelTwoCategory(categoryLevelOne: String, categoryLevelTwo: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, ACTION_CLICK_LEVEL_TWO,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryLevelOne, categoryLevelTwo))

        data.addCategoryJumperGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickLihatSemuaCategory(categoryLevelOne: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, ACTION_CLICK_LEVEL_TWO,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryLevelOne, SEE_ALL_CATEGORY))

        data.addCategoryJumperGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickCloseButton() {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, ACTION_CLICK_CLOSE_BUTTON,
                TrackAppUtils.EVENT_LABEL, "")

        data.addCategoryJumperGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onCollapseLevelOneCategory(categoryLevelOne: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, ACTION_COLLAPSE_LEVEL_ONE,
                TrackAppUtils.EVENT_LABEL, categoryLevelOne)

        data.addCategoryJumperGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun MutableMap<String, Any>.addCategoryJumperGeneralClick(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = EVENT_CLICK_TOKONOW
        this[TrackAppUtils.EVENT_CATEGORY] = EVENT_CATEGORY_VALUE
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_PHYSICAL_GOODS
        return this
    }
}