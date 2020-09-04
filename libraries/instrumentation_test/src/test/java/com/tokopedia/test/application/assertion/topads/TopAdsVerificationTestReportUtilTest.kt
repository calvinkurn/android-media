package com.tokopedia.test.application.assertion.topads

import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.STATUS_PENDING
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.EVENT_CLICK
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.EVENT_IMPRESSION
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.REPORT_HEADER
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.generateTopAdsVerificatorReportCoverage
import org.junit.Test

private const val HOME_PAGE_MIX_TOP = "home_page_mix_top"
private const val HOME_PAGE_MIX_LEFT = "home_page_mix_left"
private const val SOURCE_CAROUSEL = "com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselProductCardViewHolder"
private const val SOURCE_HOME_RECOM_FRAGMENT = "com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment"

internal class TopAdsVerificationTestReportUtilTest {

    private fun topAdsMixTopImpression(status: String) = TopAdsLogDB(
            sourceName = SOURCE_CAROUSEL, eventType = EVENT_IMPRESSION, eventStatus = status, componentName = HOME_PAGE_MIX_TOP
    )

    private fun topAdsMixLeftImpression(status: String) = TopAdsLogDB(
            sourceName = SOURCE_CAROUSEL, eventType = EVENT_IMPRESSION, eventStatus = status, componentName = HOME_PAGE_MIX_LEFT
    )

    private fun topAdsMixTopClick(status: String) = TopAdsLogDB(
            sourceName = SOURCE_CAROUSEL, eventType = EVENT_CLICK, eventStatus = status, componentName = HOME_PAGE_MIX_TOP
    )

    private fun topAdsMixLeftClick(status: String) = TopAdsLogDB(
            sourceName = SOURCE_CAROUSEL, eventType = EVENT_CLICK, eventStatus = status, componentName = HOME_PAGE_MIX_LEFT
    )

    private fun topAdsCarouselImpression(status: String) = TopAdsLogDB(
            sourceName = SOURCE_CAROUSEL, eventType = EVENT_IMPRESSION, eventStatus = status
    )

    private fun topAdsCarouselClick(status: String) = TopAdsLogDB(
            sourceName = SOURCE_CAROUSEL, eventType = EVENT_CLICK, eventStatus = status
    )

    private fun topAdsHomeRecomImpression(status: String) = TopAdsLogDB(
            sourceName = SOURCE_HOME_RECOM_FRAGMENT, eventType = EVENT_IMPRESSION, eventStatus = status
    )

    private fun topAdsHomeRecomClick(status: String) = TopAdsLogDB(
            sourceName = SOURCE_HOME_RECOM_FRAGMENT, eventType = EVENT_CLICK, eventStatus = status
    )

    private fun assertReportCoverage(topAdsLogDBList: List<TopAdsLogDB>, vararg expectedReportString: String) {
        val actualReport = generateTopAdsVerificatorReportCoverage(topAdsLogDBList)
        val expectedReport = listOf(*expectedReportString)

        assert(actualReport == expectedReport) {
            "Actual report: $actualReport\nExpected report: $expectedReport"
        }
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with empty list`() {
        val result = generateTopAdsVerificatorReportCoverage(listOf())

        assert(result.isEmpty())
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with one impression record`() {
        val topAdsLogDB = topAdsMixTopImpression(STATUS_MATCH)

        assertReportCoverage(listOf(topAdsLogDB),
                REPORT_HEADER,
                "$HOME_PAGE_MIX_TOP,100.0%,-",
                ",100.0%,-"
        )
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with one click record`() {
        val topAdsLogDB = topAdsMixTopClick(STATUS_MATCH)

        assertReportCoverage(listOf(topAdsLogDB),
                REPORT_HEADER,
                "$HOME_PAGE_MIX_TOP,-,100.0%",
                ",-,100.0%"
        )
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with multiple impression record`() {
        val topAdsLogDBList = listOf(
                topAdsMixTopImpression(STATUS_MATCH),
                topAdsMixTopImpression(STATUS_PENDING)
        )

        assertReportCoverage(topAdsLogDBList,
                REPORT_HEADER,
                "$HOME_PAGE_MIX_TOP,50.0%,-",
                ",50.0%,-"
        )
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with multiple click record`() {
        val topAdsLogDBList = listOf(
                topAdsMixTopClick(STATUS_MATCH),
                topAdsMixTopClick(STATUS_PENDING)
        )

        assertReportCoverage(topAdsLogDBList,
                REPORT_HEADER,
                "$HOME_PAGE_MIX_TOP,-,50.0%",
                ",-,50.0%"
        )
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with multiple component name and multiple record`() {
        val topAdsLogDBList = listOf(
                topAdsMixTopImpression(STATUS_MATCH),
                topAdsMixTopImpression(STATUS_PENDING),
                topAdsMixLeftImpression(STATUS_MATCH),
                topAdsMixLeftImpression(STATUS_MATCH),
                topAdsMixTopClick(STATUS_MATCH),
                topAdsMixTopClick(STATUS_MATCH),
                topAdsMixLeftClick(STATUS_MATCH),
                topAdsMixLeftClick(STATUS_PENDING)
        )

        assertReportCoverage(topAdsLogDBList,
                REPORT_HEADER,
                "$HOME_PAGE_MIX_TOP,50.0%,100.0%",
                "$HOME_PAGE_MIX_LEFT,100.0%,50.0%",
                ",75.0%,75.0%"
        )
    }

    @Test
    fun `test generateTopAdsVerificatorReportCoverage with mix component name and source name`() {
        val topAdsLogDBList = listOf(
                topAdsMixTopImpression(STATUS_MATCH),
                topAdsCarouselImpression(STATUS_MATCH),
                topAdsCarouselImpression(STATUS_PENDING),
                topAdsHomeRecomImpression(STATUS_MATCH),
                topAdsHomeRecomImpression(STATUS_MATCH),
                topAdsCarouselClick(STATUS_MATCH),
                topAdsCarouselClick(STATUS_MATCH),
                topAdsHomeRecomClick(STATUS_MATCH),
                topAdsHomeRecomClick(STATUS_PENDING)
        )

        assertReportCoverage(topAdsLogDBList,
                REPORT_HEADER,
                "$HOME_PAGE_MIX_TOP,100.0%,-",
                "$SOURCE_CAROUSEL,50.0%,100.0%",
                "$SOURCE_HOME_RECOM_FRAGMENT,100.0%,50.0%",
                ",80.0%,75.0%"
        )
    }
}