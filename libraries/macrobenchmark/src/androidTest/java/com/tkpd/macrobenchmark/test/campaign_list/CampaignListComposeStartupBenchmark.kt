package com.tkpd.macrobenchmark.test.campaign_list

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class CampaignListComposeStartupBenchmark(startupMode: StartupMode) :
    BaseStartupBenchmark(startupMode) {

    override fun getIntent() = MacroIntent.CampaignList.getCampaignListIntent()

    override fun waitUntil() {
        MacroInteration.waitUntilActivityShown(MacroIntent.CampaignList.PACKAGE_NAME)
    }

    override fun traceName() = "mp_campaign_list"
}
