package com.tokopedia.topads.auto.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.auto.data.entity.TopAdsShopInfoData
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfo
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.view.AutoAdsTestDispatcherProvider
import com.tokopedia.topads.auto.view.RequestHelper
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsInfoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopAdsInfoViewModel

    private lateinit var dispatcher: AutoAdsDispatcherProvider
    private lateinit var repository: GraphqlRepository
    private val rawQueries: Map<String, String> = mapOf()

    @Before
    fun setUp() {
        dispatcher = AutoAdsTestDispatcherProvider()
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mockk()
        viewModel = spyk(TopAdsInfoViewModel(dispatcher, repository, rawQueries))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }


    @Test
    fun `test exception in getShopAdsInfo`() {
        var t:Throwable? = null
        val myThrowable:Throwable = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws myThrowable

        viewModel.getShopAdsInfo(0) {
            t = it
        }

        assertEquals(myThrowable.message, t?.message)
    }


    @Test
    fun `test result in getShopAdsInfo`() {
        val data = TopAdsShopInfoData()
        val successData: TopAdsShopInfo.Response = mockk(relaxed = true)
        val response: GraphqlResponse = mockk(relaxed = true)

        coEvery { repository.getReseponse(any(),any()) } returns response
        every { response.getError(TopAdsShopInfo.Response::class.java)} returns listOf()
        every { response.getData<TopAdsShopInfo.Response>(TopAdsShopInfo.Response::class.java) } returns successData
        every { successData.shopInfo.data} returns data

        viewModel.getShopAdsInfo(0){}


        assertEquals(data, viewModel.shopInfoData.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }


}