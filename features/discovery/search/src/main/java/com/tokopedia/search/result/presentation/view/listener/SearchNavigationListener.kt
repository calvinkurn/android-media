package com.tokopedia.search.result.presentation.view.listener

interface SearchNavigationListener {
    fun setupSearchNavigation(clickListener: ClickListener?)
    fun refreshMenuItemGridIcon(titleResId: Int, iconResId: Int)
    fun removeSearchPageLoading()
    interface ClickListener {
        fun onChangeGridClick()
    }
}