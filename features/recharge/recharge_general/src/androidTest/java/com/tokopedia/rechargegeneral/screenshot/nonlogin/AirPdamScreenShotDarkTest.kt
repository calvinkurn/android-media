package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseAirPdamScreenShotTest

class AirPdamScreenShotDarkTest: BaseAirPdamScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = false
}