package com.tkpd.macrobenchmark.test.campaignlist
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
class CampaignListFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun setupMock() {

    }

    override fun setupEnvironment() {

    }

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicComposableListInteraction("Campaign List")
    }

    override fun getIntent() = MacroIntent.CampaignList.getCampaignListIntent()


}
