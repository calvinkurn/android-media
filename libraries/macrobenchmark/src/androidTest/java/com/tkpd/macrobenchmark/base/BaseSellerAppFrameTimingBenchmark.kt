package com.tkpd.macrobenchmark.base

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import org.junit.runner.RunWith

/**
 * Created by @ilhamsuaib on 2/29/24.
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
abstract class BaseSellerAppFrameTimingBenchmark : BaseFrameTimingBenchmark() {

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

    open fun email() = BaseSellerAppStartupBenchmark.EMAIL

    open fun password() = BaseSellerAppStartupBenchmark.PASSWORD
}
