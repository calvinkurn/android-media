package com.tokopedia.sellerorder.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.detail.domain.usecase.SomGetFeeTransparencyUseCase
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeUiModelWrapper
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailTransparencyFeeViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SomDetailTransparencyFeeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testRule = UnconfinedTestRule()

    @RelaxedMockK
    lateinit var transparencyFeeUseCase: SomGetFeeTransparencyUseCase

    lateinit var viewModel: SomDetailTransparencyFeeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SomDetailTransparencyFeeViewModel(
            dispatcher = CoroutineTestDispatchers,
            getSomTransparencyFeeTransparencyUseCase = transparencyFeeUseCase
        )
    }

    @Test
    fun `fetchTransparencyFee should execute transparencyFeeUseCase once`() {
        coEvery {
            transparencyFeeUseCase.execute(any())
        } returns mockk(relaxed = true)
        viewModel.fetchTransparencyFee("123")
        coVerify {
            transparencyFeeUseCase.execute(any())
        }
    }

    @Test
    fun `fetchTransparencyFee should set _transparencyFee to Success on success`() {
        coEvery {
            transparencyFeeUseCase.execute(any())
        } returns mockk(relaxed = true)
        viewModel.fetchTransparencyFee("123")
        Assert.assertTrue(viewModel.transparencyFee.value is Success<TransparencyFeeUiModelWrapper>)
    }

    @Test
    fun `fetchTransparencyFee should set _transparencyFee to Fail on error`() {
        coEvery {
            transparencyFeeUseCase.execute(any())
        } throws Throwable()
        viewModel.fetchTransparencyFee("123")
        Assert.assertTrue(viewModel.transparencyFee.value is Fail)
    }
}
