package com.tokopedia.navigation_common.listener

interface HomeBottomNavListener {
    fun setForYouToHomeMenuTabSelected()

    fun setHomeToForYouTabSelected()

    fun isIconJumperEnabled(): Boolean
}
