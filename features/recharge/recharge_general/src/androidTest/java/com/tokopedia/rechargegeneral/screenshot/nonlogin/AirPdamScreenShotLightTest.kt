package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseAirPdamScreenShotTest

class AirPdamScreenShotLightTest: BaseAirPdamScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun isLogin(): Boolean = false

}