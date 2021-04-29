package com.tokopedia.entertainment.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.entertainment.home.utils.MapperHomeData.mappingItem
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

@RunWith(JUnit4::class)
class EventHomeViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventHomeViewModel: EventHomeViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventHomeViewModel = EventHomeViewModel(Dispatchers.Unconfined, graphqlRepository)
    }

    @Test
    fun loadingTest_SuccessInitialTest(){
        //when
        eventHomeViewModel.getIntialList()

        //then
        assertNotNull(eventHomeViewModel.eventHomeListData.value)
    }

    @Test
    fun getHomeData_SuccessGetHomeData_Success(){
        //given
        val data = Gson().fromJson<EventHomeDataResponse.Data>(getJson("home_response_mock.json"), EventHomeDataResponse.Data::class.java)
        val dataResponse = Gson().fromJson<EventHomeDataResponse.Data>(getJson("home_response_mock.json"), EventHomeDataResponse.Data::class.java)
        val mappedData = Success(mappingItem(data))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = EventHomeDataResponse.Data::class.java
        result[objectType] = dataResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        //when
        eventHomeViewModel.getHomeData(false)

        //then
        val mappedResultData = eventHomeViewModel.eventHomeListData.value as Success

        assert(!mappedResultData.data.isNullOrEmpty())
        assertEquals(mappedResultData.data.size, mappedData.data.size)
    }

    @Test
    fun getHomeData_FailedGetHomeData_Failed(){
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error Fetch All Location"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EventHomeDataResponse.Data::class.java] = listOf(errorGql)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } coAnswers {
            GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        }
        //when
        eventHomeViewModel.getHomeData(true)

        //then
        assert(eventHomeViewModel.eventHomeListData.value is Fail)
    }
}