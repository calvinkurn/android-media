package com.tokopedia.analytics.topads.events

import android.app.Activity
import android.content.Context
import com.bytedance.applog.AppLog
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.topads.assertShowOutsideSRP
import com.tokopedia.analytics.topads.assertShowSRP
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

class AdsLogShowTest {

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
    fun `given show model, then convert to json object and send show event to App Log in SRP `() {
        val showOverModel = spyk(AdsLogShowModel(
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogShowModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra",
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

        val showJSONObject = showOverModel.toJSONObject(context)

        AppLogTopAds.sendEventShow(context, showOverModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventShow(context, showOverModel)
            AppLog.onEventV3(AdsLogConst.Event.SHOW, showJSONObject)
        }

        showJSONObject.assertShowSRP(showOverModel)
    }

    @Test
    fun `given show model, then convert to json object and send show event to App Log in outside SRP `() {
        val showModel = spyk(AdsLogShowModel(
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogShowModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra",
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

        val showJSONObject = showModel.toJSONObject(context)

        AppLogTopAds.sendEventShow(context, showModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventShow(context, showModel)
            AppLog.onEventV3(AdsLogConst.Event.SHOW, showJSONObject)
        }

        showJSONObject.assertShowOutsideSRP(showModel)
    }
}
