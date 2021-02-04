package com.tokopedia.topads.dashboard.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.domain.interactor.*
import com.tokopedia.topads.common.domain.usecase.*
import com.tokopedia.topads.dashboard.data.model.DailyBudgetRecommendationModel
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.headline.data.ShopAdInfo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TopAdsDashboardPresenterTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private var shopAdInfoUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ShopAdInfo> = mockk(relaxed = true)
    private var gqlGetShopInfoUseCase: GQLGetShopInfoUseCase = mockk(relaxed = true)
    private var topAdsGetGroupDataUseCase: TopAdsGetGroupDataUseCase = mockk(relaxed = true)
    private var topAdsGetGroupStatisticsUseCase: TopAdsGetGroupStatisticsUseCase = mockk(relaxed = true)
    private var topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase = mockk(relaxed = true)
    private var topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase = mockk(relaxed = true)
    private var topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase = mockk(relaxed = true)
    private var topAdsGroupActionUseCase: TopAdsGroupActionUseCase = mockk(relaxed = true)
    private var topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private var topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase = mockk(relaxed = true)
    private var topAdsInsightUseCase: TopAdsInsightUseCase = mockk(relaxed = true)
    private var getStatisticUseCase: GetStatisticUseCase = mockk(relaxed = true)
    private var budgetRecomUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<DailyBudgetRecommendationModel> = mockk(relaxed = true)
    private var productRecomUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductRecommendationModel> = mockk(relaxed = true)
    private var topAdsEditUseCase: TopAdsEditUseCase = mockk(relaxed = true)
    private var validGroupUseCase: TopAdsGroupValidateNameUseCase = mockk(relaxed = true)
    private var createGroupUseCase: CreateGroupUseCase = mockk(relaxed = true)
    private var bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private var groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private var userSession: UserSessionInterface = mockk(relaxed = true)

    var view: TopAdsDashboardView = mockk(relaxed = true)
    private val presenter by lazy {
        TopAdsDashboardPresenter(topAdsGetShopDepositUseCase, shopAdInfoUseCase, gqlGetShopInfoUseCase, topAdsGetGroupDataUseCase, topAdsGetGroupStatisticsUseCase, topAdsGetProductStatisticsUseCase, topAdsGetProductKeyCountUseCase, topAdsGetGroupListUseCase, topAdsGroupActionUseCase, topAdsProductActionUseCase, topAdsGetGroupProductDataUseCase, topAdsInsightUseCase, getStatisticUseCase, budgetRecomUseCase, productRecomUseCase, topAdsEditUseCase, validGroupUseCase, createGroupUseCase, bidInfoUseCase, groupInfoUseCase, userSession)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `get shop deposit success`(){
        val expected = 10
        var actual = 0
        val dataDeposit = DepositAmount(amount = 10)
        val onSuccess:(dataDeposit: DepositAmount) -> Unit = {
            actual = it.amount
        }
        every { topAdsGetShopDepositUseCase.execute(captureLambda(),any()) }answers {
            onSuccess.invoke(dataDeposit)
        }

        presenter.getShopDeposit { onSuccess }
        Assert.assertEquals(expected,actual)

    }
}