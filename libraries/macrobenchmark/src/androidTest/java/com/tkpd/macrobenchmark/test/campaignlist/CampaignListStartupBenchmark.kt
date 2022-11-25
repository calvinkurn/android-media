package com.tkpd.macrobenchmark.test.campaignlist

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class CampaignListStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun setupMock() {

    }

    override fun setupEnvironment() {

    }

    override fun getIntent() = MacroIntent.CampaignList.getCampaignListIntent()

    override fun waitUntil() {
        MacroInteration.waitForComposableWidgetVisible(
            widgetContentDescription = MacroIntent.CampaignList.RECYCLER_VIEW_WIDGET_CONTENT_DESCRIPTION
        )
    }

    override fun traceName() = "mp_campaign_list"

    override fun packageName() = MacroIntent.TKPD_PACKAGE_SELLER_APP
}
