package com.tokopedia.entertainment.search.viewmodel

import org.junit.Assert.*

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewmodel.FirstTimeModel
import com.tokopedia.entertainment.search.adapter.viewmodel.HistoryModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationModel
import com.tokopedia.entertainment.search.data.EventSearchHistoryResponse
import com.tokopedia.entertainment.search.data.EventSearchLocationResponse
import com.tokopedia.entertainment.search.data.mapper.SearchMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.Before
import org.junit.Test
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
class EventSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventSearchViewModel: EventSearchViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var resources: Resources

    val context = mockk<Context>(relaxed = true)


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        eventSearchViewModel = EventSearchViewModel(Dispatchers.Unconfined, graphqlRepository, userSessionInterface)
        eventSearchViewModel.resources = context.resources
    }

    @Test
    fun  fetchhistory_successhistorylogin_success() {
        assertNotNull(graphqlRepository)

        val dataMock = Gson().fromJson(getJson("history_mock.json"), EventSearchHistoryResponse::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventSearchHistoryResponse.Data::class.java to dataMock.data
        ) as MutableMap<Type, Any>, HashMap<Type, List<GraphqlError>>(), false)
        assertNotNull(dataMock)

        every { userSessionInterface.isLoggedIn } returns true

        val dataMockMapper = SearchMapper.mappingHistorytoSearchList(dataMock.data)

        eventSearchViewModel.getHistorySearch(CacheType.CACHE_FIRST,"")

        assertNotNull(eventSearchViewModel.searchList)
        assertEquals((eventSearchViewModel.searchList.value?.get(0) as HistoryModel).list, (dataMockMapper.get(0) as HistoryModel).list)

    }

    @Test
    fun  fetchhistory_successhistorynonlogin_success() {
        assertNotNull(graphqlRepository)

        val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
        listViewHolder.add(FirstTimeModel())

        every { userSessionInterface.isLoggedIn } returns false

        eventSearchViewModel.getHistorySearch(CacheType.CACHE_FIRST,"")

        assertNotNull(eventSearchViewModel.searchList)
        assertEquals((eventSearchViewModel.searchList.value?.get(0) as FirstTimeModel).isFirstTime , (listViewHolder.get(0) as FirstTimeModel).isFirstTime)

    }

    @Test
    fun fetchhistory_failedhistory_failed() {
        assertNotNull(graphqlRepository)

        val errorGql = GraphqlError()
        errorGql.message = "Error Fetch History"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[ EventSearchHistoryResponse.Data::class.java] = listOf(errorGql)

        every { userSessionInterface.isLoggedIn } returns true

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } coAnswers {
            GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        }

        eventSearchViewModel.getHistorySearch(CacheType.CACHE_FIRST,"")

        assertNotNull(eventSearchViewModel.errorReport.value)
        assertEquals(eventSearchViewModel.errorReport.value, errorGql.message)
    }

    @Test
    fun fetchsearchdata_successsearchdata_success() {
        assertNotNull(graphqlRepository)

        val dataMock = Gson().fromJson(getJson("search_mock.json"), EventSearchLocationResponse::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventSearchLocationResponse.Data::class.java to dataMock.data
        ) as MutableMap<Type, Any>, HashMap<Type, List<GraphqlError>>(), false)
        assertNotNull(dataMock)

        val dataMockMapped = SearchMapper.mappingLocationandKegiatantoSearchList(dataMock.data,"", eventSearchViewModel.resources)

        eventSearchViewModel.getSearchData("",CacheType.CACHE_FIRST,"")

        assertEquals((eventSearchViewModel.searchList.value?.get(0) as SearchLocationModel).listLocation, (dataMockMapped.get(0) as SearchLocationModel).listLocation)
        assertEquals((eventSearchViewModel.searchList.value?.get(1) as SearchEventModel).listEvent, (dataMockMapped.get(1) as SearchEventModel).listEvent)
    }

    @Test
    fun fetchsearchdata_failedsearchdata_failed() {
        assertNotNull(graphqlRepository)

        val errorGql = GraphqlError()
        errorGql.message = "Error Fetch Search"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EventSearchLocationResponse.Data::class.java] = listOf(errorGql)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } coAnswers {
            GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        }

        eventSearchViewModel.getSearchData("",CacheType.CACHE_FIRST,"")

        assertNotNull(eventSearchViewModel.errorReport.value)
        assertEquals(eventSearchViewModel.errorReport.value, errorGql.message)
    }

    private fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }

    @Test
    fun job_setJOb(){
        //given
        val job = Job()

        //when
        eventSearchViewModel.job = job

        //then
        assertEquals(eventSearchViewModel.job, job)
    }

    @Test
    fun job_cancelJob(){
        //given
        val job = Job()

        //when
        eventSearchViewModel.job = job
        eventSearchViewModel.cancelRequest()

        //then
        assert(job.isCancelled)
    }
}