package com.tokopedia.pdp.fintech

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.domain.usecase.FintechWidgetUseCase
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class PdpFintectWidgetViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val fintechWidgetUseCase = mockk<FintechWidgetUseCase>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: FintechWidgetViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = FintechWidgetViewModel(
            dispatcher,
            fintechWidgetUseCase
        )
    }

    @Test
    fun successFintechWidget() {
        val widgetDetail = mockk<WidgetDetail>(relaxed = true)
        coEvery {
            fintechWidgetUseCase.getWidgetData(any(), any(), "", hashMapOf())
        } coAnswers {
            firstArg<(WidgetDetail) -> Unit>().invoke(widgetDetail)
        }
        viewModel.getWidgetData("", hashMapOf())
        Assert.assertEquals(
            (viewModel.widgetDetailLiveData.value as Success).data,
            widgetDetail
        )
    }

    @Test
    fun failFintechWidget() {

        coEvery {
            fintechWidgetUseCase.getWidgetData(any(), any(), "", hashMapOf())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getWidgetData("", hashMapOf())
        Assert.assertEquals(
            (viewModel.widgetDetailLiveData.value as Fail).throwable,
            mockThrowable
        )
    }


}