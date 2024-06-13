package com.tokopedia.analytics.topads.events

import android.app.Activity
import android.content.Context
import com.bytedance.applog.AppLog
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.topads.assertRealtimeClickOutsideSRP
import com.tokopedia.analytics.topads.assertRealtimeClickSRP
import com.tokopedia.config.GlobalConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class AdsLogRealtimeClickTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        AppLogTopAds.clearAdsPageData()
        context = mockk(relaxed = true)
        mockkStatic(GlobalConfig::class)
    }

    @After
    fun finish() {
        unmockkAll()
    }

    @Test
    fun `given realtime click area model, then convert to json object and send to App Log in SRP `() {
        val realtimeClickModel = spyk(AdsLogRealtimeClickModel(
            refer = AdsLogConst.Refer.AREA,
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogRealtimeClickModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra"
            )
        ))

        val homePageAct = mockk<Activity>(relaxed = true)
        val srpAct = mockk<Activity>(relaxed = true)

        every {
            GlobalConfig.isAllowDebuggingTools()
        } returns true

        AppLogTopAds.putAdsPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.HOME)
        AppLogTopAds.currentPageName = PageName.HOME
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT

        val realtimeJSONObject = realtimeClickModel.toJSONObject(context)

        AppLogTopAds.sendEventRealtimeClick(context, realtimeClickModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventRealtimeClick(context, realtimeClickModel)
            AppLog.onEventV3(AdsLogConst.Event.REALTIME_CLICK, realtimeJSONObject)
        }

        realtimeJSONObject.assertRealtimeClickSRP(realtimeClickModel)
    }

    @Test
    fun `given realtime click area model, then convert to json object and send to App Log in outside SRP `() {
        val realtimeClickModel = spyk(AdsLogRealtimeClickModel(
            refer = AdsLogConst.Refer.COVER,
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogRealtimeClickModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra"
            )
        ))

        val homePageAct = mockk<Activity>(relaxed = true)
        val inboxAct = mockk<Activity>(relaxed = true)

        every {
            GlobalConfig.isAllowDebuggingTools()
        } returns true

        AppLogTopAds.putAdsPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.HOME)
        AppLogTopAds.currentPageName = PageName.HOME
        AppLogTopAds.putAdsPageData(inboxAct, AppLogParam.PAGE_NAME, PageName.INBOX)
        AppLogTopAds.currentPageName = PageName.INBOX

        val realtimeJSONObject = realtimeClickModel.toJSONObject(context)

        AppLogTopAds.sendEventRealtimeClick(context, realtimeClickModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventRealtimeClick(context, realtimeClickModel)
            AppLog.onEventV3(AdsLogConst.Event.REALTIME_CLICK, realtimeJSONObject)
        }

        realtimeJSONObject.assertRealtimeClickOutsideSRP(realtimeClickModel)
    }

    @Test
    fun `given realtime click seller info model, then convert to json object and send to App Log in outside SRP `() {
        val realtimeClickModel = spyk(AdsLogRealtimeClickModel(
            refer = AdsLogConst.Refer.COVER,
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogRealtimeClickModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra"
            )
        ))

        val homePageAct = mockk<Activity>(relaxed = true)
        val rewardPage = mockk<Activity>(relaxed = true)

        every {
            GlobalConfig.isAllowDebuggingTools()
        } returns true

        AppLogTopAds.putAdsPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.HOME)
        AppLogTopAds.currentPageName = PageName.HOME
        AppLogTopAds.putAdsPageData(rewardPage, AppLogParam.PAGE_NAME, PageName.REWARD)
        AppLogTopAds.currentPageName = PageName.REWARD

        val realtimeJSONObject = realtimeClickModel.toJSONObject(context)

        AppLogTopAds.sendEventRealtimeClick(context, realtimeClickModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventRealtimeClick(context, realtimeClickModel)
            AppLog.onEventV3(AdsLogConst.Event.REALTIME_CLICK, realtimeJSONObject)
        }

        realtimeJSONObject.assertRealtimeClickOutsideSRP(realtimeClickModel)
    }
}
