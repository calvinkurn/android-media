package com.tkpd.macrobenchmark.test.play

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.Direction
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 04/10/22
 */
@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class PlayFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
            MacroIntent.Play.PACKAGE_NAME,
            MacroIntent.Play.VIEW_PAGER_RESOURCE_ID,
            Direction.RIGHT,
            scrollPercent = 90f,
        )
    }

    override fun getIntent() = MacroIntent.Play.getPlayIntent()
}
