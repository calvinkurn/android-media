package com.tkpd.macrobenchmark.test.home
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
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
class HomeFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
        MacroDevOps.setupEnvironment(MacroIntent.Session.getSessionMacroSetupIntent())
        MacroDevOps.setupEnvironment(MacroIntent.Home.getHomeIntent().apply {
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
    }

    override fun setupMock() {
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getHomeMockIntent())
    }

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
                MacroIntent.TKPD_PACKAGE_NAME,
                MacroIntent.Home.RV_RESOURCE_ID
        )
    }

    override fun getIntent() = MacroIntent.Home.getHomeIntent()
}
