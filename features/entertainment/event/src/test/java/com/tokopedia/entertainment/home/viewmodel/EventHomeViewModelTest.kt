package com.tokopedia.entertainment.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.home.adapter.viewmodel.LoadingHomeModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.entertainment.home.utils.MapperHomeData.mappingItem
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class EventHomeViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val coroutineDispatcher = CoroutineTestDispatchers

    private lateinit var eventHomeViewModel: EventHomeViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    companion object{
        const val HOME_MOCK_RESPONSE = "home_response_mock.json"
    }

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventHomeViewModel = EventHomeViewModel(coroutineDispatcher.io, graphqlRepository)
    }

    @Test
    fun loadingTest_SuccessInitialTest(){
        //given
        val expected = Success(listOf(LoadingHomeModel()))

        //when
        eventHomeViewModel.getIntialList()

        //then
        assertTrue(eventHomeViewModel.eventHomeListData.value is Success)
        val data = eventHomeViewModel.eventHomeListData.value as Success
        assertNotNull(data)
        assertEquals(expected.data.size, data.data.size)
    }

    @Test
    fun getHomeData_SuccessGetHomeData_Success(){
        //given
        val data = Gson().fromJson<EventHomeDataResponse.Data>(getJson(HOME_MOCK_RESPONSE), EventHomeDataResponse.Data::class.java)
        val dataResponse = Gson().fromJson<EventHomeDataResponse.Data>(getJson(HOME_MOCK_RESPONSE), EventHomeDataResponse.Data::class.java)
        val mappedData = Success(mappingItem(data))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = EventHomeDataResponse.Data::class.java
        result[objectType] = dataResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        eventHomeViewModel.getHomeData(false)

        //then
        assertTrue(eventHomeViewModel.eventHomeListData.value is Success)
        assertFalse(eventHomeViewModel.eventHomeListData.value is Fail)

        val mappedResultData = eventHomeViewModel.eventHomeListData.value as Success

        assertFalse(mappedResultData.data.isNullOrEmpty())
        assertEquals(mappedResultData.data.size, mappedData.data.size)

        coVerify { graphqlRepository.response(any(), any()) }
    }

    @Test
    fun getHomeData_WhenIsLoadFromCloud_SuccessGetHomeData_Success(){
        //given
        val data = Gson().fromJson<EventHomeDataResponse.Data>(getJson(HOME_MOCK_RESPONSE), EventHomeDataResponse.Data::class.java)
        val dataResponse = Gson().fromJson<EventHomeDataResponse.Data>(getJson(HOME_MOCK_RESPONSE), EventHomeDataResponse.Data::class.java)
        val mappedData = Success(mappingItem(data))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = EventHomeDataResponse.Data::class.java
        result[objectType] = dataResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        eventHomeViewModel.getHomeData(true)

        //then
        assertTrue(eventHomeViewModel.eventHomeListData.value is Success)
        assertFalse(eventHomeViewModel.eventHomeListData.value is Fail)

        val mappedResultData = eventHomeViewModel.eventHomeListData.value as Success

        assertFalse(mappedResultData.data.isNullOrEmpty())
        assertEquals(mappedResultData.data.size, mappedData.data.size)

        coVerify { graphqlRepository.response(any(), any()) }
    }

    @Test
    fun getHomeData_FailedGetHomeData_Failed(){
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error Fetch All Location"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EventHomeDataResponse.Data::class.java] = listOf(errorGql)

        coEvery {
            graphqlRepository.response(any(), any())
        } coAnswers {
            GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        }
        //when
        eventHomeViewModel.getHomeData(false)

        //then
        assertTrue(eventHomeViewModel.eventHomeListData.value is Fail)
        assertFalse(eventHomeViewModel.eventHomeListData.value is Success)

        coVerify { graphqlRepository.response(any(), any()) }
    }

    @Test
    fun getHomeData_Failed_WhenIsLoadFromCloud_GetHomeData_Failed(){
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error Fetch All Location"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EventHomeDataResponse.Data::class.java] = listOf(errorGql)

        coEvery {
            graphqlRepository.response(any(), any())
        } coAnswers {
            GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        }
        //when
        eventHomeViewModel.getHomeData(true)

        //then
        assertTrue(eventHomeViewModel.eventHomeListData.value is Fail)
        assertFalse(eventHomeViewModel.eventHomeListData.value is Success)

        coVerify { graphqlRepository.response(any(), any()) }
    }
}