package com.tokopedia.entertainment.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.entertainment.home.adapter.viewmodel.LoadingHomeModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.entertainment.home.utils.MapperHomeData.mappingItem
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.CoroutineDispatcher
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

        //when
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = EventHomeDataResponse.Data::class.java
        result[objectType] = data
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        eventHomeViewModel.getHomeData(true)

        val mappedData = Success(mappingItem(data))
        val mappedResultData = eventHomeViewModel.eventHomeListData.value as Success

        //then
        assertEquals(mappedResultData.data.size, mappedData.data.size)
    }
}