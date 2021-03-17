package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStatsResponseWrapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import org.junit.Test

class ReminderPerformanceViewModelTest : ReminderPerformanceViewModelTestFixture() {

    @Test
    fun `when fetch reminder stats success should update reminder stats value`() {
        val responseWrapper = ProductrevGetReminderStatsResponseWrapper()
        coEvery { productrevGetReminderStatsUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchReminderStats()
        viewModel.getReminderStats().verifyValueEquals(responseWrapper.productrevGetReminderStats)
    }

}