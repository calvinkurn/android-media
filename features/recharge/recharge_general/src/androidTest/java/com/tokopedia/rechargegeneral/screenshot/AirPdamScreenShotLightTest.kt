package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.base.BaseAirPdamScreenShotTest

class AirPdamScreenShotLightTest: BaseAirPdamScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "rg_airpdam-light"

}