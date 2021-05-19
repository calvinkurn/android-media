package com.tokopedia.rechargegeneral.screenshot

import com.tokopedia.rechargegeneral.screenshot.base.BaseAngsuranKreditScreenShotTest

class AngsuranKreditScreenShotLightTest: BaseAngsuranKreditScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "rg_angsuran_kredit-light"

}