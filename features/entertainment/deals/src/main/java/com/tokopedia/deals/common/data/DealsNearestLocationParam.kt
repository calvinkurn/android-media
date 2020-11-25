package com.tokopedia.deals.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/06/20
 */

data class DealsNearestLocationParam (
        @SerializedName("key")
        @Expose
        val key: String = "",

        @SerializedName("value")
        @Expose
        val value: String = ""
) {
    companion object  {

        const val PARAM_LOCATION_TYPE = "location_type"
        const val PARAM_COORDINATES = "coordinates"
        const val PARAM_SIZE = "size"
        const val PARAM_PAGE_NUM = "page_no"
        const val PARAM_CATEGORY_ID = "category_id"
        const val PARAM_SORT_BY = "sort_by"
        const val PARAM_FIXED = "fixed"
        const val PARAM_DISTANCE = "distance"

        const val VALUE_CATEGORY_ID_DEFAULT =  "15"
        const val VALUE_LOCATION_TYPE_CITY = "city"
        const val VALUE_LOCATION_TYPE_LANDMARK = "landmark"
        const val VALUE_SORT_BY_PRIORITY = "priority"
        const val VALUE_DISTANCE_20KM = "20km"
    }
}