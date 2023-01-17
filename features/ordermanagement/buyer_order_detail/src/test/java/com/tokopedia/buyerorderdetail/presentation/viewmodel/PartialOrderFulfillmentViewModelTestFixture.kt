package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.usecases.ApprovePartialOrderFulfillmentUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetPartialOrderFulfillmentInfoUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.RejectPartialOrderFulfillmentUseCase
import com.tokopedia.buyerorderdetail.presentation.model.ApprovePartialOrderFulfillmentUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.RejectPartialOrderFulfillmentUiModel
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

abstract class PartialOrderFulfillmentViewModelTestFixture {

    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPartialOrderFulfillmentInfoUseCase: Lazy<GetPartialOrderFulfillmentInfoUseCase>

    @RelaxedMockK
    lateinit var approvePartialOrderFulfillmentUseCase: Lazy<ApprovePartialOrderFulfillmentUseCase>

    @RelaxedMockK
    lateinit var rejectPartialOrderFulfillmentUseCase: Lazy<RejectPartialOrderFulfillmentUseCase>

    lateinit var viewModel: PartialOrderFulfillmentViewModel

    protected val orderId = "123456"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = PartialOrderFulfillmentViewModel(
            coroutineDispatchers,
            getPartialOrderFulfillmentInfoUseCase,
            approvePartialOrderFulfillmentUseCase,
            rejectPartialOrderFulfillmentUseCase
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetPartialOrderFulfillmentUseCase_thenReturn(
        partialOrderFulfillmentWrapperUiModel: PartialOrderFulfillmentWrapperUiModel,
        orderId: Long
    ) {
        coEvery {
            getPartialOrderFulfillmentInfoUseCase.get().execute(orderId)
        } returns partialOrderFulfillmentWrapperUiModel
    }

    protected fun onGetPartialOrderFulfillmentUseCase_thenReturnError(
        orderId: Long,
        exception: Throwable
    ) {
        coEvery { getPartialOrderFulfillmentInfoUseCase.get().execute(orderId) } throws exception
    }

    protected fun verifyGetOrderExtensionRespondInfoUseCaseCalled(
        orderId: Long
    ) {
        coVerify { getPartialOrderFulfillmentInfoUseCase.get().execute(orderId) }
    }

    protected fun onApprovePartialOrderFulfillmentUseCase_thenReturn(
        approvePartialOrderFulfillmentUiModel: ApprovePartialOrderFulfillmentUiModel,
        orderId: Long
    ) {
        coEvery {
            approvePartialOrderFulfillmentUseCase.get().execute(orderId)
        } returns approvePartialOrderFulfillmentUiModel
    }

    protected fun verifyApprovePartialOrderFulfillmentUseCaseCalled(
        orderId: Long
    ) {
        coVerify { approvePartialOrderFulfillmentUseCase.get().execute(orderId) }
    }

    protected fun onApprovePartialOrderFulfillmentUseCase_thenReturnError(
        exception: Throwable,
        orderId: Long
    ) {
        coEvery { approvePartialOrderFulfillmentUseCase.get().execute(orderId) } throws exception
    }

    protected fun onRejectPartialOrderFulfillmentUseCase_thenReturn(
        rejectPartialOrderFulfillmentUiModel: RejectPartialOrderFulfillmentUiModel,
        orderId: Long
    ) {
        coEvery {
            rejectPartialOrderFulfillmentUseCase.get().execute(orderId)
        } returns rejectPartialOrderFulfillmentUiModel
    }

    protected fun verifyRejectPartialOrderFulfillmentUseCaseCalled(
        orderId: Long
    ) {
        coVerify { rejectPartialOrderFulfillmentUseCase.get().execute(orderId) }
    }

    protected fun onRejectPartialOrderFulfillmentUseCase_thenReturnError(
        exception: Throwable,
        orderId: Long
    ) {
        coEvery { rejectPartialOrderFulfillmentUseCase.get().execute(orderId) } throws exception
    }
}
