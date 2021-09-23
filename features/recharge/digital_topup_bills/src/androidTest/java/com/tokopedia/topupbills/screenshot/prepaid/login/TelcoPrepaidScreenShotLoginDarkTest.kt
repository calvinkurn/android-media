package com.tokopedia.topupbills.screenshot.prepaid.login

class TelcoPrepaidScreenShotLoginDarkTest : BaseTelcoPrepaidScreenShotLoginTest() {

    override fun filePrefix(): String = "telco-prepaid"

    override fun forceDarkMode(): Boolean = true
}