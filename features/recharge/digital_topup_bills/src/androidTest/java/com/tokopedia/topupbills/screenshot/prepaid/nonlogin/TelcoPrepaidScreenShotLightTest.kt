package com.tokopedia.topupbills.screenshot.prepaid.nonlogin

class TelcoPrepaidScreenShotLightTest: BaseTelcoPrepaidScreenShotTest() {

    override fun filePrefix(): String = "telco-prepaid"

    override fun forceDarkMode(): Boolean = false
}