package com.tokopedia.discovery.categoryrevamp.view.fragments.activity

interface CategoryNavigationListener {

    fun setupSearchNavigation(clickListener: ClickListener)


    interface ClickListener {
        fun onFilterClick()
        fun onSortClick()
        fun onChangeGridClick()
    }
}