package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.*
import com.tokopedia.topads.common.domain.usecase.*
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.headline.data.Ad
import com.tokopedia.topads.headline.data.Data
import com.tokopedia.topads.headline.data.ShopAdInfo
import com.tokopedia.topads.headline.data.TopadsGetShopInfoV2
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsDashboardPresenterTest {

    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val shopAdInfoUseCase: GraphqlUseCase<ShopAdInfo> = mockk(relaxed = true)
    private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase = mockk(relaxed = true)
    private val topAdsGetGroupDataUseCase: TopAdsGetGroupDataUseCase = mockk(relaxed = true)
    private val topAdsGetGroupStatisticsUseCase: TopAdsGetGroupStatisticsUseCase = mockk(relaxed = true)
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase = mockk(relaxed = true)
    private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase = mockk(relaxed = true)
    private val topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase = mockk(relaxed = true)
    private val topAdsGroupActionUseCase: TopAdsGroupActionUseCase = mockk(relaxed = true)
    private val topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase = mockk(relaxed = true)
    private val topAdsInsightUseCase: TopAdsInsightUseCase = mockk(relaxed = true)
    private val getStatisticUseCase: GetStatisticUseCase = mockk(relaxed = true)
    private val budgetRecomUseCase: GraphqlUseCase<DailyBudgetRecommendationModel> = mockk(relaxed = true)
    private val productRecomUseCase: GraphqlUseCase<ProductRecommendationModel> = mockk(relaxed = true)
    private val topAdsEditUseCase: TopAdsEditUseCase = mockk(relaxed = true)
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase = mockk(relaxed = true)
    private val createGroupUseCase: CreateGroupUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase = mockk(relaxed = true)
    private val adsStatusUseCase: GraphqlUseCase<AdStatusResponse> = mockk(relaxed = true)
    private val autoAdsStatusUseCase: GraphqlUseCase<AutoAdsResponse> = mockk(relaxed = true)
    private val getExpiryDateUseCase: GraphqlUseCase<ExpiryDateResponse> = mockk(relaxed = true)
    private val getHiddenTrialUseCase: GraphqlUseCase<FreeTrialShopListResponse> = mockk(relaxed = true)
    private var userSession: UserSessionInterface = mockk(relaxed = true)
    private val res: Resources = mockk(relaxed = true)

    var view: TopAdsDashboardView = mockk(relaxed = true)
    private val presenter by lazy {
        TopAdsDashboardPresenter(topAdsGetShopDepositUseCase, shopAdInfoUseCase,
                gqlGetShopInfoUseCase, topAdsGetGroupDataUseCase,
                topAdsGetGroupStatisticsUseCase, topAdsGetProductStatisticsUseCase,
                topAdsGetProductKeyCountUseCase, topAdsGetGroupListUseCase,
                topAdsGroupActionUseCase, topAdsProductActionUseCase,
                topAdsGetGroupProductDataUseCase, topAdsInsightUseCase,
                getStatisticUseCase, budgetRecomUseCase,
                productRecomUseCase, topAdsEditUseCase,
                validGroupUseCase, createGroupUseCase,
                bidInfoUseCase, groupInfoUseCase, autoTopUpUSeCase, adsStatusUseCase,
                autoAdsStatusUseCase, getExpiryDateUseCase,
                getHiddenTrialUseCase, userSession)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `get shop deposit success`() {
        val expected = 10
        var actual = 0
        val dataDeposit = DepositAmount(amount = 10)
        val onSuccess: (dataDeposit: DepositAmount) -> Unit = {
            actual = it.amount
        }
        every { topAdsGetShopDepositUseCase.execute(captureLambda(), any()) } answers {
            onSuccess.invoke(dataDeposit)
        }
        presenter.getShopDeposit(onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get group data success`() {
        presenter.getGroupData(0, "", "", 1, "", "", 1) {}

        verify {
            topAdsGetGroupDataUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get overall stats success`() {

        presenter.getStatistic(Date(), Date(), 1, "", ) {}

        verify {
            getStatisticUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get group stats success`() {

        presenter.getGroupStatisticsData(1, "", "", 1, "", "", listOf()) {}

        verify {
            topAdsGetGroupStatisticsUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on product stats success`() {
        val expected = 10
        var actual = 0
        val res: Resources = mockk(relaxed = true)
        val data = GetDashboardProductStatistics(data = listOf(WithoutGroupDataItem(adId = expected)))
        val onSuccess: (data: GetDashboardProductStatistics) -> Unit = {
            actual = it.data[0].adId
        }
        every { topAdsGetProductStatisticsUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.getProductStats(res, ",", "", listOf(), "", 0, onSuccess)
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
        presenter.getCountProductKeyword(res, listOf(), onSuccess)
        Assert.assertEquals(expected, actual)
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
        presenter.getGroupList("", onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `set group action `() {
        presenter.setGroupAction({}, "", listOf(), res)
        verify {
            topAdsGroupActionUseCase.execute(any(), any())
        }
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
        presenter.setProductAction(onSuccess, "", listOf(), "")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get product data `() {
        presenter.getGroupProductData(1, 1, "", "", 1, "", "", {}) {}
        verify {
            topAdsGetGroupProductDataUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get insight data `() {
        presenter.getInsight(res) {}
        verify {
            topAdsInsightUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get shop info success`() {
        val expected = true
        var actual = false
        val data = ShopAdInfo(topadsGetShopInfoV2 = TopadsGetShopInfoV2(data = Data(listOf(Ad(isUsed = expected)))))
        val onSuccess: (data: ShopAdInfo) -> Unit = {
            actual = it.topadsGetShopInfoV2.data.ads[0].isUsed
        }
        every { shopAdInfoUseCase.execute(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.getShopAdsInfo(onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get expiray date success`() {

        presenter.getExpiryDate(res)
        verify {
            getExpiryDateUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get hidden trial list date success`() {
        presenter.getShopListHiddenTrial(res)
        verify {
            getHiddenTrialUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get ad status success`() {
        presenter.getAdsStatus(res)
        verify {
            adsStatusUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get auto ad status success`() {
        presenter.getAdsStatus(res)
        verify {
            adsStatusUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get auto topup value success`() {
        presenter.getAutoTopUpStatus(res, {})
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `get product recommendation success`() {
        val expected = "47"
        var actual = ""
        val data = ProductRecommendationModel(topadsGetProductRecommendation = TopadsGetProductRecommendation
        (data = ProductRecommendationData(nominalId = expected)))
        val onSuccess: (data: ProductRecommendationModel) -> Unit = {
            actual = it.topadsGetProductRecommendation.data.nominalId
        }
        every { productRecomUseCase.execute(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.getProductRecommendation(onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get daily budget recommendation success`() {
        val expected = "47"
        var actual = ""
        val data = DailyBudgetRecommendationModel(topadsGetDailyBudgetRecommendation =
        TopadsGetDailyBudgetRecommendation(data = listOf(DataBudget(groupId = expected))))
        val onSuccess: (data: DailyBudgetRecommendationModel) -> Unit = {
            actual = it.topadsGetDailyBudgetRecommendation.data[0].groupId
        }
        every { budgetRecomUseCase.execute(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.getDailyBudgetRecommendation(onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `edit budget insight`() {
        val expected = "haha"
        val data = FinalAdResponse.TopadsManageGroupAds(keywordResponse =
        FinalAdResponse.TopadsManageGroupAds.KeywordResponse(errors =
        listOf(FinalAdResponse.TopadsManageGroupAds.ErrorsItem(title = expected))))
        var actual = ""
        val onSuccess: (data: FinalAdResponse.TopadsManageGroupAds) -> Unit = {
            actual = it.keywordResponse.errors?.get(0)?.title ?: "haha"
        }
        every { topAdsEditUseCase.execute(any(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.editBudgetThroughInsight(mutableListOf(), hashMapOf(), onSuccess, {})
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `on group valid success`() {
        val expected = 10
        var actual = 0
        val data = ResponseGroupValidateName.TopAdsGroupValidateName(data = ResponseGroupValidateName.TopAdsGroupValidateName.Data(shopID = expected))
        val onSuccess: (dataDeposit: ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit = {
            actual = it.data.shopID
        }
        every { validGroupUseCase.execute(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.validateGroup("", onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `create group success`() {
        val expected = "haha"
        var actual = ""
        val data = ResponseCreateGroup.TopadsCreateGroupAds(meta = ResponseCreateGroup.Meta(messages = listOf(ResponseCreateGroup.MessagesItem(title = expected))))
        val onSuccess: (dataDeposit: ResponseCreateGroup.TopadsCreateGroupAds) -> Unit = {
            actual = it.meta.messages[0].title
        }
        every { createGroupUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.createGroup(hashMapOf(), onSuccess)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get bid info success`() {
        val expected = "10"
        var actual = "0"
        val data = listOf(TopadsBidInfo.DataItem(maxBid = expected))
        val onSuccess: (data: List<TopadsBidInfo.DataItem>) -> Unit = {
            actual = it[0].maxBid
        }
        every { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            onSuccess.invoke(data)
        }
        presenter.getBidInfo(listOf(), onSuccess)
        Assert.assertEquals(expected, actual)
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
        presenter.getGroupInfo(res, "", onSuccess)
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `check detach view`() {
        presenter.detachView()
        verify {
            topAdsGetShopDepositUseCase.cancelJobs()
        }
    }
}