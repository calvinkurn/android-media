package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetPopularKeywordUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


@ExperimentalCoroutinesApi
class HomeViewModelPopularKeywordUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Popular keyword test") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()

        Scenario("Test Popular is not visible") {
            val popular = PopularKeywordListDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            Given("Populate empty data") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf()
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect popular widget not show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is PopularKeywordListDataModel } == null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Test Popular is visible") {
            val popular = PopularKeywordListDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            Given("Data with popular keyword") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(popular)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect popular keyword will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is PopularKeywordListDataModel } != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Test Popular with data keyword") {
            val popular = PopularKeywordListDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val getPopularKeywordUseCase by memoized<GetPopularKeywordUseCase>()

            Given("Data with popular keyword") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(popular)
                        )
                )
            }

            Given("Popular keyword data"){
                coEvery { getPopularKeywordUseCase.executeOnBackground() } returns HomeWidget.PopularKeywordQuery(
                        data = HomeWidget.PopularKeywordList(
                                keywords = listOf(
                                        HomeWidget.PopularKeyword(),
                                        HomeWidget.PopularKeyword(),
                                        HomeWidget.PopularKeyword(),
                                        HomeWidget.PopularKeyword()
                                )
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Load popular keyword"){
                homeViewModel.getPopularKeywordData()
            }

            Then("Expect popular keyword will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is PopularKeywordListDataModel } != null
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.find { it is PopularKeywordListDataModel } != null
                                && (it.list.find { it is PopularKeywordListDataModel } as PopularKeywordListDataModel)?.popularKeywordList.isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})