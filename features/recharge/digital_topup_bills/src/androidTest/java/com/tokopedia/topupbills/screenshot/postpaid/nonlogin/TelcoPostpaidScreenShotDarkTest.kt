package com.tokopedia.topupbills.screenshot.postpaid.nonlogin

class TelcoPostpaidScreenShotDarkTest : BaseTelcoPostpaidScreenShotTest() {

    override fun filePrefix(): String = "telco-postpaid-nonlogin"

    override fun forceDarkMode(): Boolean = true
}