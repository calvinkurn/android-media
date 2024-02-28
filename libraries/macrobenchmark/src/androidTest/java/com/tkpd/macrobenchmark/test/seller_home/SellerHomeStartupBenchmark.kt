package com.tkpd.macrobenchmark.test.seller_home

import android.content.Intent
import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
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
class SellerHomeStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {

    companion object {
        const val TRACE_NAME = "seller_home"
        const val EMAIL = "homeanalytics.prod.seller+frontendtest@tokopedia.com"
        const val PASSWORD = "tokopedia"
    }

    override fun setupEnvironment() {
        MacroDevOps.skipOnboardingSellerApp()
        MacroDevOps.setupLoginFlow(
            packageName = packageName(),
            email = EMAIL,
            password = PASSWORD
        )
    }

    override fun setupMock() {
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

    override fun packageName(): String = MacroIntent.TKPD_PACKAGE_SELLER_APP
}
