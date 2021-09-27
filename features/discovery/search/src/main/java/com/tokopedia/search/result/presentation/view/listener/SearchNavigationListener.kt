package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.discovery.common.model.SearchParameter

interface SearchNavigationListener {
    fun setupSearchNavigation(clickListener: ClickListener?)
    fun refreshMenuItemGridIcon(titleResId: Int, iconResId: Int)
    fun removeSearchPageLoading()
    interface ClickListener {
        fun onChangeGridClick()
    }
    fun updateSearchParameter(searchParameter: SearchParameter?)
}