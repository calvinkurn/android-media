package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BasePbbScreenShotTest

class PbbScreenShotDarkTest: BasePbbScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = false
}