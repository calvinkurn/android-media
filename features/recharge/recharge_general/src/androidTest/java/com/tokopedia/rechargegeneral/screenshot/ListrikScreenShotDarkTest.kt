package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.base.BaseListrikScreenShotTest

class ListrikScreenShotDarkTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "rg_listrik-dark"

}