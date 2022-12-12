package com.tokopedia.topads.dashboard.view.model

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.HeadlineInfoResponse
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupListUseCase
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.viewmodel.GroupDetailViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GroupDetailViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase =
        mockk(relaxed = true)
    private val topAdsGetAdKeywordUseCase: TopAdsGetAdKeywordUseCase = mockk(relaxed = true)
    private val topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private val topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase = mockk(relaxed = true)
    private val topAdsGetStatisticsUseCase: TopAdsGetStatisticsUseCase = mockk(relaxed = true)
    private val topAdsKeywordsActionUseCase: TopAdsKeywordsActionUseCase = mockk(relaxed = true)
    private val topAdsGroupActionUseCase: TopAdsGroupActionUseCase = mockk(relaxed = true)
    private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase =
        mockk(relaxed = true)
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase =
        mockk(relaxed = true)
    private val groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)
    private val headlineInfoUseCase: GetHeadlineInfoUseCase = mockk(relaxed = true)
    private val res: Resources = mockk(relaxed = true)
    private val throwable: Throwable = mockk(relaxed = true)
    private val params: RequestParams = mockk(relaxed = true)

    private val viewModel by lazy {
        GroupDetailViewModel(rule.dispatchers,
            topAdsGetGroupProductDataUseCase,
            topAdsGetAdKeywordUseCase,
            topAdsProductActionUseCase,
            topAdsGetGroupListUseCase,
            topAdsGetStatisticsUseCase,
            topAdsKeywordsActionUseCase,
            topAdsGroupActionUseCase,
            topAdsGetProductKeyCountUseCase,
            topAdsGetProductStatisticsUseCase,
            groupInfoUseCase,
            headlineInfoUseCase,
            bidInfoUseCase,
            topAdsCreateUseCase,
            userSession)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `get product data`() {
        viewModel.getGroupProductData(1, "1", "", "", 1, "", "", 0, {}) {}
        coVerify {
            topAdsGetGroupProductDataUseCase.execute(any())
        }
    }

    @Test
    fun `getGroupProductData success check`() {
        val fakeResponse =
            spyk(NonGroupResponse(NonGroupResponse.TopadsDashboardGroupProducts(data = listOf(mockk()))))
        var actual: NonGroupResponse.TopadsDashboardGroupProducts? = null

        coEvery { topAdsGetGroupProductDataUseCase.execute(any()) } returns fakeResponse

        viewModel.getGroupProductData(1, "1", "", "", 1, "", "", 0, {
            actual = it
        }, {})

        Assert.assertEquals(actual, fakeResponse.topadsDashboardGroupProducts)
    }

    @Test
    fun `getGroupProductData on empty response should invoke error`() {
        val fakeResponse =
            spyk(NonGroupResponse(NonGroupResponse.TopadsDashboardGroupProducts(data = emptyList())))
        var onErrorInvoked = false

        coEvery { topAdsGetGroupProductDataUseCase.execute(any()) } returns fakeResponse

        viewModel.getGroupProductData(1, "1", "", "", 1, "", "", 0, {}, {
            onErrorInvoked = true
        })

        Assert.assertTrue(onErrorInvoked)
    }

    @Test
    fun `get group info success`() {
        val expected = 10.0F
        var actual = 0.0F
        val data = GroupInfoResponse.TopAdsGetPromoGroup.Data(daiyBudget = expected)
        val onSuccess: (data: GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit = {
            actual = it.daiyBudget
        }
        every { groupInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        viewModel.getGroupInfo(res, "", "", onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get group info error`() {
        val throwable = spyk(Throwable())

        every { groupInfoUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getGroupInfo(res, "", "") { }

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `get headline info success`() {
        val expected = 10
        var actual = 0
        val data = HeadlineInfoResponse.TopAdsGetPromoHeadline.Data(priceBid = expected)
        val onSuccess: (data: HeadlineInfoResponse.TopAdsGetPromoHeadline.Data) -> Unit = {
            actual = it.priceBid
        }
        every { headlineInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        viewModel.getHeadlineInfo(res, "", onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getHeadlineInfo error`() {
        val throwable = spyk(Throwable())

        every { headlineInfoUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getHeadlineInfo(res, "") { }

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getProductStats success`() {
        val expected = "10"
        var actual = "0"
        val data =
            ProductStatisticsResponse(getDashboardProductStatistics = GetDashboardProductStatistics(
                listOf(WithoutGroupDataItem(adId = expected))))
        val onSuccess: (data: GetDashboardProductStatistics) -> Unit = {
            actual = it.data[0].adId
        }
        every {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(captureLambda(),
                any())
        } answers {
            onSuccess.invoke(data.getDashboardProductStatistics)
        }
        viewModel.getProductStats(res, "", "", listOf(), onSuccess, "", 1, 0)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getProductStats error`() {
        val throwable = spyk(Throwable())

        every {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(),
                captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getProductStats(res, "", "", listOf(), {}, "", 1, 0)

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getBidInfo success`() {
        val expected = spyk(ResponseBidInfo.Result())
        var actual: List<TopadsBidInfo.DataItem>? = null

        every { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(expected)
        }

        viewModel.getBidInfo(listOf(), "") {
            actual = it
        }

        Assert.assertEquals(expected.topadsBidInfo.data, actual)
    }

    @Test
    fun `getBidInfo failure`() {
        val throwable = spyk(Throwable())

        every { bidInfoUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getBidInfo(listOf(), "") {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getGroupKeywordData success`() {
        val expected = "10"
        var actual = "0"
        val data =
            KeywordsResponse(getTopadsDashboardKeywords = KeywordsResponse.GetTopadsDashboardKeywords
                (listOf(KeywordsResponse.GetTopadsDashboardKeywords.DataItem(keywordId = expected))))
        val onSuccess: (data: KeywordsResponse.GetTopadsDashboardKeywords) -> Unit = {
            actual = it.data[0].keywordId
        }
        every { topAdsGetAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data.getTopadsDashboardKeywords)
        }
        viewModel.getGroupKeywordData(res, 1, 1, "", "", 1, 1, onSuccess, {})
        Assert.assertEquals(expected, actual)

    }

    @Test
    fun `getGroupKeywordData empty response`() {
        val fakeObj = spyk(KeywordsResponse())

        every { topAdsGetAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(KeywordsResponse) -> Unit>().invoke(fakeObj)
        }

        var onEmptyInvoked = false

        viewModel.getGroupKeywordData(res, 1, 1, "", "", 1, 1, { }, {
            onEmptyInvoked = true
        })

        Assert.assertTrue(onEmptyInvoked)
    }

    @Test
    fun `getGroupKeywordData error check`() {
        val throwable = spyk(Throwable())

        every { topAdsGetAdKeywordUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getGroupKeywordData(res, 1, 1, "", "", 1, 1, {}, {})

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getCountProductKeyword success`() {
        var actual: List<CountDataItem>? = null
        val expected = TotalProductKeyResponse()

        every {
            topAdsGetProductKeyCountUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(TotalProductKeyResponse) -> Unit>().invoke(expected)
        }
        viewModel.getCountProductKeyword(res, listOf()) {
            actual = it
        }
        Assert.assertEquals(expected.topAdsGetTotalAdsAndKeywords.data, actual)
    }

    @Test
    fun `getCountProductKeyword error`() {
        val throwable = spyk(Throwable())

        every {
            topAdsGetProductKeyCountUseCase.executeQuerySafeMode(any(),
                captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getCountProductKeyword(res, listOf()) {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getTopAdsStatistic check`() {
        viewModel.getTopAdsStatistic(Date(), Date(), 1, {}, "", 0)
        coVerify {
            topAdsGetStatisticsUseCase.execute(any())
        }
    }

    @Test
    fun `getTopAdsStatistic success`() {
        val expected = StatsData()
        var actual: DataStatistic? = null

        coEvery { topAdsGetStatisticsUseCase.execute(any()) } returns expected

        viewModel.getTopAdsStatistic(Date(), Date(), 1, {
            actual = it
        }, "", 0)

        Assert.assertEquals(expected.topadsDashboardStatistics.data, actual)
    }

    @Test
    fun `getTopAdsStatistic failure`() {
        val exception = spyk(Exception())

        coEvery { topAdsGetStatisticsUseCase.execute(any()) } throws exception

        viewModel.getTopAdsStatistic(Date(), Date(), 1, {}, "", 0)

        verify { exception.printStackTrace() }
    }

    @Test
    fun `getGroupList success`() {
        var actual: List<GroupListDataItem>? = null
        val expected = DashGroupListResponse()

        coEvery { topAdsGetGroupListUseCase.execute(any()) } returns expected

        viewModel.getGroupList("") {
            actual = it
        }

        Assert.assertEquals(expected.getTopadsDashboardGroups.data, actual)
    }

    @Test
    fun `getGroupList failure`() {
        val exception = spyk(Exception())

        coEvery { topAdsGetGroupListUseCase.execute(any()) } throws exception

        viewModel.getGroupList("") {}

        verify { exception.printStackTrace() }
    }

    @Test
    fun `setProductAction test`() {

        viewModel.setProductAction({}, "", listOf(), "")

        coVerify { topAdsProductActionUseCase.execute(any()) }
    }

    @Test
    fun `setProductAction success invokes`() {
        var successInvoked = false

        viewModel.setProductAction({
            successInvoked = true
        }, "", listOf(), "")

        assert(successInvoked)
    }

    @Test
    fun `setProductAction error invokes`() {
        val throwable = spyk(Throwable())

        every { topAdsProductActionUseCase.setParams("", listOf(), "") } throws throwable

        viewModel.setProductAction({}, "", listOf(), "")

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `setGroupAction test`() {
        viewModel.setGroupAction("", listOf(), res)
        coVerify {
            topAdsGroupActionUseCase.execute(any(), any())
        }
    }

    @Test
    fun `setGroupAction error invokes`() {
        val throwable = spyk(Throwable())

        every { topAdsGroupActionUseCase.setParams("", listOf()) } throws throwable

        viewModel.setGroupAction("", listOf(), res)

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `setKeywordAction success`() {
        var onSuccessInvoked = false

        every {
            topAdsKeywordsActionUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(KeywordActionResponse) -> Unit>().invoke(mockk())
        }

        viewModel.setKeywordAction("", listOf(), res, { onSuccessInvoked = true })
        Assert.assertTrue(onSuccessInvoked)
    }

    @Test
    fun `setProductActionMoveGroup success test`() {
        var successCalled = false
        val fakeParam: RequestParams = mockk(relaxed = true)

        every {
            topAdsCreateUseCase.createRequestParamMoveGroup(any(),
                TopAdsDashboardConstant.SOURCE_DASH, any(), ParamObject.ACTION_ADD)
        } returns fakeParam

        viewModel.setProductActionMoveGroup("", listOf()) { successCalled = true }

        coVerify {
            topAdsCreateUseCase.execute(fakeParam)
        }
        Assert.assertTrue(successCalled)
    }

    @Test
    fun `setProductActionMoveGroup error test`() {
        var successCalled = false

        every {
            topAdsCreateUseCase.createRequestParamMoveGroup(any(),
                TopAdsDashboardConstant.SOURCE_DASH, any(), ParamObject.ACTION_ADD)
        } throws throwable

        viewModel.setProductActionMoveGroup("", listOf()) { successCalled = true }

        verify { throwable.printStackTrace() }
        Assert.assertEquals(successCalled, false)
    }

    @Test
    fun `setKeywordActionForGroup if action is not delete`() {

        viewModel.setKeywordActionForGroup("", "", listOf(), res) {}

        coVerify(exactly = 0) {
            topAdsCreateUseCase.execute(params)
        }
        verify {
            topAdsKeywordsActionUseCase.setParams(any(), any())
        }
    }

    @Test
    fun `setKeywordActionForGroup for action == delete success`() {
        var successCalled = false
        every {
            topAdsCreateUseCase.createRequestParamActionDelete(any(), any(), any())
        } returns params
        viewModel.setKeywordActionForGroup("",
            TopAdsDashboardConstant.ACTION_DELETE,
            listOf(),
            res) { successCalled = true }
        coVerify { topAdsCreateUseCase.execute(params) }

        Assert.assertTrue(successCalled)
    }

    @Test
    fun `setKeywordActionForGroup for action == delete failure`() {
        var successCalled = false
        every {
            topAdsCreateUseCase.createRequestParamActionDelete(any(), any(), any())
        } throws throwable

        viewModel.setKeywordActionForGroup("",
            TopAdsDashboardConstant.ACTION_DELETE,
            listOf(),
            res) { successCalled = true }

        Assert.assertTrue(!successCalled)
    }


    @Test
    fun `check detach view`() {
        viewModel.onCleared()
        verify { topAdsGetAdKeywordUseCase.cancelJobs() }
        verify { topAdsKeywordsActionUseCase.cancelJobs() }
        verify { topAdsGetProductStatisticsUseCase.cancelJobs() }
        verify { headlineInfoUseCase.cancelJobs() }
        verify { topAdsGetProductKeyCountUseCase.cancelJobs() }
        verify { bidInfoUseCase.cancelJobs() }
        verify { groupInfoUseCase.cancelJobs() }
    }
}
