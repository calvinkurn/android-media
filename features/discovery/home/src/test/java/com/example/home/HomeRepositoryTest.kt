package com.example.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.home.rules.CoroutinesMainDispatcherRule
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.data.datasource.local.dao.HomeDao
import com.tokopedia.v2.home.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.v2.home.data.repository.HomeRepositoryImpl
import com.tokopedia.v2.home.model.pojo.home.HomeData
import com.tokopedia.v2.home.model.vo.Resource
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class HomeRepositoryTest {
    private lateinit var repository: HomeRepository
    private val dao = mockk<HomeDao>(relaxed = true)
    private val service = mockk<HomeRemoteDataSource>()
    private lateinit var observerHome: Observer<Resource<HomeData>>

        @Rule
        @JvmField
        val instantExecutorRule = InstantTaskExecutorRule()

        @get:Rule
        var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Before
    fun init(){
        observerHome = mockk(relaxed = true)
        repository = HomeRepositoryImpl(dao, service)
    }

    @Test
    fun `Get home data from server when no internet is available`(){
        val exception = Exception("Internet")
        val mockHomeData = mockk<HomeData>()
        runBlocking {
            coEvery { service.getHomeData() } throws exception
            coEvery { dao.getHomeData() } returns mockHomeData
            repository.getHomeDataWithCache().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Init loading with no value
            observerHome.onChanged(Resource.loading(mockHomeData)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.error(exception, mockHomeData)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Get home data from network`() {
        val fakeHomeData = HomeData(1, null, null, null, null, null, null)
        val graphqlResponse = GraphqlResponse(mapOf(
                HomeData::class.java to fakeHomeData
        ), mapOf(), false)
        val mockHomeData = mockk<HomeData>()
        runBlocking {
            coEvery { service.getHomeData() } returns graphqlResponse
            coEvery { dao.getHomeData() } returns mockHomeData andThen fakeHomeData
        }

        runBlocking {
            repository.getHomeDataWithCache().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Loading from remote source
            observerHome.onChanged(Resource.loading(mockHomeData)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.success(fakeHomeData)) // Success
        }

        coVerify(exactly = 1) {
            dao.save(fakeHomeData)
        }

        confirmVerified(observerHome)
    }


    @Test
    fun `Get home data from db`() {
        val fakeHomeData = HomeData(1, null, null, null, null, null, null)
        val graphqlResponse = GraphqlResponse(mapOf(
                HomeData::class.java to fakeHomeData
        ), mapOf(), false)
        coEvery { service.getHomeData() } returns graphqlResponse
        coEvery { dao.getHomeData() } returns fakeHomeData

        runBlocking {
            repository.getHomeDataWithCache().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Loading from remote source
            observerHome.onChanged(Resource.loading(fakeHomeData)) // Loading from remote source
            observerHome.onChanged(Resource.success(fakeHomeData)) // Success
        }

        confirmVerified(observerHome)
    }

}