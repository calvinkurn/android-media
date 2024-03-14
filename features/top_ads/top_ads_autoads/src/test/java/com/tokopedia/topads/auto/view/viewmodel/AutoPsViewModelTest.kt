@file:Suppress("DEPRECATION")

package com.tokopedia.topads.auto.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.domain.usecase.TopadsStatisticsEstimationAttributeUseCase
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.data.response.TopadsGetBudgetRecommendation
import com.tokopedia.topads.common.data.response.TopadsGetBudgetRecommendationResponse
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel
import com.tokopedia.topads.common.domain.usecase.GetVariantByIdUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetAutoAdsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase
import com.tokopedia.topads.common.domain.usecase.TopadsGetBudgetRecommendationUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AutoPsViewModelTest {

    @Suppress("DEPRECATION")
    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: AutoPsViewModel
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val topAdsQueryPostAutoadsUseCase: TopAdsQueryPostAutoadsUseCase = mockk(relaxed = true)
    private val getBudgetRecommendationUseCase: TopadsGetBudgetRecommendationUseCase =
        mockk(relaxed = true)
    private val topadsStatisticsEstimationAttributeUseCase: TopadsStatisticsEstimationAttributeUseCase =
        mockk(relaxed = true)
    private val topAdsGetAutoAdsUseCase: TopAdsGetAutoAdsUseCase = mockk(relaxed = true)
    private val getVariantByIdUseCase: GetVariantByIdUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val userSession: UserSession = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = spyk(
            AutoPsViewModel(
                testRule.dispatchers,
                userSession,
                topadsStatisticsEstimationAttributeUseCase,
                bidInfoUseCase,
                topAdsQueryPostAutoadsUseCase,
                getBudgetRecommendationUseCase,
                topAdsGetShopDepositUseCase,
                topAdsGetAutoAdsUseCase,
                getVariantByIdUseCase
            )
        )
    }

    @Test
    fun `test exception in loadData`() {
        coEvery {
            getBudgetRecommendationUseCase.executeOnBackground()
        } throws Throwable()
        coEvery {
            topAdsGetShopDepositUseCase.executeOnBackground()
        } throws Throwable()
        coEvery {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(Result<TopadsBidInfo>) -> Unit>().invoke(Fail(throwable = Throwable()))
        }
        coEvery {
            topadsStatisticsEstimationAttributeUseCase.execute(
                TopadsAutoPsConstants.STATISTICS_ESTIMATION_TYPE,
                TopadsAutoPsConstants.TOPADS_AUTO_PS_SOURCE
            )
        } throws Throwable()
        coEvery {
            topAdsGetAutoAdsUseCase.executeOnBackground()
        } throws Throwable()


        viewModel.loadData()

        assertEquals(null, viewModel.autoAdsData.value)
        assertEquals(null, viewModel.bidInfo.value)
        assertEquals(null, viewModel.topAdsGetAutoAds.value)
    }

    @Test
    fun `test exception in getVariantById`() {
        coEvery {
            getVariantByIdUseCase().getVariantById.shopIdVariants
        } answers {
            throw Throwable()
        }
        viewModel.getVariantById()
        assertEquals(
            emptyList<GetVariantByIdResponse.GetVariantById.ExperimentVariant>(),
            viewModel.shopVariant.value
        )
    }

    @Test
    fun `test exception in postAutoPs`() {
        val param = AutoAdsParam(
            AutoAdsParam.Input(
                TopadsAutoPsConstants.AUTO_PS_TOGGLE_OFF,
                TopadsAutoPsConstants.AUTO_PS_CHANNEL,
                10000,
                userSession.shopId,
                TopadsAutoPsConstants.AUTO_PS_SOURCE
            )
        )
        val data = Fail(throwable = Throwable())
        every { topAdsQueryPostAutoadsUseCase.executeQuery(param, captureLambda()) } answers {
            secondArg<(Result<TopAdsAutoAdsModel>) -> Unit>().invoke(data)
        }
        viewModel.postAutoPs(10000, TopadsAutoPsConstants.AUTO_PS_TOGGLE_OFF)
        assertEquals(data, viewModel.autoAdsData.value)
    }

    @Test
    fun `test result in loadData`() {
        val budgetRecommendation = TopadsGetBudgetRecommendationResponse(
            TopadsGetBudgetRecommendation(
                TopadsGetBudgetRecommendation.Data(1000, 10000, true),
                listOf()
            )
        )
        val deposit = Deposit(TopadsDashboardDeposits(DepositAmount(5000)))
        val estimation = EstimationResponse(
            EstimationResponse.TopadsStatisticsEstimationAttribute(
                listOf(EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem(0))
            )
        )
        val autoAds = AutoAdsResponse.TopAdsGetAutoAds()

        coEvery { getBudgetRecommendationUseCase.executeOnBackground() } returns budgetRecommendation
        coEvery { topAdsGetShopDepositUseCase.executeOnBackground() } returns deposit
        coEvery { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(ResponseBidInfo.Result(TopadsBidInfo()))
        }
        coEvery {
            topadsStatisticsEstimationAttributeUseCase.execute(
                TopadsAutoPsConstants.STATISTICS_ESTIMATION_TYPE,
                TopadsAutoPsConstants.TOPADS_AUTO_PS_SOURCE
            )
        } returns estimation
        coEvery { topAdsGetAutoAdsUseCase.executeOnBackground() } returns autoAds

        viewModel.loadData()

        assertEquals(autoAds, viewModel.topAdsGetAutoAds.value)
    }


    @Test
    fun `test result in postAutoPs`() {
        val param = AutoAdsParam(
            AutoAdsParam.Input(
                TopadsAutoPsConstants.AUTO_PS_TOGGLE_OFF,
                TopadsAutoPsConstants.AUTO_PS_CHANNEL,
                10000,
                userSession.shopId,
                TopadsAutoPsConstants.AUTO_PS_SOURCE
            )
        )
        val data = Success(TopAdsAutoAdsModel(shopId = userSession.shopId))
        every { topAdsQueryPostAutoadsUseCase.executeQuery(param, captureLambda()) } answers {
            secondArg<(Result<TopAdsAutoAdsModel>) -> Unit>().invoke(data)
        }
        viewModel.postAutoPs(10000, TopadsAutoPsConstants.AUTO_PS_TOGGLE_OFF)
        assertEquals(data, viewModel.autoAdsData.value)
    }


    @Test
    fun `test more than zero  in getPotentialImpression`() {
        val expected = "5,000"
        val budget = "5000"
        val result = viewModel.getPotentialImpression(budget.toInt())
        assertEquals(expected, result)
    }

    @Test
    fun `test less than zero  in getPotentialImpression`() {
        val expected = "-5,000"
        val budget = "-5000"
        val result = viewModel.getPotentialImpression(budget.toInt())
        assertEquals(expected, result)
    }

    @Test
    fun `test zero  in getPotentialImpression`() {
        val expected = "0"
        val budget = "0"
        val result = viewModel.getPotentialImpression(budget.toInt())
        assertEquals(expected, result)
    }

    @Test
    fun `test false in checkDeposits`() {
        val expected = false
        val result = viewModel.checkDeposits()
        assertEquals(expected, result)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}
