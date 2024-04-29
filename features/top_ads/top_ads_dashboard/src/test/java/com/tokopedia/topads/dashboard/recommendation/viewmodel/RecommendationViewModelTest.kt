package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.common.data.model.TopadsInsightProductsResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsInsightProductsUseCase
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetShopInfoUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RecommendationViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val recommendationStatisticsUseCase: TopadsRecommendationStatisticsUseCase =
        mockk(relaxed = true)
    private val topAdsInsightProductsUseCase: TopAdsInsightProductsUseCase = mockk(relaxed = true)
    private val topAdsGetShopInfoUseCase: TopAdsGetShopInfoUseCase = mockk(relaxed = true)
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase =
        mockk(relaxed = true)
    private lateinit var viewModel: RecommendationViewModel

    @Before
    fun setUp() {
        viewModel = RecommendationViewModel(
            recommendationStatisticsUseCase,
            topAdsInsightProductsUseCase,
            rule.dispatchers,
            topAdsGetShopInfoUseCase,
            topAdsGetTotalAdGroupsWithInsightUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadRecommendationPage failure`() {

        coEvery {
            topAdsGetShopInfoUseCase(source = any())
        } answers {
            throw Exception()
        }

        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase(any(), any())
        } answers {
            throw Exception()
        }

        viewModel.loadRecommendationPage()
        assertTrue(viewModel.shopInfo.value is Fail)
        assertTrue(viewModel.adGroupWithInsight.value is TopAdsListAllInsightState.Fail)
    }

    @Test
    fun `loadRecommendationPage success, getAdGroupWithInsight success`() {

        val shopInfo: Result<TopAdsGetShopInfoUiModel> = Success(TopAdsGetShopInfoUiModel())
        val adGroupWithInsight: TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> =
            TopAdsListAllInsightState.Success(TopAdsTotalAdGroupsWithInsightResponse())

        coEvery {
            topAdsGetShopInfoUseCase(source = any())
        } answers {
            shopInfo
        }

        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase(any(), any())
        } answers {
            adGroupWithInsight
        }

        viewModel.loadRecommendationPage()
        assertTrue(viewModel.shopInfo.value is Success)
        assertTrue(viewModel.adGroupWithInsight.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `loadRecommendationPage success, getAdGroupWithInsight fail`() {

        val shopInfo: Result<TopAdsGetShopInfoUiModel> = Success(TopAdsGetShopInfoUiModel())
        val adGroupWithInsight: TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> =
            TopAdsListAllInsightState.Fail(Exception())

        coEvery {
            topAdsGetShopInfoUseCase(source = any())
        } answers {
            shopInfo
        }

        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase(any(), any())
        } answers {
            adGroupWithInsight
        }

        viewModel.loadRecommendationPage()
        assertTrue(viewModel.shopInfo.value is Success)
        assertTrue(viewModel.adGroupWithInsight.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `fetchRecommendationStatistics failure`() {
        coEvery {
            recommendationStatisticsUseCase.fetchRecommendationStatistics()
        } answers {
            throw Exception()
        }
        viewModel.fetchRecommendationStatistics()
        assertTrue(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `fetchRecommendationStatistics success data not null`() {
        val data = RecommendationStatistics(
            statistics = RecommendationStatistics.Statistics(
                data = RecommendationStatistics.Statistics.Data(
                    dailyBudgetRecommendationStats = RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats(
                        count = 0,
                        groupList = listOf(
                            RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats.GroupInfo(
                                "Group1"
                            )
                        ),
                        totalClicks = 0
                    ),
                    keywordRecommendationStats = RecommendationStatistics.Statistics.Data.KeywordRecommendationStats(
                        groupCount = 0,
                        insightCount = 0,
                        topGroups = listOf(RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup())
                    ),
                    productRecommendationStats = RecommendationStatistics.Statistics.Data.ProductRecommendationStats(
                        count = 0,
                        productList = listOf(),
                        totalSearchCount = 0
                    )
                ),
                errors = listOf(),
                header = RecommendationStatistics.Statistics.Header(processTime = 0.0)
            )
        )
        coEvery {
            recommendationStatisticsUseCase.fetchRecommendationStatistics()
        } answers {
            data
        }
        viewModel.fetchRecommendationStatistics()
        assertTrue(viewModel.recommendationStatsLiveData.value is Success)
    }

    @Test
    fun `fetchRecommendationStatistics success data = null`() {
        coEvery {
            recommendationStatisticsUseCase.fetchRecommendationStatistics()?.statistics?.data
        } answers {
            null
        }
        viewModel.fetchRecommendationStatistics()
        assertTrue(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `getOutOfStockProducts failure`() {
        coEvery {
            topAdsInsightProductsUseCase(String.EMPTY)
        } answers {
            throw Exception()
        }
        viewModel.getOutOfStockProducts()
        assertTrue(viewModel.productInsightLiveData.value is Fail)
    }

    @Test
    fun `getOutOfStockProducts success no error`() {
        val data = TopadsInsightProductsResponse()
        coEvery {
            topAdsInsightProductsUseCase(String.EMPTY)
        } answers {
            data
        }
        viewModel.getOutOfStockProducts()
        assertTrue(viewModel.productInsightLiveData.value is Success)
    }

    @Test
    fun `getOutOfStockProducts success with error`() {
        val data = listOf(Error())
        coEvery {
            topAdsInsightProductsUseCase(String.EMPTY).topadsInsightProducts?.errors
        } answers {
            data
        }
        viewModel.getOutOfStockProducts()
        assertTrue(viewModel.productInsightLiveData.value is Fail)
    }
}
