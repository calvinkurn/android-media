package com.tokopedia.searchbar.navigation_component.icons

import com.tokopedia.searchbar.navigation_component.IconConfig

object DefaultIconConfig {
    fun get(): IconConfig {
        return IconConfig(listOf(MessageIcon.get(), NotificationIcon.get(), CartIcon.get(), NavGlobalIcon.get()))
    }
}