package com.tokopedia.search.result.presentation.model

data class ChildViewVisibilityModel(
        val isChildViewVisibleToUser: Boolean = false,
        val isChildViewReady: Boolean = false,
        val isFilterEnabled: Boolean = false,
        val isSortEnabled: Boolean = false
)