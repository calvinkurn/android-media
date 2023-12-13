package com.tokopedia.targetedticker

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.targetedticker.domain.TargetedTickerMapper
import com.tokopedia.targetedticker.domain.TargetedTickerPage
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.targetedticker.ui.TargetedTickerViewModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TargetedTickerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val tickerUseCase: GetTargetedTickerUseCase = mockk(relaxed = true)

    private var observerTickerUseCase =
        mockk<Observer<Result<TickerModel>>>(relaxed = true)
    private val mockThrowable = mockk<Throwable>(relaxed = true)

    private val context = mockk<Context>(relaxed = true)
    private val sharedPrefs = mockk<SharedPreferences>(relaxed = true)

    private lateinit var targetedTickerViewModel: TargetedTickerViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        stubSharePrefs()
        targetedTickerViewModel = TargetedTickerViewModel(
            tickerUseCase,
            coroutineTestRule.dispatchers
        )

        targetedTickerViewModel.tickerState.observeForever(observerTickerUseCase)
    }

    @After
    fun tearDown() {
        TokopediaUrl.deleteInstance()
    }

    private fun stubSharePrefs() {
        coEvery { context.getSharedPreferences(any(), any()) } returns sharedPrefs
    }

    @Test
    fun `WHEN setupTicker THEN return sorted TickerModel`() {
        val response = TickerDataProvider.provideDummy()
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val tickerState = targetedTickerViewModel.tickerState.value
        assertTrue(tickerState is Success)
        assertTrue((tickerState as Success).data.item.first().priority == 1L)
        assertTrue(tickerState.data.item[1].priority == 2L)
        assertTrue(tickerState.data.item[2].priority == 3L)
    }

    @Test
    fun `WHEN setupTicker failed THEN return fail`() {
        coEvery { tickerUseCase.invoke(any()) } throws mockThrowable

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        assertTrue(targetedTickerViewModel.tickerState.value is Fail)
    }

    @Test
    fun `WHEN setupTicker failed and empty first ticker content THEN return fail`() {
        coEvery { tickerUseCase.invoke(any()) } throws mockThrowable

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        assertTrue(targetedTickerViewModel.tickerState.value is Fail)
    }

    @Test
    fun `WHEN setupTicker with ticker type info THEN return TickerModel with TYPE_INFORMATION`() {
        val tickerType = TargetedTickerMapper.TICKER_INFO_TYPE
        val response = TickerDataProvider.provideDummy()
        val tickerItemInfoId =
            response.getTargetedTickerData.list.find { it.type == tickerType }?.id
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemInfoId }
        assertTrue(tickerUiModel?.type == Ticker.TYPE_ANNOUNCEMENT)
    }

    @Test
    fun `WHEN setupTicker return with param and ticker info THEN return success`() {
        val tickerType = TargetedTickerMapper.TICKER_INFO_TYPE
        val response = TickerDataProvider.provideDummy()
        val tickerItemInfoId =
            response.getTargetedTickerData.list.find { it.type == tickerType }?.id
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        val param = TargetedTickerParamModel(
            page = TargetedTickerPage.TRACKING_PAGE,
            template = TargetedTickerParamModel.Template()
        )
        targetedTickerViewModel.getTargetedTicker(param)

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemInfoId }
        assertTrue(tickerUiModel?.type == Ticker.TYPE_ANNOUNCEMENT)
    }

    @Test
    fun `WHEN setupTicker with ticker type warning THEN return TickerModel with TYPE_WARNING`() {
        val tickerType = TargetedTickerMapper.TICKER_WARNING_TYPE
        val response = TickerDataProvider.provideDummy()
        val tickerItemInfoId =
            response.getTargetedTickerData.list.find { it.type == tickerType }?.id
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemInfoId }
        assertTrue(tickerUiModel?.type == Ticker.TYPE_WARNING)
    }

    @Test
    fun `WHEN setupTicker with ticker type error THEN return TickerModel with TYPE_ERROR`() {
        val tickerType = TargetedTickerMapper.TICKER_ERROR_TYPE
        val response = TickerDataProvider.provideDummy()
        val tickerItemInfoId =
            response.getTargetedTickerData.list.find { it.type == tickerType }?.id
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemInfoId }
        assertTrue(tickerUiModel?.type == Ticker.TYPE_ERROR)
    }

    @Test
    fun `WHEN setupTicker with ticker type announcement THEN return TickerModel with TYPE_ANNOUNCEMENT`() {
        val tickerType = "announcement"
        val response = TickerDataProvider.provideDummy()
        val tickerItemInfoId =
            response.getTargetedTickerData.list.find { it.type == tickerType }?.id
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemInfoId }
        assertTrue(tickerUiModel?.type == Ticker.TYPE_ANNOUNCEMENT)
    }

    @Test
    fun `WHEN setupTicker with action THEN return TickerModel content with hyperlink text`() {
        val response = TickerDataProvider.provideDummy()
        val tickerItemWithAction = response.getTargetedTickerData.list.first()
        val expected =
            "${tickerItemWithAction.content} <a href=\"${tickerItemWithAction.action.appURL}\">${tickerItemWithAction.action.label}</a>"
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemWithAction.id }
        assertTrue(tickerUiModel?.content == expected)
    }

    @Test
    fun `WHEN setupTicker without action THEN return TickerModel content without hyperlink text`() {
        val response = TickerDataProvider.provideDummy()
        val tickerItemWithoutAction =
            response.getTargetedTickerData.list.find { it.action.label.isEmpty() }
        val expected = "${tickerItemWithoutAction?.content}"
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemWithoutAction?.id }
        assertTrue(tickerUiModel?.content == expected)
    }

    @Test
    fun `WHEN setupTicker with app url THEN return TickerModel linkUrl with appUrl`() {
        val response = TickerDataProvider.provideDummy()
        val tickerItemWithAppUrl =
            response.getTargetedTickerData.list.find { it.action.appURL.isNotEmpty() }
        val expected = "${tickerItemWithAppUrl?.action?.appURL}"
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemWithAppUrl?.id }
        assertTrue(tickerUiModel?.linkUrl == expected)
    }

    @Test
    fun `WHEN setupTicker with web url THEN return TickerModel linkUrl with webUrl`() {
        val response = TickerDataProvider.provideDummy()
        val tickerItemWithWebUrl =
            response.getTargetedTickerData.list.find { it.action.webURL.isNotEmpty() }
        val expected = "${tickerItemWithWebUrl?.action?.webURL}"
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemWithWebUrl?.id }
        assertTrue(tickerUiModel?.linkUrl == expected)
    }

    @Test
    fun `WHEN setupTicker with web url and app url THEN return TickerModel linkUrl with appUrl`() {
        val response = TickerDataProvider.provideDummy()
        val tickerItemWithAppAndWebUrl = response.getTargetedTickerData.list.find {
            it.action.appURL.isNotEmpty().and(it.action.webURL.isNotEmpty())
        }
        val expected = "${tickerItemWithAppAndWebUrl?.action?.appURL}"
        coEvery { tickerUseCase.invoke(any()) } returns response

        // when
        targetedTickerViewModel.getTargetedTicker()

        // then
        val result = targetedTickerViewModel.tickerState.value as Success
        val tickerUiModel = result.data.item.find { it.id == tickerItemWithAppAndWebUrl?.id }
        assertTrue(tickerUiModel?.linkUrl == expected)
    }
}
