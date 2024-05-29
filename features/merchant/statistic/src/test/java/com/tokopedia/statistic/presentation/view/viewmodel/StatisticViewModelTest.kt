package com.tokopedia.statistic.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.ParamTableWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.usecase.GetAnnouncementDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetBarChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCardDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCarouselDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLineGraphDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiComponentDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiComponentDetailDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiLineGraphUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPieChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPostDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetProgressDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTableDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTickerUseCase
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetEmptyStateUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetLayoutUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.utils.TestConst
import com.tokopedia.statistic.view.viewmodel.StatisticViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifySuccessEquals
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

    @RelaxedMockK
    lateinit var getAnnouncementDataUseCase: GetAnnouncementDataUseCase

    @RelaxedMockK
    lateinit var getMultiComponentDataUseCase: GetMultiComponentDataUseCase

    @RelaxedMockK
    lateinit var getMultiComponentDetailDataUseCase: GetMultiComponentDetailDataUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: StatisticViewModel
    private lateinit var dynamicParameter: ParamCommonWidgetModel
    private lateinit var privateDynamicParameter: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StatisticViewModel(
            userSession,
            { getTickerUseCase },
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
            { getAnnouncementDataUseCase },
            { getMultiComponentDataUseCase },
            { getMultiComponentDetailDataUseCase },
            CoroutineTestDispatchersProvider
        )

        dynamicParameter = getDynamicParameter()

        privateDynamicParameter = viewModel::class.java.getDeclaredField("dynamicParameter").apply {
            isAccessible = true
        }
    }

    private fun getDynamicParameter(): ParamCommonWidgetModel {
        return ParamCommonWidgetModel(
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

        val dynamicParameterModel = privateDynamicParameter.get(viewModel) as ParamCommonWidgetModel

        assert(dynamicParameterModel.startDate == startDateFmt)
        assert(dynamicParameterModel.endDate == endDateFmt)
        assert(dynamicParameterModel.pageSource == mockPageSource)
        assert(dynamicParameterModel.dateType == TestConst.DATE_TYPE_DAY)
    }

    @Test
    fun `should success when get widget layout`() = runBlocking {
        val layoutList: List<BaseWidgetUiModel<*>> = emptyList()
        val shopId = "123456"
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, TestConst.PAGE_SOURCE)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout

        viewModel.getWidgetLayout(TestConst.PAGE_SOURCE)

        coVerify {
            userSession.shopId
        }
        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        viewModel.widgetLayout.verifySuccessEquals(Success(layoutList))
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

        coVerify {
            getTickerUseCase.executeOnBackground()
        }

        assert(viewModel.tickers.value is Fail)
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

        val lineGraphDataResult =
            listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

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

        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

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
    fun `should success when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } returns result

        viewModel.getMultiLineGraphWidgetData(dataKeys)

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

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } throws RuntimeException("error")

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        assert(viewModel.multiLineGraphWidgetData.value is Fail)
    }

    @Test
    fun `should success when get progress widget data`() = runBlocking {
        val dateStr = "15-07-2020"
        val dataKeys = listOf("x", "y", "z")
        val progressDataList =
            listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())

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
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )
        val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(
            dataKey = dataKeys,
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate
        )

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
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )
        val exception = MessageErrorException("error msg")

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(
            dataKey = dataKeys,
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate
        )

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
        val carouselList = listOf(
            CarouselDataUiModel(),
            CarouselDataUiModel(),
            CarouselDataUiModel(),
            CarouselDataUiModel()
        )

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

    @Test
    fun `should success when get table widget data`() = runBlocking {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )
        val result = listOf(TableDataUiModel(), TableDataUiModel())

        val dynamicParam = ParamTableWidgetModel(
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate,
            pageSource = dynamicParameter.pageSource
        )
        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParam)

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns result

        viewModel.getTableWidgetData(dataKeys)

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.tableWidgetData.value)
    }

    @Test
    fun `should failed when get table widget data`() = runBlocking {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )

        val dynamicParam = ParamTableWidgetModel(
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate,
            pageSource = dynamicParameter.pageSource
        )
        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParam)

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getTableWidgetData(dataKeys)

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        assert(viewModel.tableWidgetData.value is Fail)
    }

    @Test
    fun `should success when get pie chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())

        getPieChartDataUseCase.params =
            GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getPieChartWidgetData(dataKeys)

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

        getPieChartDataUseCase.params =
            GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getPieChartWidgetData(dataKeys)

        coVerify {
            getPieChartDataUseCase.executeOnBackground()
        }

        assert(viewModel.pieChartWidgetData.value is Fail)
    }

    @Test
    fun `should success when get bar chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())

        getBarChartDataUseCase.params =
            GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getBarChartWidgetData(dataKeys)

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

        getBarChartDataUseCase.params =
            GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getBarChartWidgetData(dataKeys)

        coVerify {
            getBarChartDataUseCase.executeOnBackground()
        }

        assert(viewModel.barChartWidgetData.value is Fail)
    }

    @Test
    fun `should success when get announcement widget data`() = runBlocking {
        val dataKeys = listOf(anyString())
        val result = listOf(AnnouncementDataUiModel())

        getAnnouncementDataUseCase.params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)

        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } returns result

        viewModel.getAnnouncementWidgetData(dataKeys)

        coVerify {
            getAnnouncementDataUseCase.executeOnBackground()
        }

        //number of data keys and result should same
        Assertions.assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.announcementWidgetData.value)
    }

    @Test
    fun `should failed when get announcement widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getAnnouncementDataUseCase.params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)

        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } throws RuntimeException("error")

        viewModel.getAnnouncementWidgetData(dataKeys)

        coVerify {
            getAnnouncementDataUseCase.executeOnBackground()
        }

        assert(viewModel.announcementWidgetData.value is Fail)
    }

    @Test
    fun `should success when get multi component data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        val multiComponentData = listOf(
            MultiComponentDataUiModel(
                dataKey = "1",
                tabs = listOf(
                    MultiComponentTab(
                        id = "1",
                        title = "title1",
                        ticker = "ticker",
                        components = listOf(
                            MultiComponentData(
                                componentType = "component type 1",
                                dataKey = "pieChart1",
                                data = null
                            ),
                            MultiComponentData(
                                componentType = "component type 2",
                                dataKey = "pieChart2",
                                data = null
                            )
                        ),
                        isLoaded = false,
                        isError = false
                    )
                )
            )
        )

        coEvery {
            getMultiComponentDataUseCase.executeOnBackground()
        } returns multiComponentData

        viewModel.getMultiComponentWidgetData(dataKeys)

        coVerify {
            getMultiComponentDataUseCase.executeOnBackground()
        }

        assert(viewModel.multiComponentWidgetData.value is Success)
        assert((viewModel.multiComponentWidgetData.value as Success).data.size == 1)
        assert(
            (viewModel.multiComponentWidgetData.value as Success)
                .data.first().tabs.first().components.size == 2
        )
    }

    @Test
    fun `should fail when get multi component data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        coEvery {
            getMultiComponentDataUseCase.executeOnBackground()
        } throws RuntimeException("error")

        viewModel.getMultiComponentWidgetData(dataKeys)

        coVerify {
            getMultiComponentDataUseCase.executeOnBackground()
        }

        assert(viewModel.multiComponentWidgetData.value is Fail)
    }

    @Test
    fun `should success when get multi component detail`() = runBlocking {
        val multiComponentTab = MultiComponentTab(
            id = "1",
            title = "title1",
            ticker = "ticker",
            components = listOf(),
            isLoaded = false,
            isError = false
        )

        val multiComponentData = listOf(
            MultiComponentData(
                componentType = "component type 1",
                dataKey = "pieChart1",
                data = PieChartWidgetUiModel(
                    id = "pie",
                    sectionId = "0",
                    widgetType = WidgetType.PIE_CHART,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = "pie",
                    ctaText = "",
                    gridSize = 4,
                    isShowEmpty = true,
                    data = PieChartDataUiModel(),
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
                )
            ),
            MultiComponentData(
                componentType = "component type 2",
                dataKey = "pieChart2",
                data = PieChartWidgetUiModel(
                    id = "pie",
                    sectionId = "0",
                    widgetType = WidgetType.PIE_CHART,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = "pie",
                    ctaText = "",
                    gridSize = 4,
                    isShowEmpty = true,
                    data = PieChartDataUiModel(),
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
                )
            )
        )

        coEvery {
            getMultiComponentDetailDataUseCase.executeOnBackground(any(), any())
        } returns multiComponentData

        viewModel.getMultiComponentDetailTabWidgetData(multiComponentTab)

        coVerify {
            getMultiComponentDetailDataUseCase.executeOnBackground(any(), any())
        }

        val successData = viewModel.multiComponentTabsData.value
        assert(successData?.components?.size == 2)
        assert(successData?.isError == false)
    }

    @Test
    fun `should Fail when get multi component detail`() = runBlocking {
        val multiComponentTab = MultiComponentTab(
            id = "1",
            title = "title1",
            ticker = "ticker",
            components = listOf(
            ),
            isLoaded = false,
            isError = false
        )

        coEvery {
            getMultiComponentDetailDataUseCase.executeOnBackground(any(), any())
        } throws RuntimeException("")


        viewModel.getMultiComponentDetailTabWidgetData(multiComponentTab)

        coVerify {
            getMultiComponentDetailDataUseCase.executeOnBackground(any(), any())
        }

        val successData = viewModel.multiComponentTabsData.value
        assert(successData?.isError == true)
    }

    @Test
    fun `should fail when one of multi component detail null`() = runBlocking {
        val multiComponentTab = MultiComponentTab(
            id = "1",
            title = "title1",
            ticker = "ticker",
            components = listOf(),
            isLoaded = false,
            isError = false
        )

        /**
         * Make one data nykk si ut errors all
         */
        val multiComponentData = listOf(
            MultiComponentData(
                componentType = "component type 1",
                dataKey = "pieChart1",
                data = PieChartWidgetUiModel(
                    id = "pie",
                    sectionId = "0",
                    widgetType = WidgetType.PIE_CHART,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = "pie",
                    ctaText = "",
                    gridSize = 4,
                    isShowEmpty = true,
                    data = PieChartDataUiModel(),
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
                )
            ),
            MultiComponentData(
                componentType = "component type 2",
                dataKey = "pieChart2",
                data = null
            )
        )

        coEvery {
            getMultiComponentDetailDataUseCase.executeOnBackground(any(), any())
        } returns multiComponentData

        viewModel.getMultiComponentDetailTabWidgetData(multiComponentTab)

        coVerify {
            getMultiComponentDetailDataUseCase.executeOnBackground(any(), any())
        }

        val successData = viewModel.multiComponentTabsData.value
        assert(successData?.components?.size == 2)
        assert(successData?.isError == true)
    }
}
