package com.tokopedia.topads.dashboard.view.model

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupListUseCase
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase = mockk(relaxed = true)
    private val topAdsGetAdKeywordUseCase: TopAdsGetAdKeywordUseCase = mockk(relaxed = true)
    private val topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private val topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase = mockk(relaxed = true)
    private val topAdsGetStatisticsUseCase: TopAdsGetStatisticsUseCase = mockk(relaxed = true)
    private val topAdsKeywordsActionUseCase: TopAdsKeywordsActionUseCase = mockk(relaxed = true)
    private val topAdsGroupActionUseCase: TopAdsGroupActionUseCase = mockk(relaxed = true)
    private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase = mockk(relaxed = true)
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase = mockk(relaxed = true)
    private val groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val res: Resources = mockk(relaxed = true)


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
                userSession)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `get product data `() {
        viewModel.getGroupProductData(1, 1, "", "", 1, "", "", {}) {}
        verify {
            topAdsGetGroupProductDataUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get group info success`() {
        val expected = 10
        var actual = 0
        val data = GroupInfoResponse.TopAdsGetPromoGroup.Data(priceBid = expected)
        val onSuccess: (data: GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit = {
            actual = it.priceBid
        }
        every { groupInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        viewModel.getGroupInfo(res, "", onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `product stats success`() {
        val expected = 10
        var actual = 0
        val data = ProductStatisticsResponse(getDashboardProductStatistics = GetDashboardProductStatistics(listOf(WithoutGroupDataItem(adId = expected))))
        val onSuccess: (data: GetDashboardProductStatistics) -> Unit = {
            actual = it.data[0].adId
        }
        every { topAdsGetProductStatisticsUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data.getDashboardProductStatistics)
        }
        viewModel.getProductStats(res, "", "", listOf(), onSuccess, "", 1)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get keyword data of a group successfully`() {
        val expected = 10
        var actual = 0
        val data = KeywordsResponse(getTopadsDashboardKeywords = KeywordsResponse.GetTopadsDashboardKeywords
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
    fun `on product keyword count success`() {
        val expected = 10
        var actual = 0
        val data = CountDataItem(totalAds = expected)
        val onSuccess: (data: List<CountDataItem>) -> Unit = {
            actual = it[0].totalAds
        }
        every { topAdsGetProductKeyCountUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(listOf(data))
        }
        viewModel.getCountProductKeyword(res, listOf(), onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get topads stats`() {
        viewModel.getTopAdsStatistic(Date(), Date(), 1, {}, "")
        verify {
            topAdsGetStatisticsUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get group list`() {
        val expected = 10
        var actual = 0
        val data = GroupListDataItem(totalItem = expected)
        val onSuccess: (data: List<GroupListDataItem>) -> Unit = {
            actual = it[0].totalItem
        }
        every { topAdsGetGroupListUseCase.execute(any(), any()) } answers {
            onSuccess.invoke(listOf(data))
        }
        viewModel.getGroupList("", onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `set product action success`() {
        val expected = "add"
        var actual = ""
        val onSuccess: () -> Unit = {
            actual = expected
        }
        every { topAdsProductActionUseCase.execute(any(), any()) } answers {
            onSuccess.invoke()
        }
        viewModel.setProductAction(onSuccess, "", listOf(), res, "")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `set Group action`() {
        viewModel.setGroupAction("", listOf(), res)
        verify {
            topAdsGroupActionUseCase.execute(any(), any())
        }
    }

    @Test
    fun `set keyword action`() {
        val expected = 10
        var actual = 0
        val onSuccess: () -> Unit = {
            actual = expected
        }
        every {
            topAdsKeywordsActionUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            onSuccess.invoke()
        }
        viewModel.setKeywordAction("", listOf(), res, onSuccess)
        Assert.assertEquals(expected, actual)

    }

    @Test
    fun `check detach view`() {
        viewModel.onCleared()
        verify {
            topAdsGetAdKeywordUseCase.cancelJobs()
        }
    }
}