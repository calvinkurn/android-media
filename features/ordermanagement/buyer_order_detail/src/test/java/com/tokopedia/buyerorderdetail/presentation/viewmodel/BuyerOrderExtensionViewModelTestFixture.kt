package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.usecases.GetOrderExtensionRespondInfoUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.InsertOrderExtensionRespondUseCase
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BuyerOrderExtensionViewModelTestFixture {

    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var insertOrderExtensionRespondUseCase: Lazy<InsertOrderExtensionRespondUseCase>

    @RelaxedMockK
    lateinit var getOrderExtensionRespondInfoUseCase: Lazy<GetOrderExtensionRespondInfoUseCase>

    lateinit var viewModel: BuyerOrderDetailExtensionViewModel

    protected val orderId = "123456"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = BuyerOrderDetailExtensionViewModel(
            coroutineDispatchers,
            insertOrderExtensionRespondUseCase,
            getOrderExtensionRespondInfoUseCase
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetOrderExtensionRespondInfoUseCase_thenReturn(
        orderExtensionRespondInfoUiModel: OrderExtensionRespondInfoUiModel
    ) {
        coEvery {
            getOrderExtensionRespondInfoUseCase.get().executeOnBackground()
        } returns orderExtensionRespondInfoUiModel
    }

    protected fun onGetOrderExtensionRespondInfoUseCase_thenReturn(exception: Throwable) {
        coEvery { getOrderExtensionRespondInfoUseCase.get().executeOnBackground() } throws exception
    }

    protected fun verifyGetOrderExtensionRespondInfoUseCaseCalled() {
        coVerify { getOrderExtensionRespondInfoUseCase.get().executeOnBackground() }
    }

    protected fun onInsertOrderExtensionRespondUseCase_thenReturn(
        orderExtensionRespondUiModel: OrderExtensionRespondUiModel
    ) {
        coEvery {
            insertOrderExtensionRespondUseCase.get().executeOnBackground()
        } returns orderExtensionRespondUiModel
    }

    protected fun verifyInsertOrderExtensionRespondUseCaseCalled() {
        coVerify { insertOrderExtensionRespondUseCase.get().executeOnBackground() }
    }

    protected fun onInsertOrderExtensionRespondUseCaseError_thenReturn(exception: Throwable) {
        coEvery { insertOrderExtensionRespondUseCase.get().executeOnBackground() } throws exception
    }

}