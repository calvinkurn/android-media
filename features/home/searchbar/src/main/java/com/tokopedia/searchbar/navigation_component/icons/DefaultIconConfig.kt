package com.tokopedia.searchbar.navigation_component.icons

object DefaultIconConfig {
    fun get(): IconConfig {
        return IconConfig(listOf(IconList.MessageIcon.get(), IconList.NotificationIcon.get(), IconList.CartIcon.get(), IconList.NavGlobalIcon.get()))
    }
}