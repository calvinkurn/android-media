package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.Base.BasePbbScreenShotTest

class PbbScreenShotLightTest: BasePbbScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "rg_pbb-light"
}