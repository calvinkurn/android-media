package com.tokopedia.rechargegeneral.screenshot.login

import com.tokopedia.rechargegeneral.screenshot.base.product.BasePbbScreenShotTest

class PbbScreenShotLoginLightTest: BasePbbScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun isLogin(): Boolean = true

}