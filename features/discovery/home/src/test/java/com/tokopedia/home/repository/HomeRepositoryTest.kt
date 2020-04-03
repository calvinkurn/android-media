package com.tokopedia.home.repository

import com.tokopedia.createHomeRepositoryImpl
import com.tokopedia.createHomeRepositoryTestInstance
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class  HomeRepositoryTest : Spek({
//    Feature("Repository default data") {
//        lateinit var homeRepository: HomeRepository
//        createHomeRepositoryTestInstance()
//
//        val getCachedDataSource by memoized<HomeCachedDataSource>()
//        val getHomeDefaultDataSource by memoized<HomeDefaultDataSource>()
//
//        Scenario("Repository with empty database should return default home data") {
//            var homeDataResult : HomeData? = null
//            val mockDefaultDynamicChannelId = "9981"
//
//            Given("Repository has empty database data") {
//                coEvery { getCachedDataSource.getCachedHomeData() } .returns(flowOf(null))
//            }
//            Given("Home repository") {
//                homeRepository = createHomeRepositoryImpl()
//            }
//            Given("Home default data source returns mock default data") {
//                every { getHomeDefaultDataSource.getDefaultHomeData() }.returns(HomeData(
//                        DynamicHomeChannel(listOf(
//                                DynamicHomeChannel.Channels(id = mockDefaultDynamicChannelId)
//                        ))
//                ))
//            }
//            When("Home flow data collected") {
//                runBlockingTest {
//                    homeRepository.getHomeData().collect {
//                        homeDataResult = it
//                    }
//                }
//            }
//            Then("Home data result channel is not null") {
//                Assert.assertNotNull(homeDataResult?.dynamicHomeChannel)
//            }
//            Then("Home data result channel id is same as default data mockId") {
//                Assert.assertEquals(mockDefaultDynamicChannelId, homeDataResult?.dynamicHomeChannel?.channels?.get(0)?.id?:0)
//            }
//        }
//
//        Scenario("Repository with non-empty database should return database data") {
//            var homeDataResult : HomeData? = null
//            val mockDatabaseDynamicChannelId = "8871"
//            val mockDefaultDynamicChannelId = "9981"
//
//            Given("Repository has empty database data") {
//                coEvery { getCachedDataSource.getCachedHomeData() } .returns(flowOf(HomeData(
//                        DynamicHomeChannel(listOf(
//                                DynamicHomeChannel.Channels(id = mockDatabaseDynamicChannelId)
//                        ))
//                )))
//            }
//            Given("Home repository") {
//                homeRepository = createHomeRepositoryImpl()
//            }
//            Given("Home default data source returns mock default data") {
//                every { getHomeDefaultDataSource.getDefaultHomeData() }.returns(HomeData(
//                        DynamicHomeChannel(listOf(
//                                DynamicHomeChannel.Channels(id = mockDefaultDynamicChannelId)
//                        ))
//                ))
//            }
//            When("Home flow data collected") {
//                runBlockingTest {
//                    homeRepository.getHomeData().collect {
//                        homeDataResult = it
//                    }
//                }
//            }
//            Then("Home data result channel is not null") {
//                Assert.assertNotNull(homeDataResult?.dynamicHomeChannel)
//            }
//            Then("Home data result channel id is same as database mockId") {
//                Assert.assertEquals(mockDatabaseDynamicChannelId, homeDataResult?.dynamicHomeChannel?.channels?.get(0)?.id?:0)
//            }
//        }
//    }
})