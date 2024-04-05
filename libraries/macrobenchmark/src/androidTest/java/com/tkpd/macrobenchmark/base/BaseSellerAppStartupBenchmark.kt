package com.tkpd.macrobenchmark.base

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by @ilhamsuaib on 2/29/24.
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
abstract class BaseSellerAppStartupBenchmark(startupMode: StartupMode) :
    BaseStartupBenchmark(startupMode) {

    companion object {
        const val EMAIL = "homeanalytics.prod.seller+frontendtest@tokopedia.com"
        const val PASSWORD = "tokopedia"
    }

    override fun setupEnvironment() {
        MacroDevOps.skipOnboardingSellerApp()
        MacroDevOps.setupLoginFlow(
            packageName = packageName(),
            email = email(),
            password = password()
        )
    }

    override fun packageName(): String = MacroIntent.TKPD_PACKAGE_SELLER_APP

    override fun setupMock() {
    }

    open fun email() = EMAIL

    open fun password() = PASSWORD
}
