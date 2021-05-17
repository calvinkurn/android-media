package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.Base.BaseListrikScreenShotTest

class ListrikScreenShotLightTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "rg_listrik-light"
}