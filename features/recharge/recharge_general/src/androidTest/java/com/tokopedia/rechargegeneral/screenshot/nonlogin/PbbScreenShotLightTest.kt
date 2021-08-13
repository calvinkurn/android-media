package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BasePbbScreenShotTest

class PbbScreenShotLightTest: BasePbbScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun isLogin(): Boolean = false
}