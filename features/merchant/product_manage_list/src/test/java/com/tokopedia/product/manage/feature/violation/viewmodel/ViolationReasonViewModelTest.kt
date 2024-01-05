package com.tokopedia.product.manage.feature.violation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.violation.domain.usecase.ViolationReasonUseCase
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import com.tokopedia.product.manage.feature.violation.view.viewmodel.ViolationReasonViewModel
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

class ViolationReasonViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var violationReasonUseCase: ViolationReasonUseCase

    private lateinit var viewModel: ViolationReasonViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ViolationReasonViewModel(
                violationReasonUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when getViolationReason success, should set live data success`() {
        val successResponse = ViolationReasonUiModel(
                "", "", "", "", listOf(), "", ""
        )
        coEvery {
            violationReasonUseCase.execute(any())
        } returns successResponse

        viewModel.getViolationReason("")

        coVerify { violationReasonUseCase.execute(any()) }
        assert(viewModel.violationReasonUiModelLiveData.value == Success(successResponse))
    }

    @Test
    fun `when getViolationReason error, should set live data fail`() {
        val throwable = NullPointerException()
        coEvery {
            violationReasonUseCase.execute(any())
        } throws throwable

        viewModel.getViolationReason("")

        coVerify { violationReasonUseCase.execute(any()) }
        assert(viewModel.violationReasonUiModelLiveData.value is Fail)
    }

}
