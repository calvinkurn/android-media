package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.usecases.GetBomGroupedOrderUseCase
import com.tokopedia.buyerorderdetail.presentation.model.OwocGroupedOrderWrapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class OwocViewModelTestFixture {

    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getBomGroupedOrderUseCase: Lazy<GetBomGroupedOrderUseCase>

    lateinit var viewModel: OwocViewModel

    protected val orderId = "123456"
    protected val txId = "456789"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = OwocViewModel(
            coroutineDispatchers,
            getBomGroupedOrderUseCase
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetBomGroupedOrderUseCase_thenReturn(
        owocGroupedOrderWrapper: OwocGroupedOrderWrapper,
        orderId: String,
        txId: String
    ) {
        coEvery {
            getBomGroupedOrderUseCase.get().execute(txId, orderId)
        } returns owocGroupedOrderWrapper
    }

    protected fun onGetBomGroupedOrderUseCase_thenReturnError(
        orderId: String,
        txId: String,
        exception: Throwable
    ) {
        coEvery { getBomGroupedOrderUseCase.get().execute(txId, orderId) } throws exception
    }

    protected fun verifyGetBomGroupedOrderUseCaseUseCaseCalled(
        orderId: String,
        txId: String
    ) {
        coVerify { getBomGroupedOrderUseCase.get().execute(txId, orderId) }
    }
}
