package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderStatsUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReminderPerformanceViewModelTestFixture {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var productrevGetReminderStatsUseCase: ProductrevGetReminderStatsUseCase

    protected lateinit var viewModel: ReminderPerformanceViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReminderPerformanceViewModel(
                CoroutineTestDispatchersProvider,
                productrevGetReminderStatsUseCase
        )
    }
}