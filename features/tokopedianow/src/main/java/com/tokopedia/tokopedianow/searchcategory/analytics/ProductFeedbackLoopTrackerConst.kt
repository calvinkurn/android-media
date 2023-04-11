package com.tokopedia.tokopedianow.searchcategory.analytics

object ProductFeedbackLoopTrackerConst {
    object Id{
       const val TRACKER_ID = "trackerId"
       const val USER_ID = "userId"
        const val WAREHOUSE_ID = "warehouseId"
    }

    object Category{
        const val TOKONOW_SEARCH_RESULT = "tokonow - search result"
        const val TOKONOW_NO_SEARCH_RESULT = "tokonow - no search result"
    }

    object Action{
        const val VIEW_WIDGET_SRP = "impression feedback loop widget"
        const val CLICK_BACK_BUTTON = "click back button"
        const val CLICK_SAKARANG_CTA = "click sarankan produk button on feedback loop"
        const val VIEW_FEEDBACK_SHEET = "impression feedback loop sheet"
        const val CLOSE_FEEDBACK_SHEET = "click close button on feedback loop"
        const val CLICK_FEEDBACK_SHEET_TEXT_INPUT = "click text box on feedback loop"
        const val CLICK_FEEDBACK_SHEET_CTA = "click kirim on feedback loop"
        const val VIEW_SUCCESS_TOAST = "impression success toaster feedback loop"
        const val CLICK_OK_SUCCESS_TOAST = "click okay button on success toaster feedback loop"
        const val VIEW_ERROR_TOAST = "impression failed toaster feedback loop"
        const val CLICK_OK_ERROR_TOAST = "click okay button on failed toaster feedback loop"
    }

    object Event{
        const val CLICK_GROCERIES = "clickGroceries"
        const val VIEW_GROCERIES = "viewGroceriesIris"
    }

    object TrackerId{
        val VIEW_WIDGET_SRP = getTrackerIdMap("39547","39550")
        val CLICK_BACK_BUTTON = getTrackerIdMap("39548","39552")
        val CLICK_SAKARANG_CTA = getTrackerIdMap("39549","39551")
        val VIEW_FEEDBACK_SHEET = getTrackerIdMap("39554","39940")
        val CLOSE_FEEDBACK_SHEET = getTrackerIdMap("39555","39941")
        val CLICK_FEEDBACK_SHEET_TEXT_INPUT = getTrackerIdMap("39556","39942")
        val CLICK_FEEDBACK_SHEET_CTA = getTrackerIdMap("39557","39943")
        val VIEW_SUCCESS_TOAST = getTrackerIdMap("39558","39944")
        val CLICK_OK_SUCCESS_TOAST = getTrackerIdMap("39559","39945")
        val VIEW_ERROR_TOAST = getTrackerIdMap("39560","39946")
        val CLICK_OK_ERROR_TOAST = getTrackerIdMap("39561","39947")
    }

    private fun getTrackerIdMap(searchResultId:String,noSearchResultId:String) : Map<String,String> = mapOf(
        Category.TOKONOW_SEARCH_RESULT to searchResultId,
        Category.TOKONOW_NO_SEARCH_RESULT to noSearchResultId
    )
}
