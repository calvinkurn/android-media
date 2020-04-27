package com.tokopedia.discovery.categoryrevamp.view.interfaces

interface CategoryNavigationListener {

    fun setupSearchNavigation(clickListener: ClickListener)

    fun setUpVisibleFragmentListener(visibleClickListener: VisibleClickListener)

    fun hideBottomNavigation()


    interface ClickListener {
        fun onChangeGridClick()

    }

    interface VisibleClickListener {
        fun onSortClick()
        fun onFilterClick()
        fun onShareButtonClick()
    }
}