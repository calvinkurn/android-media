package com.tkpd.macrobenchmark.test.playlive

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by kenny.hadisaputra on 04/10/22
 */
@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class PlayLiveStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {

    override fun setupEnvironment() {}

    override fun setupMock() {}

    override fun getIntent() = MacroIntent.Play.getPlayLiveIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.Play.PACKAGE_NAME,
            MacroIntent.Play.VIEW_PAGER_RESOURCE_ID
        )
    }

    override fun traceName() = "plt_play_page"

}

