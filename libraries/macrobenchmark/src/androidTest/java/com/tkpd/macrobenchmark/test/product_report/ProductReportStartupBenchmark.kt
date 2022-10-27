package com.tkpd.macrobenchmark.test.searchresult

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
class ProductReportStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    @Before
    fun setupEnvironment() {
        MacroDevOps.setupEnvironment(MacroIntent.ProductReport.getStartupIntent())
    }

    override fun getIntent() = MacroIntent.ProductReport.getStartupIntent()

    override fun traceName() = "product_report_trace"

    override fun waitUntil() {
        Thread.sleep(5000)
        MacroInteration.waitforColumnInCompose(MacroIntent.ProductReport.COLUMN_TAG)
    }
}
