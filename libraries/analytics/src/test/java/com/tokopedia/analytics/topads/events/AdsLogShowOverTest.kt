package com.tokopedia.analytics.topads.events

import android.app.Activity
import android.content.Context
import com.bytedance.applog.AppLog
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.analytics.topads.assertShowOverOutsideSRP
import com.tokopedia.analytics.topads.assertShowOverSRP
import com.tokopedia.config.GlobalConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AdsLogShowOverTest {

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
    fun `given show over model, then convert to json object and send show over event to App Log in SRP `() {
        val showOverModel = spyk(AdsLogShowOverModel(
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogShowOverModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra",
                sizePercent = "75"
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

        val showOverJSONObject = showOverModel.toJSONObject(context)

        AppLogTopAds.sendEventShowOver(context, showOverModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventShowOver(context, showOverModel)
            AppLog.onEventV3(AdsLogConst.Event.SHOW, showOverJSONObject)
        }

        showOverJSONObject.assertShowOverSRP(showOverModel)
    }

    @Test
    fun `given show over model, then convert to json object and send show over event to App Log in outside SRP `() {
        val showOverModel = spyk(AdsLogShowOverModel(
            adsValue = 123456,
            logExtra = "{rit:123456}",
            adExtraData = AdsLogShowOverModel.AdExtraData(
                productId = "987654321",
                productName = "samsung galaxy s23 ultra",
                sizePercent = "100"
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

        val showOverJSONObject = showOverModel.toJSONObject(context)

        AppLogTopAds.sendEventShowOver(context, showOverModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventShowOver(context, showOverModel)
            AppLog.onEventV3(AdsLogConst.Event.SHOW, showOverJSONObject)
        }

        showOverJSONObject.assertShowOverOutsideSRP(showOverModel)
    }

    @Test
    fun `when show over event is triggered, the size percentage should not be zero`() {
        var sizePercentage = "45"

        val showModel = spyk(AdsLogShowModel(
            adsValue = 123457,
            logExtra = "{rit:123457}",
            adExtraData = AdsLogShowModel.AdExtraData(
                productId = "987654621",
                productName = "samsung galaxy s23 ultra",
            )
        ))

        every {
            GlobalConfig.isAllowDebuggingTools()
        } returns true

        val showJSONObject = showModel.toJSONObject(context)

        AppLogTopAds.sendEventShow(context, showModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventShow(context, showModel)
            AppLog.onEventV3(AdsLogConst.Event.SHOW, showJSONObject)
        }

        val showOverModel = spyk(AdsLogShowOverModel(
            adsValue = 123457,
            logExtra = "{rit:123457}",
            adExtraData = AdsLogShowOverModel.AdExtraData(
                productId = "987654621",
                productName = "samsung galaxy s23 ultra",
                sizePercent = sizePercentage
            )
        ))

        val showOverJSONObject = showOverModel.toJSONObject(context)

        AppLogTopAds.sendEventShowOver(context, showOverModel)

        verify(atLeast = 1) {
            AppLogTopAds.sendEventShowOver(context, showOverModel)
            AppLog.onEventV3(AdsLogConst.Event.SHOW, showOverJSONObject)
        }

        val adExtraData = showOverJSONObject[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject

        val actualSizePercentage = adExtraData[AdsLogConst.Param.SIZE_PERCENT]

        assert(actualSizePercentage != 0)

        sizePercentage = "0"

        //then after sizePercentage is triggered, size percentage should be 0
        assertEquals(sizePercentage, "0")
    }
}
