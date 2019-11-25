package com.example.tkpdhome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tkpdhome.rules.CoroutinesMainDispatcherRule
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.HomeMapper
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.abstraction.base.data.source.Resource
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
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
    private val homeDataSource = mockk<HomeDataSource>()
    private val homeMapper = mockk<HomeMapper>()
    private lateinit var observerHome: Observer<Resource<HomeViewModel>>

        @Rule
        @JvmField
        val instantExecutorRule = InstantTaskExecutorRule()

        @get:Rule
        var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Before
    fun init(){
        observerHome = mockk(relaxed = true)
        repository = HomeRepositoryImpl(homeDataSource, dao, service, homeMapper)
    }

    @Test
    fun `Get home data from server when no internet is available`(){
        val exception = Exception("Internet")
        val mockHomeData = mockk<HomeData>()
        val mockHomeViewModel = mockk<HomeViewModel>()
        runBlocking {
            coEvery { service.getHomeData() } throws exception
            coEvery { dao.getHomeData() } returns mockHomeData
            repository.getHomeData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Init loading with no value
            observerHome.onChanged(Resource.loading(mockHomeViewModel)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.error(exception, mockHomeViewModel)) // Retrofit 403 error
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Get home data from network`() {
        val fakeHomeData = HomeData()
        val graphqlResponse = GraphqlResponse(mapOf(
                HomeData::class.java to fakeHomeData
        ), mapOf(), false)
        val mockHomeData = mockk<HomeData>()
        val mockHomeViewModel = mockk<HomeViewModel>()
        runBlocking {
            coEvery { service.getHomeData() } returns graphqlResponse
            coEvery { dao.getHomeData() } returns mockHomeData andThen fakeHomeData
        }

        runBlocking {
            repository.getHomeData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Loading from remote source
            observerHome.onChanged(Resource.success(mockHomeViewModel)) // Then trying to load from db (fast temp loading) before load from remote source
            observerHome.onChanged(Resource.success(mockHomeViewModel)) // Success
        }

        coVerify(exactly = 1) {
            dao.save(fakeHomeData)
        }

        confirmVerified(observerHome)
    }


    @Test
    fun `Get home data from db`() {
        val fakeHomeData = HomeData()
        val mockHomeViewModel = mockk<HomeViewModel>()
        val graphqlResponse = GraphqlResponse(mapOf(
                HomeData::class.java to fakeHomeData
        ), mapOf(), false)
        coEvery { service.getHomeData() } returns graphqlResponse
        coEvery { dao.getHomeData() } returns fakeHomeData

        runBlocking {
            repository.getHomeData().observeForever(observerHome)
        }

        verifyOrder {
            observerHome.onChanged(Resource.loading(null)) // Loading from remote source
            observerHome.onChanged(Resource.success(mockHomeViewModel)) // Loading from remote source
            observerHome.onChanged(Resource.success(mockHomeViewModel)) // Success
        }

        confirmVerified(observerHome)
    }

}