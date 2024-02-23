package com.tkpd.macrobenchmark.test.home
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by DevAra
 * This test will measure MainParentActivity janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class HomeFrameTimingBenchmark : BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
        MacroDevOps.skipOnboarding()
        MacroDevOps.setupLoginFlow()
    }

    override fun setupMock() {
    }

    override fun pageInteractionTest(currentIteration: Int) {
        Thread.sleep(10000)
        MacroInteration.basicFlingInteraction(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.Home.RV_RESOURCE_ID,
            flingSpeed = 3000
        )
    }

    override fun getIntent() = MacroIntent.Home.getHomeIntent()
}
