package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.base.BaseAirPdamScreenShotTest

class AirPdamScreenShotDarkTest: BaseAirPdamScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "rg_airpdam-dark"

}