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

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class SellerHomeStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {

    companion object {
        private const val PACKAGE_NAME = MacroIntent.TKPD_PACKAGE_SELLER_APP
        private const val TRACE_NAME = "seller_home"
        private const val EMAIL = "homeanalytics.prod.seller+frontendtest@tokopedia.com"
        private const val PASSWORD = "tokopedia"
    }

    override fun setupEnvironment() {
        MacroDevOps.skipOnboardingSellerApp()
        MacroDevOps.setupLoginFlow(
            packageName = PACKAGE_NAME,
            email = EMAIL,
            password = PASSWORD
        )
    }

    override fun setupMock() {
    }

    override fun getIntent(): Intent = MacroIntent.SellerHome.getIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            PACKAGE_NAME,
            MacroIntent.SellerHome.RV_RESOURCE_ID
        )
        Thread.sleep(5000)
    }

    override fun traceName(): String = TRACE_NAME

    override fun packageName(): String = PACKAGE_NAME
}
