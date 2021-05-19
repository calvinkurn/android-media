package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.base.BaseAngsuranKreditScreenShotTest

class AngsuranKreditScreenShotDarkTest: BaseAngsuranKreditScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "rg_angsuran_kredit-dark"

}