package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.common.domain.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGroupDetailUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSession
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GroupDetailViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()


    private val topAdsGroupDetailUseCase: TopAdsGroupDetailUseCase = mockk(relaxed = true)
    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase =
        mockk(relaxed = true)
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase =
        mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)
    private val groupDetailMapper: GroupDetailMapper = mockk(relaxed = true)
    private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase = mockk(relaxed = true)
    private val utils: Utils = mockk(relaxed = true)
    private val userSession: UserSession = mockk(relaxed = true)
    private lateinit var viewModel: GroupDetailViewModel

    @Before
    fun setUp() {
        viewModel = GroupDetailViewModel(
            rule.dispatchers,
            topAdsGroupDetailUseCase,
            topAdsListAllInsightCountsUseCase,
            topAdsGetTotalAdGroupsWithInsightUseCase,
            topAdsCreateUseCase,
            groupDetailMapper,
            createHeadlineAdsUseCase,
            utils,
            userSession
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadDetailPage failure`() {
        coEvery {
            topAdsGroupDetailUseCase.executeOnBackground(any(), any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.loadDetailPage(Int.ZERO, String.EMPTY)
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Fail)
    }

    @Test
    fun `loadDetailPage success`() {
        val groupDetail = object : GroupDetailDataModel {
            override fun type(): String = String.EMPTY
            override fun type(typeFactory: GroupDetailAdapterFactory?): Int = Int.ZERO
            override fun equalsWith(newItem: GroupDetailDataModel): Boolean = true
        }
        val data: Map<Int, GroupDetailDataModel> = mapOf(1 to groupDetail)
        coEvery {
            topAdsGroupDetailUseCase.executeOnBackground(any(), any(), any())
        } answers {
            data
        }
        viewModel.loadDetailPage(Int.ZERO, String.EMPTY)
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `reSyncDetailPageData success`() {
        viewModel.reSyncDetailPageData(0)
        verify {
            groupDetailMapper.reSyncDetailPageData(
                0, RecommendationConstants.INVALID_INSIGHT_TYPE,
                RecommendationConstants.INVALID_INSIGHT_TYPE
            )
        }
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `loadInsightTypeChips success`() {
        viewModel.loadInsightTypeChips(String.EMPTY, arrayListOf(), String.EMPTY)
        verify { groupDetailMapper.detailPageDataMap }
    }

    @Test
    fun `loadDetailPageOnAction failure when isSwitchAdType false`() {

        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.loadDetailPageOnAction(Int.ZERO, String.EMPTY, Int.ZERO, false, String.EMPTY)
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Fail)
    }


    @Test
    fun `loadDetailPageOnAction failure when isSwitchAdType true`() {

        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.loadDetailPageOnAction(Int.ZERO, String.EMPTY, Int.ZERO, true, String.EMPTY)
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Fail)
    }

    @Test
    fun `loadDetailPageOnAction success when isSwitchAdType false`() {

        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.loadDetailPageOnAction(Int.ZERO, String.EMPTY, Int.ZERO, false, String.EMPTY)
        verify { groupDetailMapper.detailPageDataMap }
    }

    @Test
    fun `loadDetailPageOnAction success when isSwitchAdType true adgroupID empty`() {

        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.loadDetailPageOnAction(Int.ZERO, String.EMPTY, Int.ZERO, true, String.EMPTY)
        verify { groupDetailMapper.detailPageDataMap }
    }

    @Test
    fun `loadDetailPageOnAction success when isSwitchAdType true adgroupID notempty`() {

        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.loadDetailPageOnAction(Int.ZERO, "123", Int.ZERO, true, String.EMPTY)
        verify { groupDetailMapper.detailPageDataMap }
    }

    @Test
    fun `loadDetailPageOnAction success when isSwitchAdType empty, groupName empty`() {

        val data = TopAdsListAllInsightCountsResponse()
        coEvery {
            topAdsListAllInsightCountsUseCase(any(), any(), any())
        } answers {
            data
        }
        viewModel.loadDetailPageOnAction(Int.ZERO, String.EMPTY, Int.ZERO)
        verify { groupDetailMapper.detailPageDataMap }
    }

    @Test
    fun `checkIfGroupChipsAvailable return false`() {
        every {
            groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_CHIPS]
        } returns null
        assertTrue(!viewModel.checkIfGroupChipsAvailable())
    }

    @Test
    fun `checkIfGroupChipsAvailable return true`() {
        every {
            groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_CHIPS]
        } returns InsightTypeChipsUiModel()
        assertTrue(viewModel.checkIfGroupChipsAvailable())
    }

    @Test
    fun `selectDefaultChips success`() {
        viewModel.selectDefaultChips(Int.ZERO)
        verify { groupDetailMapper.detailPageDataMap }
    }

    @Test
    fun `submitInsights adType == HEADLINE_KEY && InsightType == TYPE_POSITIVE_KEYWORD failure`() {
        viewModel.submitInsights(
            TopadsManagePromoGroupProductInput(),
            String.EMPTY, RecommendationConstants.HEADLINE_KEY,
            TYPE_POSITIVE_KEYWORD, String.EMPTY
        )

        assertTrue(viewModel.editHeadlineInsightLiveData.value is Fail)
    }

    @Test
    fun `submitInsights adType == HEADLINE_KEY && InsightType == TYPE_POSITIVE_KEYWORD response error`() {
        val data = listOf(Error())
        coEvery {
            createHeadlineAdsUseCase.executeOnBackground().topadsManageHeadlineAd.errors
        } answers {
            data
        }
        viewModel.submitInsights(
            TopadsManagePromoGroupProductInput(),
            String.EMPTY, RecommendationConstants.HEADLINE_KEY,
            TYPE_POSITIVE_KEYWORD, String.EMPTY
        )
        assertTrue(viewModel.editHeadlineInsightLiveData.value is Fail)
    }

    @Test
    fun `submitInsights adType == HEADLINE_KEY && InsightType == TYPE_POSITIVE_KEYWORD response success`() {
        val data = "123"
        coEvery {
            createHeadlineAdsUseCase.executeOnBackground().topadsManageHeadlineAd.success.id
        } answers {
            data
        }
        viewModel.submitInsights(
            TopadsManagePromoGroupProductInput(),
            String.EMPTY, RecommendationConstants.HEADLINE_KEY,
            TYPE_POSITIVE_KEYWORD, String.EMPTY
        )
        coVerify { createHeadlineAdsUseCase.executeOnBackground() }
    }

    @Test
    fun `submitInsights adType != HEADLINE_KEY && InsightType != TYPE_NEGATIVE_KEYWORD response success`() {

        val data = FinalAdResponse()

        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            data
        }
        viewModel.submitInsights(
            TopadsManagePromoGroupProductInput(),
            String.EMPTY, String.EMPTY,
            Int.ZERO, String.EMPTY
        )
        coVerify { topAdsCreateUseCase.execute(any()) }
    }

    @Test
    fun `submitInsights adType != HEADLINE_KEY && InsightType != TYPE_NEGATIVE_KEYWORD response failure`() {

        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            throw Throwable()
        }
        viewModel.submitInsights(
            TopadsManagePromoGroupProductInput(),
            String.EMPTY, String.EMPTY,
            Int.ZERO, String.EMPTY
        )
        assertTrue(viewModel.editInsightLiveData.value is Fail)
    }

    @Test
    fun `loadInsightCountForOtherAdType failure`() {
        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase.invoke(any(), any())
        } answers {
            throw Throwable()
        }

        viewModel.loadInsightCountForOtherAdType(Int.ZERO)
        coVerify {
            topAdsGetTotalAdGroupsWithInsightUseCase.invoke(any(), any())
        }
    }

    @Test
    fun `loadInsightCountForOtherAdType success`() {
        val data: TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> =
            TopAdsListAllInsightState.Success(TopAdsTotalAdGroupsWithInsightResponse())
        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase.invoke(any(), any())
        } answers {
            data
        }
        viewModel.loadInsightCountForOtherAdType(Int.ZERO)
        coVerify {
            groupDetailMapper.putInsightCount(any(), any())
        }
    }

    @Test
    fun `getItemListUiModel success`() {
        viewModel.getItemListUiModel(listOf(), String.EMPTY)
        verify {
            groupDetailMapper.insightCountMap
        }
    }

    @Test
    fun `getInputDataFromMapper TYPE_POSITIVE_KEYWORD`() {
        viewModel.getInputDataFromMapper(TYPE_POSITIVE_KEYWORD)
        verify {
            groupDetailMapper.detailPageDataMap
        }
    }

    @Test
    fun `getInputDataFromMapper TYPE_KEYWORD_BID`() {
        viewModel.getInputDataFromMapper(TYPE_KEYWORD_BID)
        verify {
            groupDetailMapper.detailPageDataMap
        }
    }

    @Test
    fun `getInputDataFromMapper TYPE_GROUP_BID`() {
        viewModel.getInputDataFromMapper(TYPE_GROUP_BID)
        verify {
            groupDetailMapper.detailPageDataMap
        }
    }

    @Test
    fun `getInputDataFromMapper TYPE_DAILY_BUDGET`() {
        viewModel.getInputDataFromMapper(TYPE_DAILY_BUDGET)
        verify {
            groupDetailMapper.detailPageDataMap
        }
    }

    @Test
    fun `getInputDataFromMapper TYPE_NEGATIVE_KEYWORD_BID`() {
        viewModel.getInputDataFromMapper(TYPE_NEGATIVE_KEYWORD_BID)
        verify {
            groupDetailMapper.detailPageDataMap
        }
    }

    @Test
    fun `getInputDataFromMapper null`() {
        viewModel.getInputDataFromMapper(null)
        assertNull(viewModel.getInputDataFromMapper(null))
    }

}
