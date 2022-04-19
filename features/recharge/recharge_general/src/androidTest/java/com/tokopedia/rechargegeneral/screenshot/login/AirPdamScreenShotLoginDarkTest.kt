package com.tokopedia.rechargegeneral.screenshot.login

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseAirPdamScreenShotTest

class AirPdamScreenShotLoginDarkTest: BaseAirPdamScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = true

}