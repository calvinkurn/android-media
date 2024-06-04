package com.tokopedia.analytics.byteio

interface AppLogInterface {

    fun getPageName() : String

    fun getEnterFrom() = getPageName()

    fun isEnterFromWhitelisted() : Boolean = false

    fun isShadow() : Boolean = false

    fun shouldTrackEnterPage() : Boolean = false
}
