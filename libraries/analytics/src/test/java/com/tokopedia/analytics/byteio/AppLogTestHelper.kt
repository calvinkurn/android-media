package com.tokopedia.analytics.byteio


internal typealias SUT = AppLogAnalytics


internal val ActivityBasicOne = object : AppLogInterface {
    override fun getPageName(): String = "Page One"
}

internal val ActivityBasicTwo = object : AppLogInterface {
    override fun getPageName(): String = "Page Two"
}

internal val ActivityBasicThree = object : AppLogInterface {
    override fun getPageName(): String = "Page Three"
}

internal val ActivityPdpOne = object : AppLogInterface {
    override fun getPageName(): String = PageName.PDP
}

internal val ActivityPdpTwo = object : AppLogInterface {
    override fun getPageName(): String = PageName.PDP
}


internal val ActivityEnterFrom = object : AppLogInterface {
    override fun getPageName(): String = "Page EnterFrom"
    override fun isEnterFromWhitelisted(): Boolean = true
}

internal val ActivityShadow = object : AppLogInterface {
    override fun getPageName(): String = ""
    override fun isShadow(): Boolean = true
}
