package com.tokopedia.topads.dashboard.view.presenter

/*
** This file is temporarily commented out due to the issue of unit test stuck forever on jenkins and local for top_ads_dashboard module

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.common.data.response.groupitem.GroupStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.*
import com.tokopedia.topads.common.domain.usecase.*
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.headline.data.ShopAdInfo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.math.exp

class DashboardPresenterTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val topAdsGetShopDepositUseCase = mockk<TopAdsGetDepositUseCase>(relaxed = true)
    private val shopAdInfoUseCase = mockk<GraphqlUseCase<ShopAdInfo>>(relaxed = true)
    private val gqlGetShopInfoUseCase = mockk<GQLGetShopInfoUseCase>(relaxed = true)
    private val topAdsGetGroupDataUseCase = mockk<TopAdsGetGroupDataUseCase>(relaxed = true)
    private val topAdsGetGroupStatisticsUseCase =
        mockk<TopAdsGetGroupStatisticsUseCase>(relaxed = true)
    private val topAdsGetProductStatisticsUseCase =
        mockk<TopAdsGetProductStatisticsUseCase>(relaxed = true)
    private val topAdsGetProductKeyCountUseCase =
        mockk<TopAdsGetProductKeyCountUseCase>(relaxed = true)
    private val topAdsGetGroupListUseCase = mockk<TopAdsGetGroupListUseCase>(relaxed = true)
    private val topAdsGroupActionUseCase = mockk<TopAdsGroupActionUseCase>(relaxed = true)
    private val topAdsProductActionUseCase = mockk<TopAdsProductActionUseCase>(relaxed = true)
    private val topAdsGetGroupProductDataUseCase =
        mockk<TopAdsGetGroupProductDataUseCase>(relaxed = true)
    private val topAdsInsightUseCase = mockk<TopAdsInsightUseCase>(relaxed = true)
    private val getStatisticUseCase = mockk<TopAdsGetStatisticsUseCase>(relaxed = true)
    private val budgetRecomUseCase =
        mockk<GraphqlUseCase<DailyBudgetRecommendationModel>>(relaxed = true)
    private val productRecomUseCase =
        mockk<GraphqlUseCase<ProductRecommendationModel>>(relaxed = true)
    private val validGroupUseCase = mockk<TopAdsGroupValidateNameUseCase>(relaxed = true)
    private val topAdsCreateUseCase = mockk<TopAdsCreateUseCase>(relaxed = true)
    private val bidInfoUseCase = mockk<BidInfoUseCase>(relaxed = true)
    private val groupInfoUseCase = mockk<GroupInfoUseCase>(relaxed = true)
    private val autoTopUpUSeCase = mockk<TopAdsAutoTopUpUSeCase>(relaxed = true)
    private val adsStatusUseCase = mockk<GraphqlUseCase<AdStatusResponse>>(relaxed = true)
    private val autoAdsStatusUseCase = mockk<GraphqlUseCase<AutoAdsResponse>>(relaxed = true)
    private val getExpiryDateUseCase = mockk<GraphqlUseCase<ExpiryDateResponse>>(relaxed = true)
    private val getHiddenTrialUseCase =
        mockk<GraphqlUseCase<FreeTrialShopListResponse>>(relaxed = true)
    private val whiteListedUserUseCase = mockk<GetWhiteListedUserUseCase>(relaxed = true)
    private val topAdsGetDeletedAdsUseCase = mockk<TopAdsGetDeletedAdsUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val throwable: Throwable = mockk(relaxed = true)
    private val view: TopAdsDashboardView = mockk(relaxed = true)
    private val res = mockk<Resources>(relaxed = true)
    private val presenter by lazy {
        TopAdsDashboardPresenter(
            topAdsGetShopDepositUseCase,
            shopAdInfoUseCase,
            gqlGetShopInfoUseCase,
            topAdsGetGroupDataUseCase,
            topAdsGetGroupStatisticsUseCase,
            topAdsGetProductStatisticsUseCase,
            topAdsGetProductKeyCountUseCase,
            topAdsGetGroupListUseCase,
            topAdsGroupActionUseCase,
            topAdsProductActionUseCase,
            topAdsGetGroupProductDataUseCase,
            topAdsInsightUseCase,
            getStatisticUseCase,
            budgetRecomUseCase,
            productRecomUseCase,
            validGroupUseCase,
            topAdsCreateUseCase,
            bidInfoUseCase,
            groupInfoUseCase,
            autoTopUpUSeCase,
            adsStatusUseCase,
            autoAdsStatusUseCase,
            getExpiryDateUseCase,
            getHiddenTrialUseCase,
            whiteListedUserUseCase,
            topAdsGetDeletedAdsUseCase,
            userSession,
        )
    }
    private val param: RequestParams = mockk(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
        every { userSession.shopId } returns "123"
    }

    @Test
    fun `setProductActionMoveGroup success test`() {
        var successCalled = false
        val fakeParam: RequestParams = mockk(relaxed = true)

        every {
            topAdsCreateUseCase.createRequestParamMoveGroup(any(),
                TopAdsDashboardConstant.SOURCE_DASH, any(), ParamObject.ACTION_ADD)
        } returns fakeParam

        presenter.setProductActionMoveGroup("", listOf()) { successCalled = true }

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

        presenter.setProductActionMoveGroup("", listOf()) { successCalled = true }

        verify { throwable.printStackTrace() }
        Assert.assertEquals(successCalled, false)
    }

    @Test
    fun `getGroupData success check`() {
        val expected = GroupItemResponse()
        var actual: GroupItemResponse.GetTopadsDashboardGroups? = null

        coEvery { topAdsGetGroupDataUseCase.execute(any()) } returns expected

        presenter.getGroupData(0, "", "", 1, "", "", 1) { actual = it }

        Assert.assertEquals(expected.getTopadsDashboardGroups, actual)
    }

    @Test
    fun `getGroupData error check`() {
        coEvery { topAdsGetGroupDataUseCase.execute(any()) } throws throwable

        presenter.getGroupData(0, "", "", 1, "", "", 1) {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getStatistic success`() {
        val expected = StatsData()
        var actual: DataStatistic? = null

        coEvery { getStatisticUseCase.execute(any()) } returns expected

        presenter.getStatistic(Date(), Date(), 1, "") { actual = it }

        Assert.assertEquals(expected.topadsDashboardStatistics.data, actual)
    }

    @Test
    fun `getStatistic error check`() {
        coEvery { getStatisticUseCase.execute(any()) } throws throwable

        presenter.getStatistic(Date(), Date(), 1, "") {}

        verify { view.onErrorGetStatisticsInfo(throwable) }
    }


    @Test
    fun `getGroupStatisticsData success`() {
        val expected = GroupStatisticsResponse()
        var actual: GetTopadsDashboardGroupStatistics? = null

        coEvery { topAdsGetGroupStatisticsUseCase.execute(any()) } returns expected

        presenter.getGroupStatisticsData(1, "", "", 1, "", "", listOf()) { actual = it }

        Assert.assertEquals(expected.getTopadsDashboardGroupStatistics, actual)
    }

    @Test
    fun `getGroupStatisticsData error check`() {
        coEvery { topAdsGetGroupStatisticsUseCase.execute(any()) } throws throwable

        presenter.getGroupStatisticsData(1, "", "", 1, "", "", listOf()) {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getProductStats success`() {
        val expected = ProductStatisticsResponse()
        var actual: GetDashboardProductStatistics? = null

        every {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(ProductStatisticsResponse) -> Unit>().invoke(expected)
        }

        presenter.getProductStats(res, ",", "", listOf(), "", 0) { actual = it }

        Assert.assertEquals(expected.getDashboardProductStatistics, actual)
    }

    @Test
    fun `getProductStats error check`() {
        coEvery {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        presenter.getProductStats(res, ",", "", listOf(), "", 0) {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `on product keyword count success`() {
        val expected = 10
        var actual = 0

        val onSuccess: (data: List<CountDataItem>) -> Unit = {
            actual = it[0].totalAds
        }
        every {
            topAdsGetProductKeyCountUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(TotalProductKeyResponse) -> Unit>().invoke(TotalProductKeyResponse(
                TopAdsGetTotalAdsAndKeywords(listOf(CountDataItem(totalAds = expected)))))
        }

        presenter.getCountProductKeyword(res, listOf(), onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getCountProductKeyword error check`() {
        coEvery {
            topAdsGetProductKeyCountUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        presenter.getCountProductKeyword(res, listOf()) {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getGroupList list`() {
        val expected = DashGroupListResponse()
        var actual: List<GroupListDataItem>? = null

        val onSuccess: (data: List<GroupListDataItem>) -> Unit = {
            actual = it
        }
        coEvery { topAdsGetGroupListUseCase.execute(any()) } returns expected

        presenter.getGroupList("", onSuccess)
        Assert.assertEquals(expected.getTopadsDashboardGroups.data, actual)
    }

    @Test
    fun `setGroupAction success`() {
        val expected = "hiii"
        var actual: String? = null

        coEvery { topAdsGroupActionUseCase.execute(any(), any()) } returns
                GroupActionResponse(GroupActionResponse.TopAdsEditGroupBulk(GroupActionResponse.TopAdsEditGroupBulk.Data(
                    expected), emptyList()))

        presenter.setGroupAction({
            actual = it
        }, "", listOf(), res)

        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `setGroupAction empty data check`() {
        val expected = "hiii"

        coEvery { topAdsGroupActionUseCase.execute(any(), any()) } returns
                GroupActionResponse(GroupActionResponse.TopAdsEditGroupBulk(errors = listOf(
                    ErrorsItem(detail = expected))))

        presenter.setGroupAction({}, "", listOf(), res)

        verify { view.onError(expected) }
    }

    @Test
    fun `setGroupAction error check`() {
        coEvery {
            topAdsGroupActionUseCase.execute(any(), any())
        } throws throwable

        presenter.setGroupAction({}, "", listOf(), res)

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `setProductAction success`() {
        var successCalled = false
        coEvery { topAdsProductActionUseCase.execute(any()) } returns mockk()
        presenter.setProductAction({
            successCalled = true
        }, "", listOf(), "")

        Assert.assertTrue(successCalled)
    }

    @Test
    fun `setProductAction error check`() {
        coEvery { topAdsProductActionUseCase.execute(any()) } throws throwable

        presenter.setProductAction({}, "", listOf(), "")

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getGroupProductData empty check`() {
        var emptyCalled = false

        coEvery { topAdsGetGroupProductDataUseCase.execute(any()) } returns NonGroupResponse()

        presenter.getGroupProductData(1, 1, "", "", 1, "", "", 0, {}) { emptyCalled = true }

        Assert.assertTrue(emptyCalled)
    }

    @Test
    fun `getGroupProductData success check`() {
        val expected = NonGroupResponse(NonGroupResponse.TopadsDashboardGroupProducts(data = listOf(
            WithoutGroupDataItem())))
        var actual: NonGroupResponse.TopadsDashboardGroupProducts? = null

        coEvery { topAdsGetGroupProductDataUseCase.execute(any()) } returns expected

        presenter.getGroupProductData(1, 1, "", "", 1, "", "", 0, { actual = it }) { }

        Assert.assertEquals(expected.topadsDashboardGroupProducts, actual)
    }

    @Test
    fun `getGroupProductData error check`() {
        var emptyCalled = false

        coEvery { topAdsGetGroupProductDataUseCase.execute(any()) } throws throwable

        presenter.getGroupProductData(1, 1, "", "", 1, "", "", 0, {}) { emptyCalled = true }

        Assert.assertTrue(emptyCalled)
    }

    @Test
    fun `getInsight success`() {
        val expected = InsightKeyData()
        var actual: InsightKeyData? = null

        coEvery { topAdsInsightUseCase.execute(any(), any()) } returns expected
        presenter.getInsight(res) {
            actual = it
        }

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getInsight failure`() {

        coEvery { topAdsInsightUseCase.execute(any(), any()) } throws throwable
        presenter.getInsight(res) {}

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getShopAdsInfo success`() {
        val expected = ShopAdInfo()
        var actual: ShopAdInfo? = null

        val onSuccess: (data: ShopAdInfo) -> Unit = {
            actual = it
        }
        every { shopAdInfoUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(ShopAdInfo) -> Unit>().invoke(expected)
        }

        presenter.getShopAdsInfo(onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getShopAdsInfo error`() {

        every { shopAdInfoUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        var successCalled = false
        presenter.getShopAdsInfo { successCalled = true }

        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `get expiray date success`() {
        val expected = "iot"

        every {
            getExpiryDateUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(ExpiryDateResponse) -> Unit>().invoke(ExpiryDateResponse(ExpiryDateResponse.TopAdsGetFreeDeposit(
                expected)))
        }

        presenter.getExpiryDate(res)
        Assert.assertEquals(presenter.expiryDateHiddenTrial.value, expected)
    }

    @Test
    fun `get expiry date error`() {
        every {
            getExpiryDateUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        presenter.getExpiryDate(res)

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getShopListHiddenTrial should invoke true if hiddenTrial feature id present`() {

        every {
            getHiddenTrialUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(FreeTrialShopListResponse) -> Unit>().invoke(
                FreeTrialShopListResponse(FreeTrialShopListResponse.TopAdsGetShopWhitelistedFeature(
                    listOf(FreeTrialShopListResponse.TopAdsGetShopWhitelistedFeature.DataItem(
                        featureID = TopAdsDashboardPresenter.HIDDEN_TRIAL_FEATURE))
                ))
            )
        }

        presenter.getShopListHiddenTrial(res)

        Assert.assertTrue(presenter.isShopWhiteListed.value == true)
    }

    @Test
    fun `getShopListHiddenTrial should invoke false if not hiddenTrial feature id present`() {

        every {
            getHiddenTrialUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(FreeTrialShopListResponse) -> Unit>().invoke(
                FreeTrialShopListResponse(FreeTrialShopListResponse.TopAdsGetShopWhitelistedFeature(
                    listOf(FreeTrialShopListResponse.TopAdsGetShopWhitelistedFeature.DataItem(
                        featureID = -1))
                ))
            )
        }

        presenter.getShopListHiddenTrial(res)

        Assert.assertTrue(presenter.isShopWhiteListed.value == false)
    }

    @Test
    fun `getShopListHiddenTrial error`() {

        every {
            getHiddenTrialUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        presenter.getShopListHiddenTrial(res)
        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getAdsStatus success`() {
        val expected = AdStatusResponse()

        every {
            adsStatusUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(AdStatusResponse) -> Unit>().invoke(expected)
        }

        presenter.getAdsStatus(res)
        verify { view.onSuccessAdStatus(expected.topAdsGetShopInfo.data) }
    }

    @Test
    fun `getAdsStatus error`() {
        every {
            adsStatusUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        presenter.getAdsStatus(res)

        verify(exactly = 0) { view.onSuccessAdStatus(any()) }
    }

    @Test
    fun `get auto ad status success`() {
        val expected = AutoAdsResponse()
        var actual: AutoAdsResponse.TopAdsGetAutoAds.Data? = null

        every {
            autoAdsStatusUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(AutoAdsResponse) -> Unit>().invoke(expected)
        }

        presenter.getAutoAdsStatus(res) { actual = it }
        Assert.assertEquals(expected.topAdsGetAutoAds.data, actual)
    }

    @Test
    fun `getAutoAdsStatus error`() {
        every {
            adsStatusUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        var successCalled = false
        presenter.getAutoAdsStatus(res) { successCalled = true }
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `getProductRecommendation success`() {
        val expected = ProductRecommendationModel()
        var actual: ProductRecommendationModel? = null

        every { productRecomUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(ProductRecommendationModel) -> Unit>().invoke(expected)
        }

        presenter.getProductRecommendation { actual = it }
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getProductRecommendation error`() {
        every { productRecomUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        var successCalled = false
        presenter.getProductRecommendation { successCalled = true }
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `getDailyBudgetRecommendation success`() {
        val expected = DailyBudgetRecommendationModel()
        var actual: DailyBudgetRecommendationModel? = null

        every { budgetRecomUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(DailyBudgetRecommendationModel) -> Unit>().invoke(expected)
        }
        presenter.getDailyBudgetRecommendation { actual = it }
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getDailyBudgetRecommendation error`() {
        every { budgetRecomUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        var successCalled = false
        presenter.getDailyBudgetRecommendation { successCalled = true }
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `editBudgetThroughInsight success`() {
        val param: RequestParams = mockk()
        val expected = FinalAdResponse()
        var actual: FinalAdResponse.TopadsManageGroupAds? = null

        coEvery { topAdsCreateUseCase.execute(param) } returns expected
        coEvery {
            topAdsCreateUseCase.createRequestParamEditBudgetInsight(any(),
                any(), any(), any())
        } returns param

        presenter.editBudgetThroughInsight(mutableListOf(), 0f, "", 0.0, { actual = it }, {})

        Assert.assertEquals(expected.topadsManageGroupAds, actual)
    }

    @Test
    fun `editBudgetThroughInsight error check`() {
        coEvery {
            topAdsCreateUseCase.createRequestParamEditBudgetInsight(any(),
                any(),
                any(),
                any())
        } throws throwable

        presenter.editBudgetThroughInsight(mutableListOf(), 0f, "", 0.0, {}, {})

        verify { throwable.printStackTrace() }
    }

    @Test
    fun `validateGroup success`() {
        val expected = ResponseGroupValidateName()
        var actual: ResponseGroupValidateName.TopAdsGroupValidateName? = null

        every { validGroupUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(expected)
        }
        presenter.validateGroup("") { actual = it }
        Assert.assertEquals(expected.topAdsGroupValidateName, actual)
    }

    @Test
    fun `validateGroup error`() {

        every { validGroupUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        presenter.validateGroup("") {}
        verify { throwable.printStackTrace() }
    }


    @Test
    fun `create group success`() {
        var error: String? = ""
        every {
            topAdsCreateUseCase.createRequestParamActionCreate(mutableListOf(),
                "", 0.0, 0.0)
        } returns param

        presenter.createGroup(listOf(), "", 0.0, 0.0) { error = it }

        coVerify { topAdsCreateUseCase.execute(param) }

        Assert.assertEquals(error, null)
    }

    @Test
    fun `get bid info success`() {
        val expected = ResponseBidInfo.Result()
        var actual: List<TopadsBidInfo.DataItem>? = null
        every { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(expected)
        }
        presenter.getBidInfo(listOf()) { actual = it }
        Assert.assertEquals(expected.topadsBidInfo.data, actual)
    }

    @Test
    fun `get bid info error`() {
        every { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        var successCalled = false
        presenter.getBidInfo(listOf()) { successCalled = true }
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `getGroupInfo success`() {
        val expected = GroupInfoResponse()
        var actual: GroupInfoResponse.TopAdsGetPromoGroup.Data? = null

        every { groupInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(GroupInfoResponse) -> Unit>().invoke(expected)
        }
        presenter.getGroupInfo(res, "", "") { actual = it }
        Assert.assertEquals(expected.topAdsGetPromoGroup!!.data, actual)
    }

    @Test
    fun `getGroupInfo error`() {
        every { groupInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        presenter.getGroupInfo(res, "", "") {}
        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getWhiteListedUser success`() {
        val expected = WhiteListUserResponse.TopAdsGetShopWhitelistedFeature()
        var actual: WhiteListUserResponse.TopAdsGetShopWhitelistedFeature? = null

        val isFinished: () -> Unit = spyk()
        every { whiteListedUserUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) -> Unit>().invoke(
                expected)
        }

        presenter.getWhiteListedUser({ actual = it }, isFinished)
        Assert.assertEquals(expected, actual)
        verify { isFinished() }
    }

    @Test
    fun `getWhiteListedUser error`() {
        every { whiteListedUserUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        presenter.getWhiteListedUser({}, {})
        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getDeletedAds test`() {
        val onSuccess: (TopAdsDeletedAdsResponse) -> Unit = {}
        val onEmptyResult: () -> Unit = {}
        presenter.getDeletedAds(0, "", "", "", onSuccess, onEmptyResult)
        verify { topAdsGetDeletedAdsUseCase.execute(onSuccess, onEmptyResult) }
    }

    @Test
    fun `check detach view`() {
        presenter.detachView()
        verify {
            topAdsGetShopDepositUseCase.cancelJobs()
        }
    }

}

* */