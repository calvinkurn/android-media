package com.tokopedia.rechargegeneral.screenshot.login

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseListrikScreenShotTest

class ListrikScreenShotLoginLightTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun isLogin(): Boolean = true
}