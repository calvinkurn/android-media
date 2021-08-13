package com.tokopedia.statistic.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.statistic.utils.TestConst
import com.tokopedia.statistic.view.viewmodel.StatisticViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Field
import java.util.*

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
    lateinit var getTickerUseCase: GetTickerUseCase

    @RelaxedMockK
    lateinit var getUserRoleUseCase: GetUserRoleUseCase

    @RelaxedMockK
    lateinit var getCardDataUseCase: GetCardDataUseCase

    @RelaxedMockK
    lateinit var getLineGraphDataUseCase: GetLineGraphDataUseCase

    @RelaxedMockK
    lateinit var getMultiLineGraphUseCase: GetMultiLineGraphUseCase

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
    private lateinit var dynamicParameter: DynamicParameterModel
    private lateinit var privateDynamicParameter: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StatisticViewModel(
                userSession,
                { getTickerUseCase },
                { getUserRoleUseCase },
                { getLayoutUseCase },
                { getCardDataUseCase },
                { getLineGraphDataUseCase },
                { getMultiLineGraphUseCase },
                { getProgressDataUseCase },
                { getPostDataUseCase },
                { getCarouselDataUseCase },
                { getTableDataUseCase },
                { getPieChartDataUseCase },
                { getBarChartDataUseCase },
                CoroutineTestDispatchersProvider
        )

        dynamicParameter = getDynamicParameter()

        privateDynamicParameter = viewModel::class.java.getDeclaredField("dynamicParameter").apply {
            isAccessible = true
        }
    }

    private fun getDynamicParameter(): DynamicParameterModel {
        return DynamicParameterModel(
                startDate = "15-07-20202",
                endDate = "21-07-20202",
                pageSource = TestConst.PAGE_SOURCE,
                dateType = DateFilterType.DATE_TYPE_DAY
        )
    }

    @Test
    fun `when update set filter then private dynamic parameter should updated`() {
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(daysBefore = 7))
        val endDate = Date(DateTimeUtil.getNPastDaysTimestamp(daysBefore = 1))

        val mockPageSource = "page-source"
        val startDateFmt = DateTimeUtil.format(startDate.time, TestConst.DATE_FORMAT)
        val endDateFmt = DateTimeUtil.format(endDate.time, TestConst.DATE_FORMAT)

        viewModel.setDateFilter(mockPageSource, startDate, endDate, TestConst.DATE_TYPE_DAY)

        val dynamicParameterModel = privateDynamicParameter.get(viewModel) as DynamicParameterModel

        assert(dynamicParameterModel.startDate == startDateFmt)
        assert(dynamicParameterModel.endDate == endDateFmt)
        assert(dynamicParameterModel.pageSource == mockPageSource)
        assert(dynamicParameterModel.dateType == TestConst.DATE_TYPE_DAY)
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

        viewModel.getWidgetLayout(TestConst.PAGE_SOURCE)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.getWidgetLayout(TestConst.PAGE_SOURCE)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        assert(viewModel.widgetLayout.value is Fail)
    }

    @Test
    fun `should success when get tickers`() = runBlocking {
        val tickers = listOf(TickerItemUiModel())
        val pageName = "seller-statistic"

        getTickerUseCase.params = GetTickerUseCase.createParams(pageName)

        coEvery {
            getTickerUseCase.executeOnBackground()
        } returns tickers

        viewModel.getTickers(pageName)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTickerUseCase.executeOnBackground()
        }

        assertEquals(Success(tickers), viewModel.tickers.value)
    }

    @Test
    fun `should failed when get tickers then throws exception`() = runBlocking {
        val throwable = RuntimeException("")
        val pageName = "seller-statistic"

        getTickerUseCase.params = GetTickerUseCase.createParams(pageName)

        coEvery {
            getTickerUseCase.executeOnBackground()
        } throws throwable

        viewModel.getTickers(pageName)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTickerUseCase.executeOnBackground()
        }

        assert(viewModel.tickers.value is Fail)
    }

    @Test
    fun `should success when get user role`() = runBlocking {
        val userId = 123456
        val userRoles = listOf("a", "b", "c")
        getUserRoleUseCase.params = GetUserRoleUseCase.createParam(userId)

        every {
            userSession.userId
        } returns userId.toString()

        coEvery {
            getUserRoleUseCase.executeOnBackground()
        } returns userRoles

        viewModel.getUserRole()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.userId
        }

        coVerify {
            getUserRoleUseCase.executeOnBackground()
        }

        assertEquals(Success(userRoles), viewModel.userRole.value)
    }

    @Test
    fun `when failed to get user role then throws exception`() = runBlocking {
        val userId = 123456
        val throwable = RuntimeException("error message")
        getUserRoleUseCase.params = GetUserRoleUseCase.createParam(userId)

        every {
            userSession.userId
        } returns userId.toString()

        coEvery {
            getUserRoleUseCase.executeOnBackground()
        } throws throwable

        viewModel.getUserRole()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.userId
        }

        coVerify {
            getUserRoleUseCase.executeOnBackground()
        }

        assert(viewModel.userRole.value is Fail)
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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        assert(viewModel.lineGraphWidgetData.value is Fail)
    }

    @Test
    fun `should success when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } returns result

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        //number of data keys and result should same
        Assertions.assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
    }

    @Test
    fun `should failed when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } throws RuntimeException("error")

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        assert(viewModel.multiLineGraphWidgetData.value is Fail)
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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        assert(viewModel.progressWidgetData.value is Fail)
    }

    @Test
    fun `should success when get post widget data`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"), Pair("y", "y"))
        val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns postList

        viewModel.getPostWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(postList)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.postListWidgetData.value)
    }

    @Test
    fun `should failed when get post widget data`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"))
        val exception = MessageErrorException("error msg")

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } throws exception

        viewModel.getPostWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        assert(viewModel.carouselWidgetData.value is Fail)
    }

    @Test
    fun `should success when get table widget data`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"), Pair("y", "y"))
        val result = listOf(TableDataUiModel(), TableDataUiModel())

        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns result

        viewModel.getTableWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.tableWidgetData.value)
    }

    @Test
    fun `should failed when get table widget data`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"))

        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getTableWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        assert(viewModel.tableWidgetData.value is Fail)
    }

    @Test
    fun `should success when get pie chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())

        getPieChartDataUseCase.params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getPieChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPieChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.pieChartWidgetData.value)
    }

    @Test
    fun `should failed when get pie chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getPieChartDataUseCase.params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getPieChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPieChartDataUseCase.executeOnBackground()
        }

        assert(viewModel.pieChartWidgetData.value is Fail)
    }

    @Test
    fun `should success when get bar chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())

        getBarChartDataUseCase.params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getBarChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getBarChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
    }

    @Test
    fun `should failed when get bar chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getBarChartDataUseCase.params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getBarChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getBarChartDataUseCase.executeOnBackground()
        }

        assert(viewModel.barChartWidgetData.value is Fail)
    }
}