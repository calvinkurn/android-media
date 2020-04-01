package com.tokopedia.home.viewModel


import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetPlayLiveDynamicUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class HomeViewModelTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get play data") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()
        Scenario("Get play data success and image url valid") {
            val playDataModel = PlayCardViewModel(DynamicHomeChannel.Channels())
            val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

            Given("dynamic banner"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(playDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }


            Given("play data returns success"){
                getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                    channel = playCardHome
                )
            }

            When("viewModel load play data"){
                homeViewModel.loadPlayBannerFromNetwork(playDataModel)
            }

            Then("Expect the event on live data available and check image"){
                homeViewModel.requestImageTestLiveData.observeOnce {
                    assert(it.peekContent().playCardHome != null && it.peekContent().playCardHome!!.coverUrl == playCardHome.coverUrl)
                }
            }

            When("Image valid should submit the data on live data home"){
                homeViewModel.setPlayBanner(playDataModel)
            }

            Then("Expect the event on live data available"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(playDataModel) && (it.list.find { it == playDataModel } as? PlayCardViewModel)?.playCardHome != null
                            && (it.list.find { it == playDataModel } as? PlayCardViewModel)?.playCardHome!!.coverUrl == playCardHome.coverUrl
                    )
                }
            }
        }

        Scenario("Get play data success and image url not valid") {
            val playDataModel = PlayCardViewModel(DynamicHomeChannel.Channels())
            val playCardHome = PlayChannel(coverUrl = "")

            Given("dynamic banner"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(playDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }


            Given("play data returns success"){
                getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                        channel = playCardHome
                )
            }

            When("viewModel load play data"){
                homeViewModel.loadPlayBannerFromNetwork(playDataModel)
            }

            Then("Expect the event on live data not available"){
                homeViewModel.requestImageTestLiveData.observeOnce {
                    assert(it == null)
                }
            }

            Then("Expect the event on live data empty"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.isEmpty())
                }
            }
        }

        Scenario("Get play data success and image url valid and network some case trouble") {
            val playDataModel = PlayCardViewModel(DynamicHomeChannel.Channels())
            val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

            Given("dynamic banner"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(playDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }


            Given("play data returns success"){
                getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                        channel = playCardHome
                )
            }

            When("viewModel load play data"){
                homeViewModel.loadPlayBannerFromNetwork(playDataModel)
            }

            Then("Expect the event on live data available and check image"){
                homeViewModel.requestImageTestLiveData.observeOnce {
                    assert(it == null)
                }
            }

            When("Image valid but the network error when try get image"){
                homeViewModel.clearPlayBanner()
            }

            Then("Expect the event on live data not available"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.isEmpty())
                }
            }
        }


        Scenario("No play data available") {
            Given("dynamic banner"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf()
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }

            Then("Expect the event on live data not available"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.isEmpty())
                }
            }
        }
    }
})