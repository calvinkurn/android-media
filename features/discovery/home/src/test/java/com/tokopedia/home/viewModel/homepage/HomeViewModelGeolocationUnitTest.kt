package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


@ExperimentalCoroutinesApi
class HomeViewModelGeolocationUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test geolocation") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getHomeUseCase by memoized<HomeUseCase>()

        Scenario("Test Geolocation is need to visible") {
            val geolocation = GeoLocationPromptDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

            Given("Data with geolocation") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(geolocation)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel().apply {
                    setNeedToShowGeolocationComponent(true)
                }
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Then("Expect geolocation will show on user screen") {
                verifyOrder {
                    // check on home data initial first channel is dynamic channel
                    observerHome.onChanged(match {
                        it.list.isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})