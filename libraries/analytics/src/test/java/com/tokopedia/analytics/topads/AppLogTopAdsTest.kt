package com.tokopedia.analytics.topads

import android.app.Activity
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AppLogTopAdsTest {

    @Before
    fun setup() {
        AppLogTopAds.clearAdsPageData()
    }

    @Test
    fun `given home and SRP then assert last ads page data, channel name and channel param`() {

        val homePageAct = mockk<Activity>(relaxed = true)

        val srpAct = mockk<Activity>(relaxed = true)

        AppLogTopAds.putAdsPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.HOME)
        AppLogTopAds.currentPageName = PageName.HOME
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT

        assertLastAdsPageData(PageName.HOME)
        assertChannelAdsPageData(AdsLogConst.Channel.PRODUCT_SEARCH)
        assertChannelNameParam(AdsLogConst.Channel.PRODUCT_SEARCH)
        assertEnterFrom(AdsLogConst.EnterFrom.MALL)
    }

    @Test
    fun `given home, srp, pdp and srp then assert last ads page data is PDP, channel name and channel param`() {

        val homePageAct = mockk<Activity>(relaxed = true)
        val srpAct = mockk<Activity>(relaxed = true)
        val pdpAct = mockk<Activity>(relaxed = true)

        AppLogTopAds.putAdsPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.HOME)
        AppLogTopAds.currentPageName = PageName.HOME
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT
        AppLogTopAds.putAdsPageData(pdpAct, AppLogParam.PAGE_NAME, PageName.PDP)
        AppLogTopAds.currentPageName = PageName.PDP
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT

        assertLastAdsPageData(PageName.PDP)
        assertChannelAdsPageData(AdsLogConst.Channel.PDP_SEARCH)
        assertChannelNameParam(AdsLogConst.Channel.PDP_SEARCH)
        assertEnterFrom(AdsLogConst.EnterFrom.OTHER)
    }

    @Test
    fun `given home, official store, srp page then assert last ads page data, channel name and channel param`() {

        val homePageAct = mockk<Activity>(relaxed = true)
        val srpAct = mockk<Activity>(relaxed = true)

        AppLogTopAds.putAdsPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.HOME)
        AppLogTopAds.currentPageName = PageName.HOME
        AppLogTopAds.updateAdsFragmentPageData(homePageAct, AppLogParam.PAGE_NAME, PageName.OFFICIAL_STORE)
        AppLogTopAds.currentPageName = PageName.OFFICIAL_STORE
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT

        assertLastAdsPageData(PageName.OFFICIAL_STORE)
        assertChannelAdsPageData(AdsLogConst.Channel.PRODUCT_SEARCH)
        assertChannelNameParam(AdsLogConst.Channel.PRODUCT_SEARCH)
        assertEnterFrom(AdsLogConst.EnterFrom.OTHER)
    }

    @Test
    fun `given find page, srp page then assert last ads page data, channel name and channel param`() {

        val findPageAct = mockk<Activity>(relaxed = true)
        val srpAct = mockk<Activity>(relaxed = true)

        AppLogTopAds.putAdsPageData(findPageAct, AppLogParam.PAGE_NAME, PageName.FIND_PAGE)
        AppLogTopAds.currentPageName = PageName.FIND_PAGE
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT

        assertLastAdsPageData(PageName.FIND_PAGE)
        assertChannelAdsPageData(AdsLogConst.Channel.FIND_SEARCH)
        assertChannelNameParam(AppLogTopAds.EXTERNAL_SEARCH)
        assertEnterFrom(AdsLogConst.EnterFrom.OTHER)
    }

    @Test
    fun `given find page, srp page, and srp page then assert last ads page data, channel name and channel param`() {

        val findPageAct = mockk<Activity>(relaxed = true)
        val srpAct = mockk<Activity>(relaxed = true)

        AppLogTopAds.putAdsPageData(findPageAct, AppLogParam.PAGE_NAME, PageName.FIND_PAGE)
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)

        assertLastAdsPageData(PageName.SEARCH_RESULT)
        assertChannelAdsPageData(AdsLogConst.Channel.FIND_SEARCH)
        assertChannelNameParam(AdsLogConst.Channel.FIND_SEARCH)
        assertEnterFrom(AdsLogConst.EnterFrom.OTHER)
    }

    @Test
    fun `given find page, srp page, pdp page then back to srp page  then assert last ads page data, channel name and channel param`() {

        val findPageAct = mockk<Activity>(relaxed = true)
        val srpAct = mockk<Activity>(relaxed = true)
        val pdpAct = mockk<Activity>(relaxed = true)

        AppLogTopAds.putAdsPageData(findPageAct, AppLogParam.PAGE_NAME, PageName.FIND_PAGE)
        AppLogTopAds.currentPageName = PageName.FIND_PAGE
        AppLogTopAds.putAdsPageData(srpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT
        AppLogTopAds.putAdsPageData(pdpAct, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT
        AppLogTopAds.removeLastAdsPageData(pdpAct)

        assertLastAdsPageData(PageName.FIND_PAGE)
        assertChannelAdsPageData(AdsLogConst.Channel.FIND_SEARCH)
        assertChannelNameParam(AppLogTopAds.EXTERNAL_SEARCH)
        assertEnterFrom(AdsLogConst.EnterFrom.OTHER)
    }

    private fun assertLastAdsPageData(expectedPageName: String) {
        val lastPageName = AppLogTopAds.getLastAdsDataBeforeCurrent(AppLogParam.PAGE_NAME)
        assertEquals(expectedPageName, lastPageName)
    }

    private fun assertChannelAdsPageData(channelName: String) {
        val expectedChannelName = AppLogTopAds.getChannel()
        assertEquals(channelName, expectedChannelName)
    }

    private fun assertChannelNameParam(expectedChannelName: String) {
        val channelNameParam = AppLogTopAds.getChannelNameParam()
        assertEquals(channelNameParam, expectedChannelName)
    }

    private fun assertEnterFrom(expectedEnterFrom: String) {
        val lastPageName = AppLogTopAds.getEnterFrom()
        assertEquals(expectedEnterFrom, lastPageName)
    }
}
