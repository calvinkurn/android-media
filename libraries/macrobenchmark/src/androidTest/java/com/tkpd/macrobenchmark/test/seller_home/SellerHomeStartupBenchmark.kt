package com.tkpd.macrobenchmark.test.seller_home

import android.content.Intent
import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseSellerAppStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by @ilhamsuaib
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class SellerHomeStartupBenchmark(startupMode: StartupMode) : BaseSellerAppStartupBenchmark(startupMode) {

    companion object {
        const val TRACE_NAME = "seller_home"
    }

    override fun getIntent(): Intent = MacroIntent.SellerHome.getIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            packageName(),
            MacroIntent.SellerHome.RV_RESOURCE_ID
        )
        Thread.sleep(5000)
    }

    override fun traceName(): String = TRACE_NAME
}
