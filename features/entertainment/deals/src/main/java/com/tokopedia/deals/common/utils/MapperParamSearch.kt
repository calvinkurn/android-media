package com.tokopedia.deals.common.utils

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.deals.search.DealsSearchConstants.SEARCH_PARAM

object MapperParamSearch {

    fun generateParams(
            searchQuery: String,
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?,
            page: String,
            tree: String): Map<String, Any> {
        return mapOf(SEARCH_PARAM to
                generateSearchParams(searchQuery, locationCoordinates, locationType, childCategoryIds, page, tree))
    }

    private fun generateSearchParams(
            searchQuery: String,
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?,
            page: String,
            tree: String): ArrayList<RequestParam> {
        val searchParams: ArrayList<RequestParam> = arrayListOf()
        searchParams.add(RequestParam(DealsSearchConstants.MAP_CATEGORY, DealsSearchConstants.DEFAULT_CATEGORY))
        searchParams.add(RequestParam(DealsSearchConstants.MAP_TREE, tree))
        if (childCategoryIds != null) {
            searchParams.add(RequestParam(DealsSearchConstants.MAP_CHILD_CATEGORY_IDS, childCategoryIds))
        }
        searchParams.add(RequestParam(DealsSearchConstants.MAP_COORDINATES, locationCoordinates))
        searchParams.add(RequestParam(DealsSearchConstants.MAP_LOCATION_TYPE, locationType))
        searchParams.add(RequestParam(DealsSearchConstants.MAP_PAGE, page))
        if (searchQuery.isNotEmpty()) {
            searchParams.add(RequestParam(DealsSearchConstants.MAP_TAGS, searchQuery))
        }
        return searchParams
    }
}