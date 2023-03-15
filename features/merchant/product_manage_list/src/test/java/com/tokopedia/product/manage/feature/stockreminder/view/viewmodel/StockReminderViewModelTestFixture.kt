package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import com.tokopedia.shop.common.domain.interactor.GetMaxStockThresholdUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class StockReminderViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var stockReminderDataUseCase: StockReminderDataUseCase

    @RelaxedMockK
    lateinit var getMaxStockThresholdUseCase: GetMaxStockThresholdUseCase

    protected lateinit var viewModel: StockReminderViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = StockReminderViewModel(
            stockReminderDataUseCase,
            getMaxStockThresholdUseCase,
            CoroutineTestDispatchersProvider
        )
    }
}
