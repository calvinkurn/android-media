package com.tkpd.macrobenchmark.test.home

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
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
class HomeStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun setupEnvironment() {
        MacroDevOps.skipOnboarding()
        MacroDevOps.setupLoginFlow()
    }

    override fun setupMock() {
    }

    override fun getIntent() = MacroIntent.Home.getHomeIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.Home.RV_RESOURCE_ID
        )
        Thread.sleep(10000)
    }

    override fun traceName() = "home"
}
