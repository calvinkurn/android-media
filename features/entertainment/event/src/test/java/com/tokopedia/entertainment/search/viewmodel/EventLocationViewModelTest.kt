package com.tokopedia.entertainment.search.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.lang.Exception
import java.lang.reflect.Type

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class EventLocationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventLocationViewModel: EventLocationViewModel

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
        eventLocationViewModel = EventLocationViewModel(Dispatchers.Unconfined, graphqlRepository, userSessionInterface)
        eventLocationViewModel.resources = context.resources
    }

    @Test
    fun checkNull(){
        Assert.assertNotNull(eventLocationViewModel.searchList)
        Assert.assertNotNull(eventLocationViewModel.listViewHolder)
    }

    @Test
    fun getLocationData(){
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(eventLocationViewModel.resources)

        val dataMock = Gson().fromJson(getJson("full_location_mock.json"), EventSearchFullLocationResponse::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventSearchFullLocationResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)
        Assert.assertNotNull(dataMock)

        every { userSessionInterface.isLoggedIn } returns true

        runBlocking(Dispatchers.Unconfined){
            try {
                val data = eventLocationViewModel.getFullLocationSuggestionData()
                Assert.assertNotNull(data)
                Assert.assertEquals(dataMock.data, data)
                Assert.assertEquals(data.eventLocationSearch.locations.size, 20)
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