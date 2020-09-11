package com.tokopedia.topads.auto.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.auto.data.entity.BidInfoData
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.auto.data.network.response.TopAdsDepositResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfo
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.view.AutoAdsTestDispatcherProvider
import com.tokopedia.topads.auto.view.RequestHelper
import com.tokopedia.topads.auto.view.fragment.AutoAdsBaseBudgetFragment
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DailyBudgetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DailyBudgetViewModel

    private lateinit var dispatcher: AutoAdsDispatcherProvider
    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private val rawQueries: Map<String, String> = mapOf()

    @Before
    fun setUp() {
        dispatcher = AutoAdsTestDispatcherProvider()
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mockk()
        context = mockk()
        viewModel = spyk(DailyBudgetViewModel(context, dispatcher, repository, rawQueries))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }


    @Test
    fun `test exception in getBudgetInfo`() {
        val t = Exception("my excep")
        var data: TopadsBidInfo.Response? = null

        coEvery { repository.getReseponse(any(), any()) } throws t

        viewModel.getBudgetInfo(0, "reqType", "source") {
            data = it
        }

        assertEquals(null, data)
    }


    @Test
    fun `test result in getBudgetInfo`() {
        val expected = 2
        var actual = 0
        val bidInfoData: TopadsBidInfo.Response = TopadsBidInfo.Response(TopadsBidInfo(data = listOf(BidInfoData(shopStatus = expected))))
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(TopadsBidInfo.Response::class.java) } returns listOf()
        every { response.getData<TopadsBidInfo.Response>(TopadsBidInfo.Response::class.java) } returns bidInfoData

        viewModel.getBudgetInfo(0, "reqType", "source") {
            actual = it.bidInfo.data[0].shopStatus
        }

        assertEquals(expected, actual)
    }

    @Test
    fun `test exception in postAutoAds`() {
        val t = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws t

        viewModel.postAutoAds(AutoAdsParam(AutoAdsParam.Input(
                AutoAdsBaseBudgetFragment.TOGGLE_OFF,
                AutoAdsBaseBudgetFragment.CHANNEL,
                1000,
                123,
                AutoAdsBaseBudgetFragment.SOURCE)))

        assertEquals(viewModel.autoAdsData.value, null)
    }

    @Test
    fun `test result in postAutoAds`() {
        val data = TopAdsAutoAdsData()
        val successData: TopAdsAutoAds.Response = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(TopAdsAutoAds.Response::class.java) } returns listOf()
        every { response.getData<TopAdsAutoAds.Response>(TopAdsAutoAds.Response::class.java) } returns successData
        every { successData.autoAds.data } returns data

        viewModel.postAutoAds(AutoAdsParam(AutoAdsParam.Input(
                AutoAdsBaseBudgetFragment.TOGGLE_OFF,
                AutoAdsBaseBudgetFragment.CHANNEL,
                1000,
                123,
                AutoAdsBaseBudgetFragment.SOURCE)))


        assertEquals(data, viewModel.autoAdsData.value)
    }


    @Test
    fun `test exception in getTopAdsDeposit`() {
        val t = Exception("my excep")
        coEvery { repository.getReseponse(any(), any()) } throws t

        viewModel.getTopAdsDeposit(0)

        assertEquals(null, viewModel.topAdsDeposit.value)
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        val expected = 2
        val successData: TopAdsDepositResponse.Data = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(TopAdsDepositResponse.Data::class.java) } returns listOf()
        every { response.getData<TopAdsDepositResponse.Data>(TopAdsDepositResponse.Data::class.java) } returns successData
        every { successData.topadsDashboardDeposits.data.amount } returns expected

        viewModel.getTopAdsDeposit(0)

        assertEquals(expected, viewModel.topAdsDeposit.value)
    }


    @Test
    fun `test exception in topadsStatisticsEstimationPotentialReach`() {
        val t = Exception("my excep")
        val expected = null
        var actual: EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem? = null

        coEvery { repository.getReseponse(any(), any()) } throws t
        viewModel.topadsStatisticsEstimationPotentialReach({
            actual = it
        }, "id", "source")

        assertEquals(expected, actual)
    }

    @Test
    fun `test result in topadsStatisticsEstimationPotentialReach`() {
        val data = EstimationResponse.TopadsStatisticsEstimationAttribute(listOf(EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem(type = 2)))
        var actual: EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem? = null
        val expected = 2
        val successData: EstimationResponse = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(EstimationResponse::class.java) } returns listOf()
        every { response.getData<EstimationResponse>(EstimationResponse::class.java) } returns successData
        every { successData.topadsStatisticsEstimationAttribute } returns data

        viewModel.topadsStatisticsEstimationPotentialReach({
            actual = it
        }, "id", "source")


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
        val expected = "test string"

        every { context.getString(any()) } returns expected

        val actual = viewModel.checkBudget(-2.0, 3.0, 10.0)

        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than minDailyBudget`() {
        val expected = "test string"

        every { context.getString(any()) } returns expected

        val actual = viewModel.checkBudget(1.0, 3.0, 10.0)

        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is greater than maxDailyBudget`() {
        val expected = "test string"

        every { context.getString(any()) } returns expected

        val actual = viewModel.checkBudget(11.0, 3.0, 10.0)

        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than maxDailyBudget and greater than minDailyBudget`() {
        val expected = "test string"

        every { context.getString(any(),any()) } returns expected

        val actual = viewModel.checkBudget(5.0, 3.0, 10.0)

        assertEquals(expected, actual)
    }

    @Test
    fun `test checkBudget when number is less than maxDailyBudget and greater than minDailyBudget and not mutiple of 1000`() {
        val expected = null

        val actual = viewModel.checkBudget(5000.0, 3.0, 10000.0)

        assertEquals(expected, actual)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}