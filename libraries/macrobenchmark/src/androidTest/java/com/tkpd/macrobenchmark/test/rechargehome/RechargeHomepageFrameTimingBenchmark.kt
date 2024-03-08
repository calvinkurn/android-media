package com.tkpd.macrobenchmark.test.rechargehome

import android.content.Intent
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
class RechargeHomepageFrameTimingBenchmark: BaseFrameTimingBenchmark() {

    override fun getIntent(): Intent = MacroIntent.RechargeHomepage.getRechargeHomepageIntent()

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.RechargeHomepage.RV_RECHARGE_HOMEPAGE
        )
    }

    override fun setupMock() {
        // no-op
    }

    override fun setupEnvironment() {
        // no-op
    }
}
