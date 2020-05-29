package com.tokopedia.home.viewModel.homepage

import android.content.Context
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
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

    Feature("Get Initial Data and try click topads"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()
        Scenario("User doesn't have cache, and must get data from network. And should available on view"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val bannerDataModel = HomepageBannerDataModel()
            val slidesModel = BannerSlidesModel()
            bannerDataModel.slides = listOf(
                    slidesModel
            )
            Given("Banner data "){
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
        val topAdsUrlHitter by memoized<TopAdsUrlHitter>()
        val getHomeUseCase by memoized<HomeUseCase>()
        val context by memoized<Context>()
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
                every { topAdsUrlHitter.hitImpressionUrl(any(), capture(slotUrl)) } answers {
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
                topAdsUrlHitter.hitImpressionUrl(context,"coba topads")
            }

            Then("Check the url is same"){
                Assert.assertTrue(url == "coba topads")
            }
        }
    }
})