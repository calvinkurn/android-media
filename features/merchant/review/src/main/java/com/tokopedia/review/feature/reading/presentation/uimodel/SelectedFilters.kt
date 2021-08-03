package com.tokopedia.review.feature.reading.presentation.uimodel

data class SelectedFilters(
        var withImage: FilterType.FilterWithImage? = null,
        var rating: FilterType.FilterRating? = null,
        var topic: FilterType.FilterTopic? = null
) {
    fun isAnyFilterSelected(): Boolean {
        return withImage != null || rating != null || topic != null
    }

    fun clear() {
        withImage = null
        rating = null
        topic = null
    }

    fun mapFilterToRequestParams(): String {
        if (!isAnyFilterSelected()) return ""
        val listOfFilters = mutableListOf<FilterType>()
        withImage?.let { listOfFilters.add(it) }
        rating?.let { listOfFilters.add(it) }
        topic?.let { listOfFilters.add(it) }
        return listOfFilters.joinToString(separator = ";") { "${it.param}=${it.value}" }
    }
}