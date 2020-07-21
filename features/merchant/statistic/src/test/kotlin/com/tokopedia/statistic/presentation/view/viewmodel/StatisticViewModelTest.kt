package com.tokopedia.statistic.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhomecommon.domain.model.WidgetDataParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.statistic.utils.TestConst
import com.tokopedia.statistic.utils.TestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 20/07/20
 */

@ExperimentalCoroutinesApi
class StatisticViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getLayoutUseCase: GetLayoutUseCase

    @RelaxedMockK
    lateinit var getCardDataUseCase: GetCardDataUseCase

    @RelaxedMockK
    lateinit var getLineGraphDataUseCase: GetLineGraphDataUseCase

    @RelaxedMockK
    lateinit var getProgressDataUseCase: GetProgressDataUseCase

    @RelaxedMockK
    lateinit var getPostDataUseCase: GetPostDataUseCase

    @RelaxedMockK
    lateinit var getCarouselDataUseCase: GetCarouselDataUseCase

    @RelaxedMockK
    lateinit var getTableDataUseCase: GetTableDataUseCase

    @RelaxedMockK
    lateinit var getPieChartDataUseCase: GetPieChartDataUseCase

    @RelaxedMockK
    lateinit var getBarChartDataUseCase: GetBarChartDataUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: StatisticViewModel
    private lateinit var dynamicParameter: WidgetDataParameterModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StatisticViewModel(userSession, getLayoutUseCase, getCardDataUseCase, getLineGraphDataUseCase,
                getProgressDataUseCase, getPostDataUseCase, getCarouselDataUseCase, getTableDataUseCase,
                getPieChartDataUseCase, getBarChartDataUseCase, TestDispatchersProvider)

        dynamicParameter = getDynamicParameter()
    }

    private fun getDynamicParameter(): WidgetDataParameterModel {
        return WidgetDataParameterModel(
                startDate = "15-07-20202",
                endDate = "21-07-20202",
                pageSource = TestConst.PAGE_SOURCE
        )
    }

    @Test
    fun `should success when get widget layout`() = runBlocking {
        val layoutList: List<BaseWidgetUiModel<*>> = emptyList()
        val shopId = "123456"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, TestConst.PAGE_SOURCE)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList

        viewModel.getWidgetLayout()

        coVerify {
            userSession.shopId
        }
        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        Assertions.assertEquals(Success(layoutList), viewModel.widgetLayout.value)
    }

    @Test
    fun `should failed when get widget layout`() = runBlocking {
        val throwable = MessageErrorException("error message")
        val shopId = "123456"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, TestConst.PAGE_SOURCE)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } throws throwable

        viewModel.getWidgetLayout()

        coVerify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        assert(viewModel.widgetLayout.value is Fail)
    }

    @Test
    fun `should success when get card widget data`() = runBlocking {
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardDataResult

        viewModel.getCardWidgetData(dataKeys)

        coVerify {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.cardWidgetData.value)
    }

    @Test
    fun `should failed when get card widget data`() = runBlocking {
        val dataKeys = listOf("a", "b", "c")
        val throwable = ResponseErrorException()

        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getCardWidgetData(dataKeys)

        val result = viewModel.cardWidgetData.value
        assert(result is Fail)
    }

    @Test
    fun `should success when get line graph widget data`() = runBlocking {
        val dataKeys = listOf("x", "y", "z")

        val lineGraphDataResult = listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } returns lineGraphDataResult

        viewModel.getLineGraphWidgetData(dataKeys)

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(lineGraphDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.lineGraphWidgetData.value)
    }

    @Test
    fun `should failed when get line graph widget data`() = runBlocking {
        val dataKeys = listOf("x", "y", "z")
        val throwable = MessageErrorException("error message")

        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getLineGraphWidgetData(dataKeys)

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        assert(viewModel.lineGraphWidgetData.value is Fail)
    }

    @Test
    fun `should success when get progress widget data`() = runBlocking {
        val dateStr = "15-07-2020"
        val dataKeys = listOf("x", "y", "z")
        val progressDataList = listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(dateStr, dataKeys)

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns progressDataList

        viewModel.getProgressWidgetData(dataKeys)

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(progressDataList)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.progressWidgetData.value)
    }

    @Test
    fun `should failed when get progress widget data`() = runBlocking {
        val dateStr = "15-07-2020"
        val dataKeys = listOf("x", "y", "z")
        val throwable = MessageErrorException("error")

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(dateStr, dataKeys)

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getProgressWidgetData(dataKeys)

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        assert(viewModel.progressWidgetData.value is Fail)
    }

    @Test
    fun `should success when get post widget data`() = runBlocking {
        val dataKeys = listOf("x", "x")
        val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns postList

        viewModel.getPostWidgetData(dataKeys)

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(postList)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.postListWidgetData.value)
    }

    @Test
    fun `should failed when get post widget data`() = runBlocking {
        val dataKeys = listOf("x", "x")
        val exception = MessageErrorException("error msg")

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } throws exception

        viewModel.getPostWidgetData(dataKeys)

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        assert(viewModel.postListWidgetData.value is Fail)
    }

    @Test
    fun `should success when get carousel widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val carouselList = listOf(CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel())

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } returns carouselList

        viewModel.getCarouselWidgetData(dataKeys)

        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(carouselList)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.carouselWidgetData.value)
    }

    @Test
    fun `should failed when get carousel widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val throwable = MessageErrorException("error")

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getCarouselWidgetData(dataKeys)

        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        assert(viewModel.carouselWidgetData.value is Fail)
    }
}