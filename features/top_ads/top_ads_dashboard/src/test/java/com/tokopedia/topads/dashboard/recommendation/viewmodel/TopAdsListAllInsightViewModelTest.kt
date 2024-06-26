package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.mapper.InsightDataMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStateUiListModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsListAllInsightViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase =
        mockk(relaxed = true)
    private lateinit var viewModel: TopAdsListAllInsightViewModel
    private val chips = mutableListOf(
        SaranTopAdsChipsUiModel(RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL_NAME, true),
        SaranTopAdsChipsUiModel(RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_NAME),
        SaranTopAdsChipsUiModel(RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_NAME),
        SaranTopAdsChipsUiModel(RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_NAME),
        SaranTopAdsChipsUiModel(RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_NAME),
        SaranTopAdsChipsUiModel(RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME)
    )

    @Before
    fun setUp() {
        viewModel = TopAdsListAllInsightViewModel(
            rule.dispatchers,
            topAdsListAllInsightCountsUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getFirstPageData failure`() {
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            throw Exception()
        }

        viewModel.getFirstPageData(String.EMPTY, Int.ZERO, null)
        assertTrue(viewModel.productInsights.value is TopAdsListAllInsightState.Fail)
    }

    @Test
    fun `getFirstPageData success adGroupType not PRODUCT_KEY`() {
        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.getFirstPageData(String.EMPTY, Int.ZERO, null)
        assertTrue(viewModel.headlineInsights.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `getFirstPageData success adGroupType is PRODUCT_KEY`() {
        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.getFirstPageData(RecommendationConstants.PRODUCT_KEY, Int.ZERO, null)
        assertTrue(viewModel.productInsights.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `getNextPageData success adGroupType not PRODUCT_KEY`() {
        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.getNextPageData(String.EMPTY, Int.ZERO, String.EMPTY, InsightDataMapper())
        assertTrue(viewModel.headlineInsights.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `getNextPageData success adGroupType is PRODUCT_KEY`() {
        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.getNextPageData(
            RecommendationConstants.PRODUCT_KEY,
            Int.ZERO,
            String.EMPTY,
            InsightDataMapper()
        )
        assertTrue(viewModel.productInsights.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `getNextPageData failure`() {
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any(),any(),any())
        } answers {
            throw Throwable()
        }
        viewModel.getNextPageData(String.EMPTY, Int.ZERO, String.EMPTY, InsightDataMapper())
        assertTrue(viewModel.productInsights.value is TopAdsListAllInsightState.Fail)
    }

    @Test
    fun `getChipsData success`() {
        val data = chips
        val actual = viewModel.getChipsData()
        assertEquals(data, actual)
    }

    @Test
    fun `getEmptyStateData INSIGHT_TYPE_POSITIVE_KEYWORD`() {
        val data = EmptyStateUiListModel(
            chips[RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD].name,
            listOf(EmptyStateData.getData()[TopAdsCommonConstant.CONST_1])
        )
        val actual = viewModel.getEmptyStateData(
            RecommendationConstants
                .InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD)
        assertEquals(data, actual)
    }

    @Test
    fun `getEmptyStateData INSIGHT_TYPE_GROUP_BID`() {
        val data = EmptyStateUiListModel(
            chips[RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID].name,
            listOf(EmptyStateData.getData()[TopAdsDashboardConstant.CONST_2], EmptyStateData.getData()[TopAdsCommonConstant.CONST_3])
        )
        val actual = viewModel.getEmptyStateData(
            RecommendationConstants
                .InsightTypeConstants.INSIGHT_TYPE_GROUP_BID)
        assertEquals(data, actual)
    }

    @Test
    fun `getEmptyStateData when else `() {
        val data = EmptyStateUiListModel(
            chips[0].name,
            listOf(EmptyStateData.getData()[TopAdsDashboardConstant.CONST_4])
        )
        val actual = viewModel.getEmptyStateData(0)
        assertEquals(data, actual)
    }

}
