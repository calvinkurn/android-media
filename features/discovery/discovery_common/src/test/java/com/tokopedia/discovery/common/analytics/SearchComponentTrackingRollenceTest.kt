package com.tokopedia.discovery.common.analytics

import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.IMPRESSION_AND_CLICK
import com.tokopedia.iris.Iris
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.track.interfaces.Analytics
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class SearchComponentTrackingRollenceTest {

    private val analytics = mockk<Analytics>(relaxed = true)
    private val iris = mockk<Iris>(relaxed = true)
    private val remoteConfig = mockk<RemoteConfig>()

    private val component = "00.00.00.00_dummy_component"
    private val dummyExperimentName = "keyword_track_init"

    private fun searchComponentTracking(): SearchComponentTracking =
        SearchComponentTrackingImpl(
            trackingOption = IMPRESSION_AND_CLICK,
            keyword = "",
            valueId = "",
            valueName = "",
            campaignCode = "",
            componentId = component,
            applink = "",
            dimension90 = "",
        )

    private val dataLayerList = mutableListOf<Map<String, Any>>()

    @Before
    fun setUp() {
        every { analytics.sendGeneralEvent(capture(dataLayerList)) } just runs
        every { iris.saveEvent(capture(dataLayerList)) } just runs
    }

    @Test
    fun `use tracking component impression with experiment enabled`() {
        enableSearchComponentTrackingExperiment()

        val searchComponentTrackingList =
            listOf(searchComponentTracking(), searchComponentTracking())

        SearchComponentTrackingRollence.impress(
            remoteConfig,
            iris,
            searchComponentTrackingList,
            dummyExperimentName
        ) { }

        assertThat(dataLayerList.size, `is`(searchComponentTrackingList.size))
    }

    private fun enableSearchComponentTrackingExperiment() {
        every { remoteConfig.getString(dummyExperimentName) } returns dummyExperimentName
    }

    @Test
    fun `use tracking component impression with experiment disabled`() {
        disableSearchComponentTrackingExperiment()
        val fallback = mockk<() -> Unit>(relaxed = true)

        SearchComponentTrackingRollence.impress(
            remoteConfig,
            iris,
            listOf(searchComponentTracking()),
            dummyExperimentName,
            fallback,
        )

        verifyNotSendAnalytics()
        verify { fallback.invoke() }
    }

    private fun disableSearchComponentTrackingExperiment() {
        every { remoteConfig.getString(dummyExperimentName) } returns ""
    }

    private fun verifyNotSendAnalytics() {
        assertThat(dataLayerList.size, `is`(0))
    }

    @Test
    fun `use tracking component click with experiment enabled`() {
        enableSearchComponentTrackingExperiment()

        SearchComponentTrackingRollence.click(
            remoteConfig,
            analytics,
            searchComponentTracking(),
            dummyExperimentName
        ) { }

        assertThat(dataLayerList.size, `is`(1))
    }

    @Test
    fun `use tracking component click with experiment disabled`() {
        disableSearchComponentTrackingExperiment()
        val fallback = mockk<() -> Unit>(relaxed = true)

        SearchComponentTrackingRollence.click(
            remoteConfig,
            analytics,
            searchComponentTracking(),
            dummyExperimentName,
            fallback,
        )

        verifyNotSendAnalytics()
        verify { fallback.invoke() }
    }

    @Test
    fun `use tracking component click other action with experiment enabled`() {
        enableSearchComponentTrackingExperiment()

        SearchComponentTrackingRollence.clickOtherAction(
            remoteConfig,
            analytics,
            searchComponentTracking(),
            dummyExperimentName
        ) { }

        assertThat(dataLayerList.size, `is`(1))
    }

    @Test
    fun `use tracking component click other action with experiment disabled`() {
        disableSearchComponentTrackingExperiment()
        val fallback = mockk<() -> Unit>(relaxed = true)

        SearchComponentTrackingRollence.clickOtherAction(
            remoteConfig,
            analytics,
            searchComponentTracking(),
            dummyExperimentName,
            fallback,
        )

        verifyNotSendAnalytics()
        verify { fallback.invoke() }
    }
}