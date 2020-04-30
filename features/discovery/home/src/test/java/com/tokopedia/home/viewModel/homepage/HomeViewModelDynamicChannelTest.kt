package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class HomeViewModelDynamicChannelTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get dynamic channel data") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getDynamicChannelsUseCase by memoized<GetDynamicChannelsUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()

        Scenario("Get dynamic channel data success with single data") {
            val dataModel = DynamicChannelDataModel()
            dataModel.channel = DynamicHomeChannel.Channels(id = "1")
            val dynamicChannel = DynamicHomeChannel.Channels(id = "2")
            val dynamicChannelViewModel = DynamicChannelDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            dynamicChannelViewModel.channel = dynamicChannel
            Given("dynamic banner almost expired time") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }


            Given("dynamic data returns success") {
                getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                        dynamicChannelDataModels = listOf(dynamicChannelViewModel)
                )
            }

            When("viewModel load request update dynamic channel data") {
                homeViewModel.getDynamicChannelData(dataModel, 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
                    })
                    // check on second update data liveData is removed old dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() !is DynamicChannelDataModel
                    })

                    // check after removed is must add new channel from list of channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "2"
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get dynamic channel data success with multiple data") {
            val dataModel = DynamicChannelDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            dataModel.channel = DynamicHomeChannel.Channels(id = "1")
            val dynamicChannel = DynamicHomeChannel.Channels(id = "2")
            val dynamicChannel2 = DynamicHomeChannel.Channels(id = "3")
            val dynamicChannelViewModel1 = DynamicChannelDataModel()
            val dynamicChannelViewModel2 = DynamicChannelDataModel()
            dynamicChannelViewModel1.channel = dynamicChannel
            dynamicChannelViewModel2.channel = dynamicChannel2

            Given("dynamic banner almost expired time") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }


            Given("dynamic data returns success") {
                getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                        dynamicChannelDataModels = listOf(dynamicChannelViewModel1, dynamicChannelViewModel2)
                )
            }

            When("viewModel load request update dynamic channel data") {
                homeViewModel.getDynamicChannelData(dataModel, 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
                    })
                    // check on second update data liveData is removed old dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() !is DynamicChannelDataModel
                    })

                    // check after removed is must add new channel from list of channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "3"
                    })

                    // check the second new channel from list of channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "2"
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get dynamic channel data success with empty data") {
            val dataModel = DynamicChannelDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            dataModel.channel = DynamicHomeChannel.Channels(id = "1")

            Given("dynamic banner almost expired time") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }


            Given("dynamic data returns success") {
                getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                        dynamicChannelDataModels = listOf()
                )
            }

            When("viewModel load request update dynamic channel data") {
                homeViewModel.getDynamicChannelData(dataModel, 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() !is DynamicChannelDataModel
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get dynamic channel data error") {
            val dataModel = DynamicChannelDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            dataModel.channel = DynamicHomeChannel.Channels(id = "1")

            Given("dynamic banner almost expired time") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }


            Given("dynamic data returns success") {
                getDynamicChannelsUseCase.givenGetDynamicChannelsUseCaseThrowReturn()
            }

            When("viewModel load request update dynamic channel data") {
                homeViewModel.getDynamicChannelData(dataModel, 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})