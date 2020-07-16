package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

class HomeViewModelListCarouselUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test express checkout"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getDynamicChannelsUseCase by memoized<GetDynamicChannelsUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()
        val getAtcUseCase by memoized<AddToCartOccUseCase>()

        Scenario("Get dynamic channel data success with single data and try express checkout") {
            val dataModel = DynamicChannelDataModel()
            dataModel.channel = DynamicHomeChannel.Channels(id = "1", grids = arrayOf(DynamicHomeChannel.Grid()))
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val observerExpressCheckout: Observer<Event<Any>> = mockk(relaxed = true)


            Given("dynamic channel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("Success Express Checkout"){
                every{ getAtcUseCase.createObservable(any()) } returns
                        Observable.just(mockk())
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
                homeViewModel.oneClickCheckout.observeForever(observerExpressCheckout)
            }


            Given("dynamic data returns success") {
                getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                        dynamicChannelDataModels = listOf(dataModel)
                )
            }

            When("Express checkout clicked"){
                homeViewModel.getOneClickCheckout(dataModel.channel!!, dataModel.channel!!.grids.first(), 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
                    })
                }
                confirmVerified(observerHome)
            }

            Then("Event express checkout should be triggered"){
                Thread.sleep(100)
                verifyOrder {
                    observerExpressCheckout.onChanged(match { it != null })
                }
                confirmVerified(observerExpressCheckout)
            }
        }

        Scenario("Get dynamic channel data success with single data and fail express checkout") {
            val dataModel = DynamicChannelDataModel()
            dataModel.channel = DynamicHomeChannel.Channels(id = "1", grids = arrayOf(DynamicHomeChannel.Grid()))
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val observerExpressCheckout: Observer<Event<Any>> = mockk(relaxed = true)

            Given("dynamic channel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("Success Express Checkout"){
                every{ getAtcUseCase.createObservable(any()) } returns Observable.error(mockk())
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
                homeViewModel.oneClickCheckout.observeForever(observerExpressCheckout)
            }


            Given("dynamic data returns success") {
                getDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(
                        dynamicChannelDataModels = listOf(dataModel)
                )
            }

            When("Express checkout clicked"){
                homeViewModel.getOneClickCheckout(dataModel.channel!!, dataModel.channel!!.grids.first(), 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is DynamicChannelDataModel &&
                                (it.list.first() as DynamicChannelDataModel).channel?.id == "1"
                    })
                }
                confirmVerified(observerHome)
            }

            Then("Event express checkout should be triggered"){
                Thread.sleep(100)
                verifyOrder {
                    observerExpressCheckout.onChanged(match { it != null })
                }
                confirmVerified(observerExpressCheckout)
            }
        }
    }

    Feature("Test Close Buy Again Widget"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val getHomeUseCase by memoized<HomeUseCase>()
        val getAtcUseCase by memoized<AddToCartOccUseCase>()

        Scenario("Get dynamic channel data success with single data and try close widget") {
            val dataModel = DynamicChannelDataModel()
            dataModel.channel = DynamicHomeChannel.Channels(id = "1")
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)


            Given("dynamic channel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(dataModel)
                        )
                )
            }

            Given("Success Express Checkout"){
                every{ getAtcUseCase.createObservable(any()) } returns Observable.just(mockk())
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Express checkout clicked"){
                homeViewModel.onCloseBuyAgain(dataModel.channel!!.id, 0)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
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

    }
})