package com.tkpd.macrobenchmark.test.report

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
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
class ProductReportStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun setupMock() {
    }

    override fun setupEnvironment() {
    }

    override fun getIntent() = MacroIntent.ProductReport.getIntent()

    override fun traceName() = ""

    override fun waitUntil() {
        MacroInteration.waitForComposableWidgetVisible(MacroIntent.ProductReport.COLUMN_TAG)
    }
}
