package com.tkpd.macrobenchmark.test.rechargehome

import android.content.Intent
import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class RechargeHomepageStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    override fun getIntent(): Intent = MacroIntent.RechargeHomepage.getRechargeHomepageIntent()

    override fun setupEnvironment() {
        // no-op
    }

    override fun setupMock() {
        // no-op
    }

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.RechargeHomepage.RV_RECHARGE_HOMEPAGE
        )
    }

    override fun traceName(): String = "plt_recharge_home_page"
}
