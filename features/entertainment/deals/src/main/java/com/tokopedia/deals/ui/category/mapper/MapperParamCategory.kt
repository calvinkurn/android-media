package com.tokopedia.deals.ui.category.mapper

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.ui.search.DealsSearchConstants.BRAND_PRODUCT_TREE
import com.tokopedia.deals.ui.search.DealsSearchConstants.SEARCH_PARAM

object MapperParamCategory {
    fun generateCategoryBrandPopularParams(
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?,
            page: Int
    ): Map<String, Any>{
        val brandPopularParams: ArrayList<RequestParam> = arrayListOf()
        brandPopularParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_CATEGORY, com.tokopedia.deals.ui.search.DealsSearchConstants.DEFAULT_CATEGORY))
        brandPopularParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_TREE, BRAND_PRODUCT_TREE))
        if (childCategoryIds != null) {
            brandPopularParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_CHILD_CATEGORY_IDS, childCategoryIds))
        }
        brandPopularParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_COORDINATES, locationCoordinates))
        brandPopularParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_LOCATION_TYPE, locationType))
        brandPopularParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_PAGE, page.toString()))

        return mapOf(SEARCH_PARAM to brandPopularParams)
    }

}
