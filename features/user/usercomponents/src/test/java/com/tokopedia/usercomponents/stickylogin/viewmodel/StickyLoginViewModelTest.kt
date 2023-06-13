package com.tokopedia.usercomponents.stickylogin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.usercomponents.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.usercomponents.stickylogin.view.viewModel.StickyLoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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

    private val mockResponse = StickyLoginTickerDataModel.TickerResponse(
        StickyLoginTickerDataModel(
            arrayListOf(
                StickyLoginTickerDataModel.TickerDetailDataModel(
                    message = "",
                    layout = StickyLoginConstant.LAYOUT_FLOATING
                )
            )
        )
    )

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

        assert((viewModel.stickyContent.value as Success).data.tickerDataModels.any {
            it.layout == StickyLoginConstant.LAYOUT_FLOATING
        })
    }

    @Test
    fun `get content - not sticky`() {
        val page = StickyLoginConstant.Page.HOME

        coEvery { stickyLoginUseCase(any()) } returns mockResponse.apply {
            response.tickerDataModels.forEach {
                it.layout = StickyLoginConstant.LAYOUT_DEFAULT
            }
        }

        viewModel.getStickyContent(page)

        assert(viewModel.stickyContent.value is Fail)
    }

    @Test
    fun `get content - fail`() {
        val page = StickyLoginConstant.Page.HOME

        coEvery { stickyLoginUseCase(any()) }.throws(mockThrowable)

        viewModel.getStickyContent(page)

        coVerify { observer.onChanged(Fail(mockThrowable)) }
    }
}