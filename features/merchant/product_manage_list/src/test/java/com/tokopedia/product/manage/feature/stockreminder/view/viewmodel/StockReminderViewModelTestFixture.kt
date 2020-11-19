package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class StockReminderViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var stockReminderDataUseCase: StockReminderDataUseCase

    protected lateinit var viewModel: StockReminderViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = StockReminderViewModel(stockReminderDataUseCase, CoroutineTestDispatchersProvider)
    }
}