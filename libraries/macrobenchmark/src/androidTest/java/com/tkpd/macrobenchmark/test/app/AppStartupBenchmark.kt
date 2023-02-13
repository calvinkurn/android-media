package com.tkpd.macrobenchmark.test.app

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
 * This test will measure App Startup Time launch time with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class AppStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    /**
     * To disable introduction page
     */
    override fun getIntent() = MacroIntent.App.getAppLauncherIntent()

    override fun waitUntil() {
        Thread.sleep(1000)
    }

    override fun traceName() = ""

    override fun setupMock() {
    }

    override fun setupEnvironment() {
    }
}
