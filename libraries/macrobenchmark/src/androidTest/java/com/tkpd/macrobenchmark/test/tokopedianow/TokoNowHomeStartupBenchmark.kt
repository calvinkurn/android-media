package com.tkpd.macrobenchmark.test.tokopedianow

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by Reza
 * This test will measure TokoNowHomeActivity launch time with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class TokoNowHomeStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
    }

    override fun getIntent() = MacroIntent.TokopediaNow.getHomeIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
                MacroIntent.TokopediaNow.PACKAGE_NAME,
                MacroIntent.TokopediaNow.RV_RESOURCE_ID,
        )
        MacroInteration.waitForRecyclerViewContent(
                MacroIntent.TokopediaNow.PACKAGE_NAME,
                MacroIntent.TokopediaNow.RV_RESOURCE_ID,
        )
    }

    override fun traceName() = "mp_tokonow_home"

}
