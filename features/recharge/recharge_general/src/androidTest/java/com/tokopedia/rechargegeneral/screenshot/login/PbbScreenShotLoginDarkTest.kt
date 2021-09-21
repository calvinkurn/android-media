package com.tokopedia.rechargegeneral.screenshot.login

import com.tokopedia.rechargegeneral.screenshot.base.product.BasePbbScreenShotTest

class PbbScreenShotLoginDarkTest: BasePbbScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = true

}