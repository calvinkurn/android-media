package com.tokopedia.stickylogin.view

interface DarkModeListener {
    fun isDarkModeOn(): Boolean
    fun onDarkMode()
    fun onLightMode()
}