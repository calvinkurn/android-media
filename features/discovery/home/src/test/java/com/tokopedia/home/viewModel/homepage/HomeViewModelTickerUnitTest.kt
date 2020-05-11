package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


@ExperimentalCoroutinesApi
class HomeViewModelTickerUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test Ticker") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()

        Scenario("Test Ticker is need to visible") {
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            Given("Data non ticker") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf()
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel().apply {
                    setNeedToShowGeolocationComponent(true)
                }
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect ticker not show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Test Ticker is visible") {
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val ticker = TickerDataModel()
            Given("Data with ticker") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(ticker)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect ticker not show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.contains(ticker)
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})