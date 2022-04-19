package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseListrikScreenShotTest

class ListrikScreenShotDarkTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = false
}