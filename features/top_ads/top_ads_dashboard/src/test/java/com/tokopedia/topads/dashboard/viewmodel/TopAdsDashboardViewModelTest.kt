package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsTickerUseCase
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.raw.topAdsHomepageLatestReadingJson
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
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
    private lateinit var viewModel: TopAdsDashboardViewModel

    @Before
    fun setUp() {
        summaryStatisticsUseCase = mockk(relaxed = true)
        recommendationStatisticsUseCase = mockk(relaxed = true)
        topAdsGetShopDepositUseCase = mockk(relaxed = true)
        topadsTickerUseCase = mockk(relaxed = true)
        viewModel = TopAdsDashboardViewModel(
            summaryStatisticsUseCase, recommendationStatisticsUseCase, topAdsGetShopDepositUseCase, topadsTickerUseCase
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
            RecommendationStatistics(RecommendationStatistics.Statistics(
                mockk(relaxed = true), mockk(), mockk()))

        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns expected

        viewModel.fetchRecommendationStatistics()
        assertEquals((viewModel.recommendationStatsLiveData.value as Success).data,
            expected.statistics.data)
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
                listOf(), mockk())
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
            anyConstructed<Gson>().fromJson(topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java)
        } returns expected

        viewModel.getLatestReadings()
        assertEquals(expected[0].articles, (viewModel.latestReadingLiveData.value as Success).data)
    }

    @Test
    fun `getLatestReadings failure test`() {
        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java)
        } returns TopAdsLatestReading()

        viewModel.getLatestReadings()
        assert(viewModel.latestReadingLiveData.value is Fail)
    }

    @Test
    fun `getLatestReadings onError block test`() {
        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java)
        } returns mockk()

        viewModel.getLatestReadings()
        assert(viewModel.latestReadingLiveData.value is Fail)
    }
}
