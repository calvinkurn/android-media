package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.Base.BasePbbScreenShotTest

class PbbScreenShotDarkTest: BasePbbScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "rg_pbb-dark"
}