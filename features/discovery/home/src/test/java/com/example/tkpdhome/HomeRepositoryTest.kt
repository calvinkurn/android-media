package com.example.tkpdhome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tkpdhome.rules.CoroutinesMainDispatcherRule
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
class HomeRepositoryTest {
    private lateinit var repository: HomeRepository
    private val dao = mockk<HomeDao>(relaxed = true)
    private val service = mockk<HomeRemoteDataSource>()
    private val homeDataSource = mockk<HomeDataSource>()
    private lateinit var observerHome: Observer<Resource<HomeData>>

        @Rule
        @JvmField
        val instantExecutorRule = InstantTaskExecutorRule()

        @get:Rule
        var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Before
    fun init(){
        observerHome = mockk(relaxed = true)
        repository = HomeRepositoryImpl(homeDataSource, dao, service)
    }

    @Test
    fun `Get home data from server when no internet is available`(){
        val exception = Exception("Internet")
        val mockHomeData = mockk<HomeData>()
        coEvery { service.getHomeData() } throws exception
        coEvery { dao.getHomeData() } returns mockHomeData
        coEvery{ mockHomeData.copy(any(), any(), any(),any(),any(),any(),any(),any()) } returns mockHomeData

        runBlocking {
            repository.getHomeData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(mockHomeData)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.error(exception, mockHomeData)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Get home data from network`() {
        val fakeHomeData = HomeData()
        val graphqlResponse = GraphqlResponse(mapOf(
                HomeData::class.java to fakeHomeData
        ), mapOf(), false)
        val networkData = graphqlResponse.getSuccessData<HomeData>()
        val mockHomeData = mockk<HomeData>()
        runBlocking {
            coEvery { service.getHomeData() } returns graphqlResponse
            coEvery { dao.getHomeData() } returns mockHomeData
            coEvery{ mockHomeData.copy(any(), any(), any(),any(),any(),any(),any(),any()) } returns mockHomeData
        }

        runBlocking {
            repository.getHomeData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(mockHomeData)) // trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.success(networkData)) // Success
        }

        coVerify(exactly = 1) {
            dao.save(fakeHomeData)
        }

        confirmVerified(observerHome)
    }


    @Test
    fun `Get home data from db`() {
        val fakeHomeData = HomeData()
        val mockHomeViewModel = mockk<HomeData>()
        val graphqlResponse = GraphqlResponse(mapOf(
                HomeData::class.java to fakeHomeData
        ), mapOf(), false)
        coEvery { service.getHomeData() } returns graphqlResponse
        coEvery { dao.getHomeData() } returns mockHomeViewModel
        coEvery{ mockHomeViewModel.copy(any(), any(), any(),any(),any(),any(),any(),any()) } returns mockHomeViewModel

        runBlocking {
            repository.getHomeData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(mockHomeViewModel)) // Loading from remote source
            observerHome.onChanged(Resource.success(fakeHomeData)) // Success
        }

        confirmVerified(observerHome)
    }

}