package com.tokopedia.stickylogin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.view.viewModel.StickyLoginViewModel
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StickyLoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private val stickyLoginUseCase = mockk<StickyLoginUseCase>(relaxed = true)
    private var observer = mockk<Observer<Result<StickyLoginTickerDataModel>>>(relaxed = true)

    lateinit var viewModel: StickyLoginViewModel

    private val mockResponse = StickyLoginTickerDataModel.TickerResponse(StickyLoginTickerDataModel(arrayListOf(
            StickyLoginTickerDataModel.TickerDetailDataModel(layout = StickyLoginConstant.LAYOUT_FLOATING)
    )))

    private val mockThrowable = Throwable("Opps!")

    @Before
    fun setup() {
        viewModel = StickyLoginViewModel(stickyLoginUseCase, dispatcherProviderTest)
        viewModel.stickyContent.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.stickyContent.removeObserver(observer)
    }

    @Test
    fun `get content`() {
        val page = StickyLoginConstant.Page.HOME

        coEvery { stickyLoginUseCase(any()) } returns mockResponse

        viewModel.getStickyContent(page)

        coVerify { observer.onChanged(Success(mockResponse.response)) }

        assertEquals((viewModel.stickyContent.value as Success).data.tickerDataModels[0].layout, StickyLoginConstant.LAYOUT_FLOATING)
    }

    @Test
    fun `get content - fail`() {
        val page = StickyLoginConstant.Page.HOME

        coEvery { stickyLoginUseCase(any()) }.throws(mockThrowable)

        viewModel.getStickyContent(page)

        coVerify { observer.onChanged(Fail(mockThrowable)) }
    }
}