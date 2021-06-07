package com.tokopedia.rechargegeneral.screenshot.login

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseListrikScreenShotTest

class ListrikScreenShotLoginDarkTest: BaseListrikScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = true

}