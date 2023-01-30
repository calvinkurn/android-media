package com.tkpd.macrobenchmark.test.reschedulepickup

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
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
class ReschedulePickupFrameTimingBenchmark : BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
    }
    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicComposableInteraction(
            MacroIntent.ReschedulePickup.CONTENT_DESCRIPTION
        )
    }

    override fun getIntent() = MacroIntent.ReschedulePickup.getIntent()

    override fun packageName(): String {
        return MacroIntent.TKPD_PACKAGE_SELLER_APP
    }
}
