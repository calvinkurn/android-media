package com.tokopedia.searchbar.navigation_component.listener

interface TopNavComponentListener {
    fun getUserId(): String
    fun isLoggedIn(): Boolean
    fun getPageName(): String
}