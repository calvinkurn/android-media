package com.tokopedia.home.repository

import com.tokopedia.createHomeRepositoryImpl
import com.tokopedia.createHomeRepositoryTestInstance
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.PlayRemoteDataSource
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class HomeRepositoryTest : Spek({
    Feature("Repository default data") {
        lateinit var homeRepository: HomeRepository
        createHomeRepositoryTestInstance()

        val getHomeDataSource by memoized<HomeDataSource>()
        val getCachedDataSource by memoized<HomeCachedDataSource>()
        val getRemoteDataSource by memoized<HomeRemoteDataSource>()
        val getPlayRemoteDataSource by memoized<PlayRemoteDataSource>()
        val getHomeDefaultDataSource by memoized<HomeDefaultDataSource>()

        Scenario("Repository with empty database should return default home data") {
            var homeDataResult : HomeData? = null

            Given("Repository has empty database data") {
                coEvery { getCachedDataSource.getCachedHomeData() } .returns(flowOf(null))
            }
            Given("Home repository") {
                homeRepository = createHomeRepositoryImpl()
            }
            When("Home flow data collected") {
                runBlockingTest {
                    homeRepository.getHomeData().collect {
                        homeDataResult = it
                    }
                }
            }
            Then("HomeDataResult is not null") {
                Assert.assertNotNull(homeDataResult)
            }
        }
    }
})