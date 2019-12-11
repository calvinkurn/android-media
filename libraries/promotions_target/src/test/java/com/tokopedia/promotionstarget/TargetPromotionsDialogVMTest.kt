package com.tokopedia.promotionstarget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.TargetPromotionsDialogVM
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TargetPromotionsDialogVMTest {

    @MockK
    lateinit var autoApplyUseCase: AutoApplyUseCase
    lateinit var viewModel: TargetPromotionsDialogVM
    val dispatcher = TestCoroutineDispatcher()
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TargetPromotionsDialogVM(dispatcher, dispatcher, autoApplyUseCase)
    }

    @Test
    fun `testSuccessLiveData`() {
        val code = "123"
        coEvery { autoApplyUseCase.getResponse(any()) } returns AutoApplyResponse()
        coEvery { autoApplyUseCase.getQueryParams(any()) } returns HashMap()
        viewModel.autoApplyLiveData.observeForever { }
        viewModel.autoApply(code)
        assertEquals(viewModel.autoApplyLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun `testErrorLiveData`() {
        val code = "123"
        coEvery { autoApplyUseCase.getResponse(any()) } throws Exception()
        coEvery { autoApplyUseCase.getQueryParams(any()) } returns HashMap()
        viewModel.autoApplyLiveData.observeForever { }
        viewModel.autoApply(code)
        assertEquals(viewModel.autoApplyLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }


}