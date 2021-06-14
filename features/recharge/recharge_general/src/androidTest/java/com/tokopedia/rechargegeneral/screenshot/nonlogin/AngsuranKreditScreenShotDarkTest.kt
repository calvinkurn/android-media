package com.tokopedia.rechargegeneral.screenshot.nonlogin

import com.tokopedia.rechargegeneral.screenshot.base.product.BaseAngsuranKreditScreenShotTest

class AngsuranKreditScreenShotDarkTest: BaseAngsuranKreditScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun isLogin(): Boolean = false
}