package com.tokopedia.discovery.categoryrevamp.view

interface CategoryNavigationListener {

    fun setupSearchNavigation(clickListener: ClickListener)


    interface ClickListener {
        fun onFilterClick()
        fun onSortClick()
        fun onChangeGridClick()
    }
}