package com.tokopedia.rechargegeneral.screenshot

class RechargeGeneralScreenShotDarkTest: BaseRechargeGeneralScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "recharge_general-dark"
}