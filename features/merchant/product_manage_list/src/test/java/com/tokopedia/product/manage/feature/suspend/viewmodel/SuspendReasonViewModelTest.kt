package com.tokopedia.product.manage.feature.suspend.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.suspend.domain.usecase.SuspendReasonUseCase
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import com.tokopedia.product.manage.feature.suspend.view.viewmodel.SuspendReasonViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SuspendReasonViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var suspendReasonUseCase: SuspendReasonUseCase

    private lateinit var viewModel: SuspendReasonViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SuspendReasonViewModel(
                suspendReasonUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when getSuspendReason success, should set live data success`() {
        val successResponse = SuspendReasonUiModel(
                "", "", "", "","", listOf(), "", "",""
        )
        coEvery {
            suspendReasonUseCase.execute(any())
        } returns successResponse

        viewModel.getSuspendReason("")

        coVerify { suspendReasonUseCase.execute(any()) }
        assert(viewModel.suspendReasonUiModelLiveData.value == Success(successResponse))
    }

    @Test
    fun `when getSuspendReason error, should set live data fail`() {
        val throwable = NullPointerException()
        coEvery {
            suspendReasonUseCase.execute(any())
        } throws throwable

        viewModel.getSuspendReason("")

        coVerify { suspendReasonUseCase.execute(any()) }
        assert(viewModel.suspendReasonUiModelLiveData.value is Fail)
    }

}
