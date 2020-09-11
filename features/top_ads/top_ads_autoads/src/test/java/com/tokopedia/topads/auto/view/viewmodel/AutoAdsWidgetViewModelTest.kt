package com.tokopedia.topads.auto.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.NonDeliveryResponse
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.view.AutoAdsTestDispatcherProvider
import com.tokopedia.topads.auto.view.RequestHelper
import com.tokopedia.topads.auto.view.fragment.AutoAdsBaseBudgetFragment
import com.tokopedia.topads.common.data.util.Utils
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.json.JSONException
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AutoAdsWidgetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: AutoAdsWidgetViewModel

    private lateinit var dispatcher: AutoAdsDispatcherProvider
    private lateinit var repository: GraphqlRepository
    private val rawQueries: Map<String, String> = mapOf()

    @Before
    fun setUp() {
        dispatcher = AutoAdsTestDispatcherProvider()
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mockk()
        viewModel = spyk(AutoAdsWidgetViewModel(dispatcher, repository, rawQueries))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }


    @Test
    fun `test exception in getAutoAdsStatus`() {
        val t = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws t

        viewModel.getAutoAdsStatus(0)

        Assert.assertEquals(viewModel.autoAdsData.value, null)
    }

    @Test
    fun `test result in getAutoAdsStatus`() {
        val data = TopAdsAutoAdsData()
        val successData: TopAdsAutoAds.Response = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(TopAdsAutoAds.Response::class.java) } returns listOf()
        every { response.getData<TopAdsAutoAds.Response>(TopAdsAutoAds.Response::class.java) } returns successData
        every { successData.autoAds.data } returns data

        viewModel.getAutoAdsStatus(0)


        Assert.assertEquals(viewModel.autoAdsData.value, data)
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

        Assert.assertEquals(viewModel.autoAdsData.value, null)
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


        Assert.assertEquals(data, viewModel.autoAdsStatus.value)
    }


    @Test
    fun `test exception in getNotDeliveredReason`() {
        val t = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws t

        viewModel.getNotDeliveredReason("id")

        Assert.assertEquals(null, viewModel.adsDeliveryStatus.value)
    }

    @Test
    fun `test result in getNotDeliveredReason`() {
        val data = listOf(NonDeliveryResponse.TopAdsGetShopStatus.DataItem(status = 1))
        val successData: NonDeliveryResponse = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(NonDeliveryResponse::class.java) } returns listOf()
        every { response.getData<NonDeliveryResponse>(NonDeliveryResponse::class.java) } returns successData
        every { successData.topAdsGetShopStatus.data } returns data

        viewModel.getNotDeliveredReason("id")

        Assert.assertEquals(1, viewModel.adsDeliveryStatus.value?.status)
    }

    @Test
    fun `test exception in getParam`(){
        mockkStatic(Utils::class)
        val e = JSONException("my excep")

        every { Utils.jsonToMap(any()) } throws e

        val params = viewModel.getParams(AutoAdsParam(AutoAdsParam.Input(
                AutoAdsBaseBudgetFragment.TOGGLE_OFF,
                AutoAdsBaseBudgetFragment.CHANNEL,
                1000,
                123,
                AutoAdsBaseBudgetFragment.SOURCE)))

        Assert.assertTrue(params.parameters.isEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}