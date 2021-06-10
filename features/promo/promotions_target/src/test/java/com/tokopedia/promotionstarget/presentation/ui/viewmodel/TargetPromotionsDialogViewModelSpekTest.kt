package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import com.tokopedia.promotionstarget.InstantTaskExecutorRuleSpek
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.spekframework.spek2.Spek

@ExperimentalCoroutinesApi
class TargetPromotionsDialogViewModelSpekTest : Spek({

    lateinit var autoApplyUseCase: AutoApplyUseCase
    lateinit var viewModel: TargetPromotionsDialogViewModel
    val dispatcher = TestCoroutineDispatcher()
    InstantTaskExecutorRuleSpek(this)

    beforeEachTest {
        autoApplyUseCase = mockk()
        viewModel = TargetPromotionsDialogViewModel(dispatcher, dispatcher, autoApplyUseCase, mockk(), mockk(), mockk())
    }

    group("test function invocation") {

        test("testSuccessLiveData") {
            val code = "123"
            coEvery { autoApplyUseCase.getResponse(any()) } returns AutoApplyResponse()
            coEvery { autoApplyUseCase.getQueryParams(any()) } returns HashMap()
            viewModel.autoApplyLiveData.observeForever { }
            viewModel.autoApply(code)
            assertEquals(viewModel.autoApplyLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
        }

        test("testErrorLiveData") {
            val code = "123"
            coEvery { autoApplyUseCase.getResponse(any()) } throws Exception()
            coEvery { autoApplyUseCase.getQueryParams(any()) } returns HashMap()
            viewModel.autoApplyLiveData.observeForever { }
            viewModel.autoApply(code)
            assertEquals(viewModel.autoApplyLiveData.value?.status, LiveDataResult.STATUS.ERROR)
        }
    }

    afterGroup {
        dispatcher.cleanupTestCoroutines()
    }
})