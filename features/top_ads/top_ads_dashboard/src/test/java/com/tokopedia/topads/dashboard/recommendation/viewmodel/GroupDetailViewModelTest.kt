package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.common.domain.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TAB_NAME_PRODUCT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TAB_NAME_SHOP
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ListBottomSheetItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGroupDetailUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
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
    private val groupDetailMapper: GroupDetailMapper = GroupDetailMapper()
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
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `loadInsightTypeChips updates detailPageDataMap`() {
        val adType = "Product"
        val insightList = arrayListOf<AdGroupUiModel>()
        val adGroupName = "Test Group"
        val expectedInsightTypeChipsUiModel = InsightTypeChipsUiModel(
            mutableListOf(
                TAB_NAME_SHOP,
                adGroupName
            ),
            insightList.toMutableList()
        )
        viewModel.loadInsightTypeChips(adType, insightList, adGroupName)
        val actualInsightTypeChipsUiModel = groupDetailMapper.detailPageDataMap[TYPE_INSIGHT]
        assertEquals(expectedInsightTypeChipsUiModel, actualInsightTypeChipsUiModel)
    }

    @Test
    fun `loadDetailPageOnAction failure when isSwitchAdType false`() {
        coEvery {
            topAdsGroupDetailUseCase.executeOnBackground(any(), any(), any())
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
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
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
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
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
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
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
        assertTrue(viewModel.detailPageLiveData.value is TopAdsListAllInsightState.Success)
    }

    @Test
    fun `checkIfGroupChipsAvailable test`() {
        val data = groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_CHIPS]?.isAvailable()
        val actual = viewModel.checkIfGroupChipsAvailable()
        assertEquals(data, actual)
    }

    @Test
    fun `selectDefaultChips success`() {
        val data = GroupDetailChipsUiModel()
        viewModel.selectDefaultChips(Int.ZERO)
        val actual = groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_CHIPS]
        assertEquals(data, actual)
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
        assertEquals(Success(data), viewModel.editInsightLiveData.value)
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
        val response = TopAdsTotalAdGroupsWithInsightResponse()
        val data: TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> =
            TopAdsListAllInsightState.Success(response)
        coEvery {
            topAdsGetTotalAdGroupsWithInsightUseCase.invoke(any(), any())
        } answers {
            data
        }
        viewModel.loadInsightCountForOtherAdType(TYPE_PRODUCT_VALUE)
        val actual = groupDetailMapper.insightCountMap.size
        assertEquals(3, actual)
    }

    @Test
    fun `getItemListUiModel emptylist`() {
        val data = viewModel.getItemListUiModel(listOf(), String.EMPTY)
        assertEquals(0, data.size)
    }

    @Test
    fun `getItemListUiModel adds product value item when adType = TYPE_PRODUCT_VALUE`() {
        val titleList = listOf("Product", "Shop")
        val adGroupType = PRODUCT_KEY
        val expectedItem = ListBottomSheetItemUiModel(
            adType = TYPE_PRODUCT_VALUE,
            title = titleList.first(),
            isSelected = true
        )
       groupDetailMapper.insightCountMap[TYPE_PRODUCT_VALUE] = 1
        val result = viewModel.getItemListUiModel(titleList, adGroupType)
        assertTrue(result.contains(expectedItem))
    }

    @Test
    fun `getItemListUiModel adds product value item when adType = TYPE_SHOP_VALUE`() {
        val titleList = listOf("Product", "Shop")
        val adGroupType = PRODUCT_KEY
        val expectedItem = titleList.getOrNull(TopAdsCommonConstant.CONST_1)?.let {
            ListBottomSheetItemUiModel(
                adType = RecommendationConstants.TYPE_SHOP_VALUE,
                title = it,
                isSelected = false
            )
        }
        groupDetailMapper.insightCountMap[RecommendationConstants.TYPE_SHOP_VALUE] = 3
        val result = viewModel.getItemListUiModel(titleList, adGroupType)
        assertTrue(result.contains(expectedItem))
    }

    @Test
    fun `getInputDataFromMapper TYPE_POSITIVE_KEYWORD`() {
        val data =
            ((groupDetailMapper.detailPageDataMap[TYPE_POSITIVE_KEYWORD] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianKataKunciUiModel)?.input
        val actual = viewModel.getInputDataFromMapper(TYPE_POSITIVE_KEYWORD)
        assertEquals(data, actual)
    }

    @Test
    fun `getInputDataFromMapper TYPE_KEYWORD_BID`() {
        val data =
            ((groupDetailMapper.detailPageDataMap[TYPE_KEYWORD_BID] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianKeywordBidUiModel)?.input
        val actual = viewModel.getInputDataFromMapper(TYPE_KEYWORD_BID)
        assertEquals(data, actual)
    }

    @Test
    fun `getInputDataFromMapper TYPE_GROUP_BID`() {
        val data =
            ((groupDetailMapper.detailPageDataMap[TYPE_GROUP_BID] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianGroupBidUiModel)?.input
        val actual = viewModel.getInputDataFromMapper(TYPE_GROUP_BID)
        assertEquals(data, actual)
    }

    @Test
    fun `getInputDataFromMapper TYPE_DAILY_BUDGET`() {
        val data =
            ((groupDetailMapper.detailPageDataMap[TYPE_DAILY_BUDGET] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianDailyBudgetUiModel)?.input
        val actual = viewModel.getInputDataFromMapper(TYPE_DAILY_BUDGET)
        assertEquals(data, actual)
    }

    @Test
    fun `getInputDataFromMapper TYPE_NEGATIVE_KEYWORD_BID `() {
        val data =
            ((groupDetailMapper.detailPageDataMap[TYPE_NEGATIVE_KEYWORD_BID] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianNegativeKeywordUiModel)?.input
        val actual = viewModel.getInputDataFromMapper(TYPE_NEGATIVE_KEYWORD_BID)
        assertEquals(data, actual)
    }

    @Test
    fun `getInputDataFromMapper null`() {
        viewModel.getInputDataFromMapper(null)
        assertNull(viewModel.getInputDataFromMapper(null))
    }

}
