package com.tokopedia.entertainment.search.viewmodel

import org.junit.Assert.*

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.search.data.EventSearchHistoryResponse
import com.tokopedia.entertainment.search.data.EventSearchLocationResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Author errysuprayogi on 24,February,2020
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class EventSearchViewModelTest{

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
    fun getHistoryDataGql() {
        assertNotNull(graphqlRepository)

        val dataMock = Gson().fromJson(getJson("history_mock.json"), EventSearchHistoryResponse::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventSearchHistoryResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)
        assertNotNull(dataMock)

        every { userSessionInterface.isLoggedIn } returns true

        runBlocking(Dispatchers.Unconfined){
            try {
                val data = eventSearchViewModel.getHistorySearchData(CacheType.CACHE_FIRST)
                assertNotNull(data)
                assertEquals(dataMock.data, data)
                assertEquals(data.travelCollectiveRecentSearches.items.size, 6)
            }catch (e: Exception){
                println(e.message)
            }
        }

    }

    @Test
    fun getSearchDataGql(){
        assertNotNull(graphqlRepository)

        val dataMock = Gson().fromJson(getJson("search_mock.json"), EventSearchLocationResponse::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventSearchLocationResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)
        assertNotNull(dataMock)

        every { userSessionInterface.isLoggedIn } returns true

        runBlocking(Dispatchers.Unconfined){
            try {
                val data = eventSearchViewModel.getLocationSuggestionData("",CacheType.CACHE_FIRST)
                assertNotNull(data)
                assertEquals(dataMock.data, data)
                assertEquals(data.eventLocationSearch.count, "2")
                assertEquals(data.eventSearch.count, "5")
            }catch (e: Exception){
                println(e.message)
            }
        }
    }

    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}