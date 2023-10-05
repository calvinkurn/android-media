package com.tkpd.macrobenchmark.test.recharge

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class DigitalPDPPulsaFrameTimingBenchmark : BaseFrameTimingBenchmark() {

    override fun setupEnvironment() {}

    override fun setupMock() {}

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.DigitalProductDetail.RV_DENOM_GRID
        )
    }

    override fun getIntent() = MacroIntent.DigitalProductDetail.getPulsaPdpIntent()
}
