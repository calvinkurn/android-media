package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStatsResponseWrapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class ReminderPerformanceViewModelTest : ReminderPerformanceViewModelTestFixture() {

    @Test
    fun `when fetch reminder stats success should update reminder stats value`() {
        val responseWrapper = ProductrevGetReminderStatsResponseWrapper()
        coEvery { productrevGetReminderStatsUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchReminderStats()
        coVerify { productrevGetReminderStatsUseCase.executeOnBackground() }
        viewModel.getReminderStats().verifyValueEquals(Success(responseWrapper.productrevGetReminderStats))
    }

    @Test
    fun `when fetch reminder stats fail should update reminder stats value with error`() {
        val throwable = Throwable()
        coEvery { productrevGetReminderStatsUseCase.executeOnBackground() } throws throwable
        viewModel.fetchReminderStats()
        coVerify { productrevGetReminderStatsUseCase.executeOnBackground() }
        viewModel.getReminderStats().verifyValueEquals(Fail(throwable))
    }

}