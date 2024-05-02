@file:Suppress("DEPRECATION")

package com.tokopedia.topads.auto.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.view.RequestHelper
import com.tokopedia.topads.auto.view.fragment.AutoAdsBaseBudgetFragment
import com.tokopedia.topads.auto.view.fragment.AutoAdsBaseBudgetFragment.Companion.CHANNEL
import com.tokopedia.topads.auto.view.fragment.AutoAdsBaseBudgetFragment.Companion.TOGGLE_OFF
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.json.JSONException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Method

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DailyBudgetViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DailyBudgetViewModel
    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)

    private lateinit var queryPostAutoadsUseCase: TopAdsQueryPostAutoadsUseCase
    private lateinit var bidInfoUseCase: BidInfoUseCase
    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private val rawQueries: Map<String, String> = mapOf()
    private lateinit var getParamsMethod: Method

    @Before
    fun setUp() {
        queryPostAutoadsUseCase = mockk(relaxed = true)
        repository = mockk()
        context = mockk()
        bidInfoUseCase = mockk(relaxed = true)
        viewModel = spyk(
            DailyBudgetViewModel(
                context, testRule.dispatchers, repository,
                rawQueries, topAdsGetShopDepositUseCase, bidInfoUseCase, queryPostAutoadsUseCase
            )
        )
        getParamsMethod = DailyBudgetViewModel::class.java.getDeclaredMethod(
            "getParams",
            AutoAdsParam::class.java
        )
        getParamsMethod.isAccessible = true
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }


    @Test
    fun `test exception in getBudgetInfo`() {
        val t = Exception(String.EMPTY)
        var data: ResponseBidInfo.Result? = null
        coEvery { repository.response(any(), any()) } throws t
        coEvery {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            secondArg<(Result<ResponseBidInfo.Result>) -> Unit>().invoke(Fail(throwable = t))
        }
        viewModel.getBudgetInfo(String.EMPTY, String.EMPTY) {
            data = it
        }
        assertEquals(null, data)
    }

    @Test
    fun `test fail in getBudgetInfo`() {
        val t = Exception(String.EMPTY)
        var data: ResponseBidInfo.Result? = null

        coEvery { repository.response(any(), any()) } throws t

        coEvery {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(t)
        }
        viewModel.getBudgetInfo(String.EMPTY, String.EMPTY) {
            data = it
        }
        assertEquals(null, data)
    }

    @Test
    fun `test result in getBudgetInfo`() {
        val expected = 2
        var actual = 0
        val bidInfoData: ResponseBidInfo.Result =
            ResponseBidInfo.Result(
                TopadsBidInfo(
                    data =
                    listOf(TopadsBidInfo.DataItem(shopStatus = expected))
                )
            )
        val onSuccess: (ResponseBidInfo.Result) -> Unit = {
            actual = it.topadsBidInfo.data[Int.ZERO].shopStatus
        }
        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(bidInfoData)
        }
        viewModel.getBudgetInfo(String.EMPTY, String.EMPTY, onSuccess)
        assertEquals(expected, actual)
    }

    @Test
    fun `test exception in postAutoAds`() {
        val param = AutoAdsParam(
            AutoAdsParam.Input(
                TOGGLE_OFF, CHANNEL, Int.ZERO,
                String.EMPTY, AutoAdsBaseBudgetFragment.SOURCE
            )
        )
        val data = Fail(throwable = Throwable())
        every {
            queryPostAutoadsUseCase.executeQuery(param, captureLambda())
        } answers {
            secondArg<(Result<TopAdsAutoAdsModel>) -> Unit>().invoke(data)
        }
        viewModel.postAutoAds(param)
        assertEquals(data, viewModel.autoAdsData.value)
    }

    @Test
    fun `test result in postAutoAds`() {
        val param = AutoAdsParam(
            AutoAdsParam.Input(
                TOGGLE_OFF, CHANNEL, Int.ZERO,
                String.EMPTY, AutoAdsBaseBudgetFragment.SOURCE
            )
        )
        val data = Success(TopAdsAutoAdsModel(shopId = String.EMPTY))
        every {
            queryPostAutoadsUseCase.executeQuery(param, captureLambda())
        } answers {
            secondArg<(Result<TopAdsAutoAdsModel>) -> Unit>().invoke(data)
        }
        viewModel.postAutoAds(param)
        assertEquals(data, viewModel.autoAdsData.value)
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        val expected = Deposit(TopadsDashboardDeposits(DepositAmount(amount = Int.ZERO)))
        coEvery {
            topAdsGetShopDepositUseCase.executeOnBackground()
        } returns expected
        viewModel.getTopAdsDeposit()
        coVerify { topAdsGetShopDepositUseCase.executeOnBackground() }
        assertEquals(
            viewModel.getTopAdsDepositLiveData().value,
            expected.topadsDashboardDeposits.data.amount
        )
    }

    @Test
    fun `test exception in getTopAdsDeposit`() {
        coEvery {
            topAdsGetShopDepositUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getTopAdsDeposit()
        assertEquals(viewModel.getTopAdsDepositLiveData().value, null)
    }

    @Test
    fun `test exception in topadsStatisticsEstimationPotentialReach`() {
        val t = Exception(String.EMPTY)
        val expected = null
        var actual: EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem? = null

        coEvery { repository.response(any(), any()) } throws t
        viewModel.topadsStatisticsEstimationPotentialReach({
            actual = it
        }, String.EMPTY, String.EMPTY)
        assertEquals(expected, actual)
    }

    @Test
    fun `test result in topadsStatisticsEstimationPotentialReach`() {
        val data =
            EstimationResponse.TopadsStatisticsEstimationAttribute(
                listOf(
                    EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem(
                        type = Int.ZERO
                    )
                )
            )
        var actual: EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem? = null
        val expected = Int.ZERO
        val successData: EstimationResponse = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)
        coEvery { repository.response(any(), any()) } returns response
        every { response.getError(EstimationResponse::class.java) } returns listOf()
        every { response.getData<EstimationResponse>(EstimationResponse::class.java) } returns successData
        every { successData.topadsStatisticsEstimationAttribute } returns data
        viewModel.topadsStatisticsEstimationPotentialReach({
            actual = it
        }, String.EMPTY, String.EMPTY)
        assertEquals(expected, actual?.type)
    }

    @Test
    fun `test getPotentialImpressionGQL`() {
        val expected = "2"
        val actual = viewModel.getPotentialImpressionGQL(5, 2)
        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than or equals to zero`() {
        val expected = String.EMPTY
        every { context.getString(any()) } returns expected
        val actual = viewModel.checkBudget(-2.0, 3.0, 10.0)
        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than minDailyBudget`() {
        val expected = String.EMPTY
        every { context.getString(any()) } returns expected
        val actual = viewModel.checkBudget(1.0, 3.0, 10.0)
        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is greater than maxDailyBudget`() {
        val expected = String.EMPTY
        every { context.getString(any()) } returns expected
        val actual = viewModel.checkBudget(11.0, 3.0, 10.0)
        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than maxDailyBudget and greater than minDailyBudget`() {
        val expected = String.EMPTY
        every { context.getString(any(), any()) } returns expected
        val actual = viewModel.checkBudget(5.0, 3.0, 10.0)
        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than maxDailyBudget and greater than minDailyBudget and not mutiple of 1000`() {
        val expected = null
        val actual = viewModel.checkBudget(5000.0, 3.0, 10000.0)
        assertEquals(expected, actual)
    }

    @Test
    fun `getParams should return correct RequestParams`() {
        val params: RequestParams = mockk(relaxed = true)
        val autoAdsParam = AutoAdsParam(
            input = AutoAdsParam.Input(
                action = String.EMPTY,
                channel = String.EMPTY,
                dailyBudget = Int.ZERO,
                shopID = String.EMPTY,
                source = String.EMPTY
            )
        )
        every {
            params.putAll(any())
        } answers {
            firstArg<Map<String, Any>>()
        }
        val actualParams = getParamsMethod.invoke(viewModel, autoAdsParam)
        assertTrue(actualParams is RequestParams)
    }

    @Test
    fun `getParams should enter catch block when json conversion fails`() {
        val autoAdsParam = AutoAdsParam(
            input = AutoAdsParam.Input(
                action = String.EMPTY,
                channel = String.EMPTY,
                dailyBudget = Int.ZERO,
                shopID = String.EMPTY,
                source = String.EMPTY
            )
        )
        mockkStatic(Utils::class)
        every { Utils.jsonToMap(any()) } throws JSONException(String.EMPTY)
        val actualParams = getParamsMethod.invoke(viewModel, autoAdsParam)
        assertTrue(actualParams is RequestParams)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}
