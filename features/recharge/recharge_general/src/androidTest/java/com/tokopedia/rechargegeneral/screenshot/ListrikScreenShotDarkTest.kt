package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.Base.BaseListrikScreenShotTest

class ListrikScreenShotDarkTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "rg_listrik-dark"

}