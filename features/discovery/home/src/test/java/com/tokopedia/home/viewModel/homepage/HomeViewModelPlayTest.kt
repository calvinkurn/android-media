package com.tokopedia.home.viewModel.homepage


import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetPlayLiveDynamicUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class HomeViewModelPlayTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get play data") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()
        Scenario("Get play data success and image url valid") {
            val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
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
                homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)
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
                    assert(it.list.contains(playDataModel) && (it.list.find { it == playDataModel } as? PlayCardDataModel)?.playCardHome != null
                            && (it.list.find { it == playDataModel } as? PlayCardDataModel)?.playCardHome!!.coverUrl == playCardHome.coverUrl
                    )
                }
            }
        }

        Scenario("Get play data success and image url not valid") {
            val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
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
                homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)
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
            val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
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
                homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)
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

        Scenario("View rendered but the data play still null, it will load new data with right adapter position"){
            val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
            val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

            Given("dynamic banner with another list"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(
                                        HomepageBannerDataModel(),
                                        playDataModel
                                )
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }

            Given("simulate play data returns success"){
                getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                        channel = PlayChannel()
                )
            }

            When("simulate view want load play data with position"){
                homeViewModel.getPlayBanner(1)
            }

            Then("expect function load from network called"){
                coVerify { getPlayLiveDynamicUseCase.executeOnBackground() }
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

        Scenario("View rendered but the data play still null, it will load new data with wrong adapter position"){
            val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
            val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

            Given("dynamic banner with another list"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(
                                        HomepageBannerDataModel(),
                                        playDataModel
                                )
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }

            Given("simulate play data returns success"){
                getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                        channel = PlayChannel()
                )
            }

            When("simulate view want load play data wrong position"){
                homeViewModel.getPlayBanner(0)
            }

            Then("expect function load from network called"){
                coVerify { getPlayLiveDynamicUseCase.executeOnBackground() }
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
    }
})