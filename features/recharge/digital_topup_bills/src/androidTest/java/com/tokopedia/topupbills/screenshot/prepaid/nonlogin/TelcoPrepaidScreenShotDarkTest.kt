package com.tokopedia.topupbills.screenshot.prepaid.nonlogin

class TelcoPrepaidScreenShotDarkTest: BaseTelcoPrepaidScreenShotTest() {

    override fun filePrefix(): String = "telco-prepaid"

    override fun forceDarkMode(): Boolean = true
}