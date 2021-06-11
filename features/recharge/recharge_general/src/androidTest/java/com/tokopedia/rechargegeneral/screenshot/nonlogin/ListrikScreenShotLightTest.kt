package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseListrikScreenShotTest

class ListrikScreenShotLightTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun isLogin(): Boolean = false
}