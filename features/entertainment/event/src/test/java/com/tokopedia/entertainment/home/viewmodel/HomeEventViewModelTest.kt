package com.tokopedia.entertainment.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.Answer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.lang.reflect.Type


/**
 * Author errysuprayogi on 24,February,2020
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class HomeEventViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var homeEventViewModel: HomeEventViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository
    @MockK
    lateinit var restRepository: RestRepository
    @MockK
    lateinit var userSessionInterface: UserSessionInterface
    @MockK
    lateinit var fragmentView: FragmentView

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        homeEventViewModel = HomeEventViewModel(Dispatchers.Unconfined, graphqlRepository, restRepository, userSessionInterface)
    }

    @Test
    fun getHomeData() {
        assertNotNull(fragmentView)
        assertNotNull(graphqlRepository)
        assertNotNull(homeEventViewModel)
        val data = Gson().fromJson<EventHomeDataResponse.Data>(getJson("home_response_mock.json"), EventHomeDataResponse.Data::class.java)
        val mockKAdditionalAnswerScope = coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventHomeDataResponse.Data::class.java to data
        ) as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)

        fun result(mutableList: MutableList<HomeEventItem<*>>) {
            assertNotNull(mutableList)
            assertEquals(mutableList.size, 10)
        }

        fun error(throwable: Throwable) {
            assertNotNull(throwable)
        }

        runBlocking(Dispatchers.Unconfined){
            homeEventViewModel.getHomeData(fragmentView, ::result, ::error, CacheType.CACHE_FIRST)
        }

    }

    @Test
    fun postLiked() {
        val item = EventItemModel(
                produkId = 26423,
                price = "Rp 100.000",
                isLiked = true,
                appUrl = "tokopedia://events/jakarta-sneaker-day-2020-26423",
                date = "26/03/2020 - 29/03/2020",
                imageUrl = "https://ecs7.tokopedia.net/img/banner/2020/1/27/22166894/22166894_b80f7f4a-2401-47ce-a68f-1e3f52e6fd83.jpg",
                location = "Jabodetabek",
                rating = 0,
                title = "JSD XX")

        fun onsuccess(eventItemModel: EventItemModel) {
            assertNotNull(eventItemModel)
            assertEquals(eventItemModel.isLiked, true)
        }
        fun onerror(throwable: Throwable) {
            assertNotNull(throwable)
        }
        runBlocking {
            homeEventViewModel.postLiked(item,::onsuccess,::onerror)
        }
    }

    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path ?: "")
        return String(file.readBytes())
    }
}