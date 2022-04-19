package com.tokopedia.deals.category.utils

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.deals.search.DealsSearchConstants.BRAND_PRODUCT_TREE
import com.tokopedia.deals.search.DealsSearchConstants.SEARCH_PARAM

object MapperParamCategory {
    fun generateCategoryBrandPopularParams(
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?,
            page: Int
    ): Map<String, Any>{
        val brandPopularParams: ArrayList<RequestParam> = arrayListOf()
        brandPopularParams.add(RequestParam(DealsSearchConstants.MAP_CATEGORY, DealsSearchConstants.DEFAULT_CATEGORY))
        brandPopularParams.add(RequestParam(DealsSearchConstants.MAP_TREE, BRAND_PRODUCT_TREE))
        if (childCategoryIds != null) {
            brandPopularParams.add(RequestParam(DealsSearchConstants.MAP_CHILD_CATEGORY_IDS, childCategoryIds))
        }
        brandPopularParams.add(RequestParam(DealsSearchConstants.MAP_COORDINATES, locationCoordinates))
        brandPopularParams.add(RequestParam(DealsSearchConstants.MAP_LOCATION_TYPE, locationType))
        brandPopularParams.add(RequestParam(DealsSearchConstants.MAP_PAGE, page.toString()))

        return mapOf(SEARCH_PARAM to brandPopularParams)
    }

}