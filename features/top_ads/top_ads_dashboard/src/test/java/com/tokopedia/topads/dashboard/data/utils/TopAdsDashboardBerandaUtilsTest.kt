package com.tokopedia.topads.dashboard.data.utils

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import com.tokopedia.topads.dashboard.data.model.beranda.ImageModel
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.getSummaryAdTypes
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.mapToSummary
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsDashboardBerandaUtilsTest {

    @Test
    fun `mapImageModel less than equal 5 items should not have overlapping text`() {
        val actual = TopAdsDashboardBerandaUtils.mapImageModel(List(5) { mockk(relaxed = true) })
        assert(actual[actual.size - 1].overLappingText.isNullOrEmpty())
    }

    @Test
    fun `mapImageModel greater than 5 items should not have overlapping text`() {
        val expected = "+3"
        val actual = TopAdsDashboardBerandaUtils.mapImageModel(List(7) { ImageModel("") })

        assertEquals(actual[actual.size - 1].overLappingText, expected)
    }

    @Test
    fun `mapToSummary item count check`() {
        val expected : TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Summary = mockk(relaxed = true)
        val context = mockk<Context>()

        every { context.resources.getString(any()) } returns ""
        every { ContextCompat.getColor(context, any()) } returns 0

        val obj = expected.mapToSummary(context)
        assertEquals(obj.size, 6)
    }

    @Test
    fun `getSummaryAdTypes items count check`() {
        val resources = mockk<Resources>()

        every { resources.getString(any()) } returns ""

        val actual = resources.getSummaryAdTypes()
        assert(actual.size == 3)
    }
}