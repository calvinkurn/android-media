package com.tokopedia.discovery.categoryrevamp.fragments

interface CategoryNavigationListener {

    fun setupSearchNavigation(clickListener: ClickListener)


    interface ClickListener {
        fun onFilterClick()
        fun onSortClick()
        fun onChangeGridClick()
    }
}