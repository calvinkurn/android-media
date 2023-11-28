package com.tokopedia.review.feature.reading.presentation.uimodel

data class SelectedFilters(
    var withMedia: FilterType.FilterWithMedia? = null,
    var rating: FilterType.FilterRating? = null,
    var topic: FilterType.FilterTopic? = null
) {
    fun isAnyFilterSelected(): Boolean {
        return withMedia != null || rating != null || topic != null
    }

    fun clear() {
        withMedia = null
        rating = null
        topic = null
    }

    fun mapFilterToRequestParams(): String {
        if (!isAnyFilterSelected()) return ""
        val listOfFilters = mutableListOf<FilterType>()
        withMedia?.let { listOfFilters.add(it) }
        rating?.let { listOfFilters.add(it) }
        topic?.let { listOfFilters.add(it) }
        return listOfFilters.joinToString(separator = ";") { "${it.param}=${it.value}" }
    }

    fun getSelectedParam(): String {
        val params = mutableListOf<String>()
        withMedia?.let {
            params.add("image")
            params.add("video")
        }
        rating?.let { params.add("${it.param}=${it.value}") }
        return params.joinToString(";")
    }
}
