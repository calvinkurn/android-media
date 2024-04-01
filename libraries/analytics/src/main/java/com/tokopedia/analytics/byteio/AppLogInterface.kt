package com.tokopedia.analytics.byteio

interface AppLogInterface {

    fun getPageName() : String

    fun isEnterFromWhitelisted() : Boolean = false

    fun isShadow() : Boolean = false
}
