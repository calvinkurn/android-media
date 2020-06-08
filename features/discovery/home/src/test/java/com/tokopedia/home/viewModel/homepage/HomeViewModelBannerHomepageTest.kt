package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.SendTopAdsUseCase
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@UseExperimental(ExperimentalCoroutinesApi::class)
class HomeViewModelBannerHomepageTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get Initial Data"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()
        Scenario("User doesn't have cache, and must get data from network. And should available on view"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Banner data "){
                val bannerDataModel = HomepageBannerDataModel()
                bannerDataModel.slides = listOf(
                        BannerSlidesModel()
                )
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(bannerDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }

    Feature("Get update data"){

        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()
        Scenario("User doesn't have cache, and must get data from network. And should available on view"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Banner data "){
                val bannerDataModel = HomepageBannerDataModel()
                bannerDataModel.slides = listOf(
                        BannerSlidesModel()
                )
                val newBannerDataModel = HomepageBannerDataModel()
                newBannerDataModel.slides = listOf(
                        BannerSlidesModel(),
                        BannerSlidesModel()
                )
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(bannerDataModel)
                        ),
                        HomeDataModel(
                                list = listOf(newBannerDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }



            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }

    Feature("Impression topads on banner"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        var url = ""
        val slotUrl = slot<String>()
        val sendTopAdsUseCase by memoized<SendTopAdsUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()
        Scenario("User doesn't have cache, and must get data from network. And should available on view"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Banner data "){
                val bannerDataModel = HomepageBannerDataModel()
                bannerDataModel.slides = listOf(
                        BannerSlidesModel(
                                topadsViewUrl = "coba topads"
                        )
                )
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(bannerDataModel)
                        )
                )
            }
            Given("set return impression"){
                every { sendTopAdsUseCase.executeOnBackground(capture(slotUrl)) } answers {
                    url = slotUrl.captured
                }
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect channel updated") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is HomepageBannerDataModel
                    })
                }
                confirmVerified(observerHome)
            }

            When("Impression topads called"){
                homeViewModel.sendTopAds("coba topads")
            }

            Then("Check the url is same"){
                Assert.assertTrue(url == "coba topads")
            }
        }
    }
})