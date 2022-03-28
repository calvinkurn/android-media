package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
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
import kotlinx.coroutines.test.runBlockingTest
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
    private lateinit var viewModel: TopAdsDashboardViewModel

    @Before
    fun setUp() {
        summaryStatisticsUseCase = mockk(relaxed = true)
        recommendationStatisticsUseCase = mockk(relaxed = true)
        topAdsGetShopDepositUseCase = mockk(relaxed = true)
        viewModel = TopAdsDashboardViewModel(
            summaryStatisticsUseCase, recommendationStatisticsUseCase, topAdsGetShopDepositUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getLatestReadings should parse topAdsHomepageLatestReadingJson and invoke 0th article as success`() {
        val expected = Gson().fromJson(topAdsHomepageLatestReadingJson, TopAdsLatestReading::class.java)
        viewModel.getLatestReadings()

        assertEquals(expected[0].articles , (viewModel.latestReadingLiveData.value as Success).data)
    }

    @Test
    fun `fetchSummaryStatistics failure check`() {
        val expected : TopadsWidgetSummaryStatisticsModel? = null

        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns expected

        viewModel.fetchSummaryStatistics("", "", "")
        assert(viewModel.summaryStatisticsLiveData.value is Fail)
    }

    @Test
    fun `fetchSummaryStatistics success check`() {
        val expected =
            TopadsWidgetSummaryStatisticsModel(
                TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics(
                    TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics(
                        emptyList(), mockk()), mockk()))

        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns expected

        viewModel.fetchSummaryStatistics("", "", "")
        assertEquals(
            (viewModel.summaryStatisticsLiveData.value as Success).data,
            expected.topadsWidgetSummaryStatistics.widgetSummaryStatistics
        )
    }

    @Test
    fun `fetchRecommendationStatistics failure check if data is null after fetching`() {

        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns null

        viewModel.fetchRecommendationStatistics()
        assert(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `fetchRecommendationStatistics success check`() {
        val expected =
            RecommendationStatistics(RecommendationStatistics.Statistics(
                mockk(), listOf(), mockk()))

        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns expected

        viewModel.fetchRecommendationStatistics()
        assertEquals((viewModel.recommendationStatsLiveData.value as Success).data,
            expected.statistics.data)
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
}