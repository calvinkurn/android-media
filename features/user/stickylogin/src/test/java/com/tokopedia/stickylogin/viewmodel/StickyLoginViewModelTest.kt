package com.tokopedia.stickylogin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.stickylogin.view.viewModel.StickyLoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import androidx.lifecycle.Observer
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StickyLoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val dispatcher = TestCoroutineDispatcher()

    private val stickyLoginUseCase = mockk<StickyLoginUseCase>(relaxed = true)
    private var observer = mockk<Observer<Result<StickyLoginTickerDataModel>>>(relaxed = true)

    lateinit var viewModel: StickyLoginViewModel

    private val mockSuccessResponse = StickyLoginTickerDataModel.TickerResponse(StickyLoginTickerDataModel(arrayListOf(
            StickyLoginTickerDataModel.TickerDetailDataModel(layout = StickyLoginConstant.LAYOUT_FLOATING)
    )))

    private val mockThrowable = Throwable("Opss!")

    @Before
    fun setup() {
        viewModel = StickyLoginViewModel(stickyLoginUseCase, dispatcher)
        viewModel.stickyContent.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.onCleared()
        viewModel.stickyContent.removeObserver(observer)
    }

    @Test
    fun `get content for - success`() {
        val page = StickyLoginConstant.Page.HOME
        stickyLoginUseCase.setParam(viewModel.generateParams(page))

        every { stickyLoginUseCase.execute(any(), any()) } answers {
            firstArg<(StickyLoginTickerDataModel.TickerResponse) -> Unit>().invoke(mockSuccessResponse)
        }

        viewModel.getStickyContent(page)

        verify { observer.onChanged(Success(mockSuccessResponse.response)) }

        assertEquals((viewModel.stickyContent.value as Success).data.tickerDataModels[0].layout, StickyLoginConstant.LAYOUT_FLOATING)
    }

    @Test
    fun `get content - fail`() {
        val page = StickyLoginConstant.Page.HOME
        stickyLoginUseCase.setParam(viewModel.generateParams(page))

        every { stickyLoginUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getStickyContent(page)

        verify { observer.onChanged(Fail(mockThrowable)) }

        assertEquals((viewModel.stickyContent.value as Fail).throwable, mockThrowable)
    }
}