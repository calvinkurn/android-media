package com.tokopedia.discovery2.data.emptystate

data class EmptyStateModel(
        var isHorizontal: Boolean = false,
        var title: String = "",
        var description: String = "",
        var buttonText: String = "",
        var imageURL: String? = "",
        var buttonApplink: String? = "",
        var isFilterState: Boolean = false,
        var isRedirectionState: Boolean = false,
)
