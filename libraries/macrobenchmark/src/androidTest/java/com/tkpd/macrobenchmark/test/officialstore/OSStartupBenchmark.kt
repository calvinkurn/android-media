package com.tkpd.macrobenchmark.test.officialstore

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by DevAra
 * This test will measure MainParentActivity launch time with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class OSStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    @Before
    fun setupEnvironment() {
        MacroDevOps.setupEnvironment(MacroIntent.OfficialStore.getOsMacroSetupIntent())
    }
    override fun getIntent() = MacroIntent.OfficialStore.getOsIntent()

    override fun waitUntil() {
        MacroInteration.waitUntilRecyclerViewExist(
                MacroIntent.TKPD_PACKAGE_NAME,
                MacroIntent.OfficialStore.RV_RESOURCE_ID,
        )
        MacroInteration.waitForRecyclerViewContent(
                MacroIntent.TKPD_PACKAGE_NAME,
                MacroIntent.OfficialStore.RV_RESOURCE_ID,
        )
    }
}
