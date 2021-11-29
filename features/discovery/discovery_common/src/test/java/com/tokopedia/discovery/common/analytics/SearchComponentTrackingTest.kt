package com.tokopedia.discovery.common.analytics

import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.CLICK
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.CLICK_OTHER_ACTION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.IMPRESSION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.BUSINESSUNIT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.CAMPAIGNCODE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.CURRENTSITE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Category.SEARCH_COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Event.CLICKSEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Event.VIEWSEARCHIRIS
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.KEYWORD
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.KEYWORD_ID_NAME
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.NONE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.CLICK_ONLY
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.IMPRESSION_AND_CLICK
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.IMPRESSION_ONLY
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.NO_TRACKING
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.PAGEDESTINATION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.PAGESOURCE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.SEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.TOKOPEDIAMARKETPLACE
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.interfaces.Analytics
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class SearchComponentTrackingTest {

    private val analytics = mockk<Analytics>(relaxed = true)
    private val iris = mockk<Iris>(relaxed = true)

    private val keyword = "samsung"
    private val campaignCode = "dummy_campaign_code"
    private val component = "00.00.00.00_dummy_component"
    private val applink = "tokopedia://dummyapplink"
    private val pageSource = "dummy_page_title.dummy_navsource.dummy_value.dummy_srp_page_id"
    private val valueId = "dummy_item_id"
    private val valueName = "dummy_item_value"

    private fun searchComponentTracking(
        trackingOption: Int = IMPRESSION_AND_CLICK,
    ): SearchComponentTracking =
        SearchComponentTrackingImpl(
            trackingOption = trackingOption,
            keyword = keyword,
            valueId = valueId,
            valueName = valueName,
            campaignCode = campaignCode,
            componentId = component,
            applink = applink,
            dimension90 = pageSource,
        )

    private val dataLayerSlot = slot<Map<String, Any>>()
    private val dataLayer by lazy { dataLayerSlot.captured }

    @Before
    fun setUp() {
        every { analytics.sendGeneralEvent(capture(dataLayerSlot)) } just runs
        every { iris.saveEvent(capture(dataLayerSlot)) } just runs
    }

    @Test
    fun `track component impression will send impression data layer`() {
        searchComponentTracking().impress(iris)

        assertThat(dataLayer, `is`(expectedImpressionDataLayer()))
    }

    private fun expectedImpressionDataLayer() = mapOf(
        EVENT to VIEWSEARCHIRIS,
        EVENT_ACTION to IMPRESSION,
        EVENT_LABEL to String.format(KEYWORD_ID_NAME, keyword, valueId, valueName),
        *commonDataLayer(),
    )

    private fun commonDataLayer(): Array<Pair<String, Any>> =
        arrayOf(
            EVENT_CATEGORY to SEARCH_COMPONENT,
            BUSINESSUNIT to SEARCH,
            CURRENTSITE to TOKOPEDIAMARKETPLACE,
            CAMPAIGNCODE to campaignCode,
            COMPONENT to component,
            PAGEDESTINATION to applink,
            PAGESOURCE to pageSource,
        )

    @Test
    fun `track component click will send click data layer`() {
        searchComponentTracking().click(analytics)

        assertThat(dataLayer, `is`(expectedClickDataLayer()))
    }

    private fun expectedClickDataLayer() = mapOf(
        EVENT to CLICKSEARCH,
        EVENT_ACTION to CLICK,
        EVENT_LABEL to String.format(KEYWORD_ID_NAME, keyword, valueId, valueName),
        *commonDataLayer(),
    )

    @Test
    fun `track component click other action will send click other action data layer`() {
        searchComponentTracking().clickOtherAction(analytics)

        assertThat(dataLayer, `is`(expectedClickOtherActionDataLayer()))
    }

    private fun expectedClickOtherActionDataLayer() = mapOf(
        EVENT to CLICKSEARCH,
        EVENT_ACTION to CLICK_OTHER_ACTION,
        EVENT_LABEL to String.format(KEYWORD, keyword),
        *commonDataLayer(),
    )

    @Test
    fun `track component impression will not send if tracking option not enable impression`() {
        searchComponentTracking(trackingOption = NO_TRACKING).impress(iris)
        searchComponentTracking(trackingOption = CLICK_ONLY).impress(iris)
        val unrecognizedTrackingOption = -1221
        searchComponentTracking(trackingOption = unrecognizedTrackingOption).impress(iris)

        verifyNotSendIris()
    }

    private fun verifyNotSendIris() {
        verify(exactly = 0) { iris.saveEvent(any() as Map<String, Any>) }
    }

    @Test
    fun `track component click and click other option will not send if tracking option not enable click`() {
        searchComponentTracking(trackingOption = NO_TRACKING).allClicks()
        searchComponentTracking(trackingOption = IMPRESSION_ONLY).allClicks()
        val unrecognizedTrackingOption = -1221
        searchComponentTracking(trackingOption = unrecognizedTrackingOption).allClicks()

        verifyNotSendAnalytics()
    }

    private fun SearchComponentTracking.allClicks() {
        click(analytics)
        clickOtherAction(analytics)
    }

    private fun verifyNotSendAnalytics() {
        verify(exactly = 0) { analytics.sendGeneralEvent(any())}
    }

    @Test
    fun `empty value in tracking will be replaced with none`() {
        val emptyTracking = SearchComponentTrackingImpl(
            trackingOption = IMPRESSION_AND_CLICK,
            keyword = "",
            valueId = "",
            valueName = "",
            campaignCode = "",
            componentId = "",
            applink = "",
            dimension90 = "",
        )

        emptyTracking.impress(iris)

        assertThat(dataLayer, `is`(
            mapOf(
                EVENT to VIEWSEARCHIRIS,
                EVENT_ACTION to IMPRESSION,
                EVENT_LABEL to String.format(KEYWORD_ID_NAME, NONE, NONE, NONE),
                EVENT_CATEGORY to SEARCH_COMPONENT,
                BUSINESSUNIT to SEARCH,
                CURRENTSITE to TOKOPEDIAMARKETPLACE,
                CAMPAIGNCODE to NONE,
                COMPONENT to NONE,
                PAGEDESTINATION to NONE,
                PAGESOURCE to NONE,
            )
        ))
    }
}