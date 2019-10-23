package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener

data class ChildViewVisibilityChangedModel(
        val isChildViewVisibleToUser: Boolean = false,
        val isChildViewReady: Boolean = false,
        val isFilterEnabled: Boolean = false,
        val isSortEnabled: Boolean = false,
        val searchNavigationOnClickListener: SearchNavigationListener.ClickListener? = null
)