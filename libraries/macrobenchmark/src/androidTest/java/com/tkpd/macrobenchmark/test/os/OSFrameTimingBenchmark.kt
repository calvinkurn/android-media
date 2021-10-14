package com.tkpd.macrobenchmark.test.os
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.Before
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
class OSFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    @Before
    fun setupEnvironment() {
        MacroDevOps.setupEnvironment(MacroIntent.OfficialStore.getOsMacroSetupIntent())
    }

    override fun pageInteractionTest() {
        MacroInteration.basicRecyclerviewInteraction(MacroIntent.OfficialStore.RV_RESOURCE_ID)
    }

    override fun getIntent() = MacroIntent.OfficialStore.getOsIntent()
}
