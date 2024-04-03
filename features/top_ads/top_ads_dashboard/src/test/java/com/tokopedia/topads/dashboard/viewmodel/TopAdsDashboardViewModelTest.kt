package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.topads.common.data.model.ticker.DataMessage
import com.tokopedia.topads.common.data.model.ticker.Status
import com.tokopedia.topads.common.data.model.ticker.TickerInfo
import com.tokopedia.topads.common.data.model.ticker.TopAdsTickerResponse
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.domain.usecase.GetWhiteListedUserUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsTickerUseCase
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.GetPersonalisedCopyResponse
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.raw.topAdsHomepageLatestReadingJson
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.domain.usecase.TopAdsGetSelectedTopUpTypeUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsDashboardViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var summaryStatisticsUseCase: TopAdsWidgetSummaryStatisticsUseCase
    private lateinit var recommendationStatisticsUseCase: TopadsRecommendationStatisticsUseCase
    private lateinit var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase
    private lateinit var topadsTickerUseCase: TopAdsTickerUseCase
    private lateinit var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase
    private lateinit var topAdsGetSelectedTopUpTypeUseCase: TopAdsGetSelectedTopUpTypeUseCase
    private lateinit var whiteListedUserUseCase: GetWhiteListedUserUseCase
    private lateinit var topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase
    private lateinit var topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase
    private lateinit var viewModel: TopAdsDashboardViewModel

    @Before
    fun setUp() {
        summaryStatisticsUseCase = mockk(relaxed = true)
        recommendationStatisticsUseCase = mockk(relaxed = true)
        topAdsGetShopDepositUseCase = mockk(relaxed = true)
        topadsTickerUseCase = mockk(relaxed = true)
        autoTopUpUSeCase = mockk(relaxed = true)
        topAdsGetSelectedTopUpTypeUseCase = mockk(relaxed = true)
        whiteListedUserUseCase = mockk(relaxed = true)
        topAdsListAllInsightCountsUseCase = mockk(relaxed = true)
        topAdsGetTotalAdGroupsWithInsightUseCase = mockk(relaxed = true)

        viewModel = TopAdsDashboardViewModel(
            testRule.dispatchers,
            summaryStatisticsUseCase,
            recommendationStatisticsUseCase,
            topAdsGetShopDepositUseCase,
            topadsTickerUseCase,
            autoTopUpUSeCase,
            topAdsGetSelectedTopUpTypeUseCase,
            topAdsListAllInsightCountsUseCase,
            topAdsGetTotalAdGroupsWithInsightUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getShopDeposit() success check`() {
        val expected = 10

        every { topAdsGetShopDepositUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(
                Deposit(TopadsDashboardDeposits(DepositAmount(expected)))
            )
        }

        viewModel.fetchShopDeposit()
        assertEquals(expected, (viewModel.shopDepositLiveData.value as Success).data.amount)
    }

    @Test
    fun `getShopDeposit() failure check`() {
        val mockThrowable = Throwable()

        every { topAdsGetShopDepositUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.fetchShopDeposit()
        assertEquals(mockThrowable, (viewModel.shopDepositLiveData.value as Fail).throwable)
    }

    @Test
    fun `fetchRecommendationStatistics success check`() {
        val expected =
            RecommendationStatistics(
                RecommendationStatistics.Statistics(
                    mockk(relaxed = true),
                    mockk(),
                    mockk()
                )
            )

        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns expected

        viewModel.fetchRecommendationStatistics()
        assertEquals(
            (viewModel.recommendationStatsLiveData.value as Success).data,
            expected.statistics.data
        )
    }

    @Test
    fun `fetchRecommendationStatistics failure check `() {
        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns null

        viewModel.fetchRecommendationStatistics()
        assert(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `fetchRecommendationStatistics onError block test`() {
        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns mockk()

        viewModel.fetchRecommendationStatistics()
        assert(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `fetchSummaryStatistics success check`() {
        val expectedData =
            TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics(
                listOf(),
                mockk()
            )
        val fakeResponse: TopadsWidgetSummaryStatisticsModel = mockk()

        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns fakeResponse
        every { fakeResponse.topadsWidgetSummaryStatistics.widgetSummaryStatistics } returns expectedData

        viewModel.fetchSummaryStatistics("", "", "")
        assertEquals((viewModel.summaryStatisticsLiveData.value as Success).data, expectedData)
    }

    @Test
    fun `fetchSummaryStatistics failure check`() {
        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns null

        viewModel.fetchSummaryStatistics("", "", "")
        assert(viewModel.summaryStatisticsLiveData.value is Fail)
    }

    @Test
    fun `fetchSummaryStatistics onError block test`() {
        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns mockk()

        viewModel.fetchSummaryStatistics("", "", "")
        assert(viewModel.summaryStatisticsLiveData.value is Fail)
    }

    @Test
    fun `getLatestReadings success test`() {
        val expected: TopAdsLatestReading = TopAdsLatestReading().apply {
            add(TopAdsLatestReading.TopAdsLatestReadingItem(listOf(), "", "", ""))
        }

        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(
                topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java
            )
        } returns expected

        viewModel.getLatestReadings()
        assertEquals(expected[0].articles, (viewModel.latestReadingLiveData.value as Success).data)
    }

    @Test
    fun `getLatestReadings failure test`() {
        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(
                topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java
            )
        } returns TopAdsLatestReading()

        viewModel.getLatestReadings()
        assert(viewModel.latestReadingLiveData.value is Fail)
    }

    @Test
    fun `getLatestReadings onError block test`() {
        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(
                topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java
            )
        } returns mockk()

        viewModel.getLatestReadings()
        assert(viewModel.latestReadingLiveData.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus success - response not null and error is empty test, livedata should contain data`() {
        val mockObject = AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus()))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatus()
        assertEquals(
            (viewModel.autoTopUpStatusLiveData.value as Success).data,
            mockObject.response?.data
        )
    }

    @Test
    fun `getAutoTopUpStatus response not null and error not empty test, livedata should be fail`() {
        val actual = AutoTopUpData.Response(AutoTopUpData(errors = listOf(Error())))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        assertTrue(viewModel.autoTopUpStatusLiveData.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus response null and error not empty test, livedata should be fail`() {
        val actual = AutoTopUpData.Response(null)

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        assertTrue(viewModel.autoTopUpStatusLiveData.value is Fail)
    }

    @Test
    fun `fetchInsightItems failure test`() {

        val throwable = Throwable()
        coEvery {
            topAdsListAllInsightCountsUseCase.invoke(any(), any(), any())
        } answers {
            throw throwable
        }
        viewModel.fetchInsightItems("", 0, null)
        assertEquals(viewModel.productInsights.value, TopAdsListAllInsightState.Fail(throwable))
    }

    @Test
    fun `fetchInsightItems success test`() {
        val liveData = MutableLiveData<TopAdsListAllInsightState<MutableList<InsightListUiModel>>>()
        liveData.value = TopAdsListAllInsightState.Success(mutableListOf())

        coEvery {
            topAdsListAllInsightCountsUseCase.invoke(any(), any(), any())
        } answers {
            TopAdsListAllInsightCountsResponse(
                topAdsListAllInsightCounts =
                TopAdsListAllInsightCountsResponse.TopAdsListAllInsightCounts()
            )
        }

        viewModel.fetchInsightItems("", 0, null)

        assertEquals(
            viewModel.productInsights.value,
            liveData.value
        )
    }

    @Test
    fun `fetchInsightTitle failure test`() {
        val throwable = Throwable()
        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase(any(), any())
        } answers {
            throw throwable
        }
        viewModel.fetchInsightTitle()
        assertEquals(viewModel.adGroupWithInsight.value, TopAdsListAllInsightState.Fail(throwable))
    }

    @Test
    fun `fetchInsightTitle success`() {
        val data: TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> =
            TopAdsListAllInsightState.Success(TopAdsTotalAdGroupsWithInsightResponse())
        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase(any(), any())
        } answers {
            data
        }
        viewModel.fetchInsightTitle()
        assertEquals(
            viewModel.adGroupWithInsight.value, TopAdsListAllInsightState.Success(data = data).data
        )
    }

    @Test
    fun `getTopadsTicker failure test, livedata should be null`() {
        coEvery { topadsTickerUseCase.execute() } answers {
            throw Throwable("it")
        }
        viewModel.getTopadsTicker()
        assertEquals(viewModel.tickerLiveData.value, null)
    }

    @Test
    fun `getTopadsTicker response not null`() {
        val data = TopAdsTickerResponse(
            data = DataMessage(arrayListOf(), TickerInfo("", "")),
            status = Status()
        )

        coEvery { topadsTickerUseCase.execute() } answers {
            data
        }
        viewModel.getTopadsTicker()
        assertEquals(viewModel.tickerLiveData.value, data)

    }

    @Test
    fun `getAutoTopUpStatus on exception occured test`() {
        val actual = Exception("it")

        every { autoTopUpUSeCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        assertEquals((viewModel.autoTopUpStatusLiveData.value as Fail).throwable, actual)
    }

    @Test
    fun `test success in getSelectedTopUpType when credit performance is topUpFrequently`() {
        val actual = GetPersonalisedCopyResponse(
            GetPersonalisedCopyResponse.GetPersonalisedCopy(
                GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData(
                    creditPerformance = TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_UP_FREQUENTLY
                )
            )
        )
        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(GetPersonalisedCopyResponse) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()

        assertEquals(
            (viewModel.getAutoTopUpDefaultSate.value as Success).data.isAutoTopUpSelected,
            true
        )
    }

    @Test
    fun `test success in getSelectedTopUpType when credit performance is INSUFFICIENT_CREDIT`() {
        val actual = GetPersonalisedCopyResponse(
            GetPersonalisedCopyResponse.GetPersonalisedCopy(
                GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData(
                    creditPerformance = TopAdsDashboardConstant.TopAdsCreditTopUpConstant.INSUFFICIENT_CREDIT
                )
            )
        )
        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(GetPersonalisedCopyResponse) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()

        assertEquals(
            (viewModel.getAutoTopUpDefaultSate.value as Success).data.isAutoTopUpSelected,
            true
        )
    }

    @Test
    fun `test success in getSelectedTopUpType when credit performance is not INSUFFICIENT_CREDIT and not TOP_UP_FREQUENTLY`() {
        val actual = GetPersonalisedCopyResponse(
            GetPersonalisedCopyResponse.GetPersonalisedCopy(
                GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData()
            )
        )
        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(GetPersonalisedCopyResponse) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()

        assertEquals(
            (viewModel.getAutoTopUpDefaultSate.value as Success).data.isAutoTopUpSelected,
            false
        )
    }

    @Test
    fun `getSelectedTopUpType on exception ,livedata value should be Fail`() {
        val actual = Throwable("my exception")

        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()
        assertEquals(
            (viewModel.getAutoTopUpDefaultSate.value as Fail).throwable.message,
            actual.message
        )
    }
}
