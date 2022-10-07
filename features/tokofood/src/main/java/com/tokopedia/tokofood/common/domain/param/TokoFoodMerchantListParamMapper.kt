package com.tokopedia.tokofood.common.domain.param

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.util.TokofoodExt
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodParamFilterMerchant
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodParamSort

object TokoFoodMerchantListParamMapper {

    private const val FILTER_KEY = "filter"
    private const val SORT_BY_KEY = "sortBy"
    const val LAT_LONG_KEY = "latlong"
    const val TIMEZONE_KEY = "timezone"
    const val USER_CITY_ID_KEY = "user_cityId"
    const val PAGE_KEY = "pageKey"
    const val LIMIT_KEY = "limit"

    const val LIMIT = 10

    val TIMEZONE = TokofoodExt.getLocalTimeZone()

    @JvmStatic
    fun createRequestParams(localCacheModel: LocalCacheModel?, option: Int = 0, brandUId: String = "",
                            sortBy: Int = 0, orderById: Int = 0, cuisine: String = "", pageKey: String = "") = HashMap<String, Any>().apply {
        put(LAT_LONG_KEY, mapLocation(localCacheModel))
        put(PAGE_KEY, pageKey)
        put(TIMEZONE_KEY, TIMEZONE)
        put(LIMIT_KEY, LIMIT)
        put(FILTER_KEY, mapFilter(option, brandUId, cuisine))
        put(SORT_BY_KEY, mapSort(sortBy, orderById))
    }

    @JvmStatic
    fun mapLocation(localCacheModel: LocalCacheModel?): String {
        val stringBuilder = StringBuilder()
        if (!localCacheModel?.lat.isNullOrEmpty() && !localCacheModel?.long.isNullOrEmpty()){
            stringBuilder.append(localCacheModel?.lat)
            stringBuilder.append(",")
            stringBuilder.append(localCacheModel?.long)
        }
        return stringBuilder.toString()
    }

    private fun mapFilter(option: Int, brandUId: String, cuisine: String): TokoFoodParamFilterMerchant {
        return TokoFoodParamFilterMerchant().apply {
            this.option = option
            this.brandUId = brandUId
            if (cuisine.isNotEmpty()) {
                this.cuisines = listOf(cuisine)
            }
        }
    }

    private fun mapSort(sortBy: Int, orderById: Int): TokoFoodParamSort {
        return TokoFoodParamSort().apply {
            this.sortBy = sortBy
            this.orderBy = orderById
        }
    }

}