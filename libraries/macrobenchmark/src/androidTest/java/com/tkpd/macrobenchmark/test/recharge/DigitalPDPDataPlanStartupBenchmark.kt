package com.tkpd.macrobenchmark.test.recharge

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
class DigitalPDPDataPlanStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun getIntent(): Intent = MacroIntent.DigitalProductDetail.getDataPlanPdpIntent()

    override fun setupEnvironment() {
        // no-op
    }

    override fun setupMock() {
        // no-op
    }

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.DigitalProductDetail.RV_DENOM_FULL
        )
    }

    override fun traceName(): String = "plt_pdp_data_plan_page"
}
