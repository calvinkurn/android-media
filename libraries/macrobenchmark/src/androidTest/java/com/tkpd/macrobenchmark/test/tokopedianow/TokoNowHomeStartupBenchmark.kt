package com.tkpd.macrobenchmark.test.tokopedianow

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
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
    override fun getIntent() = MacroIntent.TokopediaNow.getHomeIntent()
}
