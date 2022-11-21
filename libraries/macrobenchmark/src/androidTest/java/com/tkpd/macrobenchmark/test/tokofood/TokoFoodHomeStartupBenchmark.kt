package com.tkpd.macrobenchmark.test.tokofood

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by RizqiAryansa
 * This test will measure BaseTokofoodActivity launch time with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class TokoFoodHomeStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
    }

    override fun getIntent() = MacroIntent.TokoFood.getHomeIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
                MacroIntent.TokoFood.PACKAGE_NAME,
                MacroIntent.TokoFood.RV_RESOURCE_ID,
        )
        MacroInteration.waitForRecyclerViewContent(
                MacroIntent.TokoFood.PACKAGE_NAME,
                MacroIntent.TokoFood.RV_RESOURCE_ID,
        )
    }

    override fun traceName() = "mp_tokofood_home"

}
