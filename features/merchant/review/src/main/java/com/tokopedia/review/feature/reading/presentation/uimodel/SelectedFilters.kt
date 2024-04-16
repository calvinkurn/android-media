package com.tokopedia.review.feature.reading.presentation.uimodel

data class SelectedFilters(
    var withMedia: FilterType.FilterWithMedia? = null,
    var rating: FilterType.FilterRating? = null,
    var topic: FilterType.FilterTopic? = null,
    var variant: FilterType.FilterVariant? = null,
) {
    fun isAnyFilterSelected(): Boolean {
        return withMedia != null || rating != null || topic != null ||
            variant != null
    }

    fun clear() {
        withMedia = null
        rating = null
        topic = null
        variant = null
    }

    fun mapFilterToRequestParams(): String {
        if (!isAnyFilterSelected()) return ""
        val listOfFilters = mutableListOf<FilterType>()
        withMedia?.let { listOfFilters.add(it) }
        rating?.let { listOfFilters.add(it) }
        topic?.let { listOfFilters.add(it) }
        variant?.let { listOfFilters.add(it) }
        return listOfFilters.joinToString(separator = ";") {
            if (it.param.isEmpty()) it.value
            else "${it.param}=${it.value}"
        }
    }

    fun getSelectedParam(): String {
        val params = mutableListOf<String>()
        withMedia?.let {
            params.add("image")
            params.add("video")
        }
        rating?.let { params.add("${it.param}=${it.value}") }
        variant?.let { params.add(it.value) }
        return params.joinToString(";")
    }
}
