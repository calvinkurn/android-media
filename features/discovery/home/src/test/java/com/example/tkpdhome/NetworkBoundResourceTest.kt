package com.example.tkpdhome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tkpdhome.rules.CoroutinesMainDispatcherRule
import com.tokopedia.abstraction.base.data.source.NetworkBoundResource
import com.tokopedia.abstraction.base.data.source.Resource
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class NetworkBoundResourceTest {
    private val mockHomeData = mockk<HomeData>()

    val graphqlResponse = GraphqlResponse(mapOf(
            HomeData::class.java to mockHomeData
    ), mapOf(), false)

    private lateinit var handleProcessResponse: (GraphqlResponse) -> HomeData

    private lateinit var handleShouldFetch: (HomeData?) -> Boolean

    private lateinit var handleCreateCall: suspend () -> GraphqlResponse

    private lateinit var handleFetchDb: suspend () -> HomeData?

    private lateinit var handleSaveResult: suspend (HomeData) -> Unit

    private lateinit var networkBound : NetworkBoundResource<GraphqlResponse, HomeData>
    private lateinit var observerHome: Observer<Resource<HomeData>>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Before
    fun init(){
        observerHome = mockk(relaxed = true)
        handleSaveResult = {}
        handleFetchDb = { null }
        networkBound = object : NetworkBoundResource<GraphqlResponse, HomeData>(){
            override fun processResponse(response: GraphqlResponse): HomeData {
                return handleProcessResponse(response)
            }

            override suspend fun saveCallResults(items: HomeData) {
                handleSaveResult(items)
            }

            override suspend fun onFetchError() {}

            override fun shouldFetch(data: HomeData?): Boolean {
                return handleShouldFetch(data)
            }

            override suspend fun loadFromDb(): HomeData? {
                return handleFetchDb()
            }

            override suspend fun createCallAsync(): GraphqlResponse {
                return handleCreateCall()
            }
        }
    }

    @Test
    fun `there is no cache and should fetch from network`(){
        handleCreateCall = { graphqlResponse }
        handleShouldFetch = { true }
        handleProcessResponse = { mockHomeData }

        runBlocking {
            networkBound.build().asLiveData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.success(mockHomeData)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `there is have cache and should fetch from network`(){
        handleCreateCall = { graphqlResponse }
        handleShouldFetch = { true }
        handleFetchDb = { mockHomeData }
        handleProcessResponse = { mockHomeData }

        runBlocking {
            networkBound.build().asLiveData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(mockHomeData)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.success(mockHomeData)) // Success
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `there is have cache and should fetch from network but error mapping`(){
        val throwable = Throwable()
        handleCreateCall = { graphqlResponse }
        handleShouldFetch = { true }
        handleFetchDb = { mockHomeData }
        handleProcessResponse = { throw throwable }

        runBlocking {
            networkBound.build().asLiveData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(mockHomeData)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.error(throwable, mockHomeData)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `there is have cache and should fetch from network but network error`(){
        val throwable = Throwable()
        handleCreateCall = { throw throwable }
        handleShouldFetch = { true }
        handleFetchDb = { mockHomeData }
        handleProcessResponse = { mockHomeData }

        runBlocking {
            networkBound.build().asLiveData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(mockHomeData)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.error(throwable, mockHomeData)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `there is no have cache and should fetch from network but network error`(){
        val throwable = Throwable()
        handleCreateCall = { throw throwable }
        handleShouldFetch = { true }
        handleFetchDb = { null }
        handleProcessResponse = { mockHomeData }

        runBlocking {
            networkBound.build().asLiveData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.error(throwable, null)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }



}