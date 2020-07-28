package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.helper.isError
import com.tokopedia.home.beranda.helper.isSuccess
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import io.mockk.coEvery
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.TimeoutException

class HomeViewModelStickyLoginUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test Search hint"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val getStickyLoginUseCase by memoized<StickyLoginUseCase>()
        Scenario("Get success data sticky"){
            Given("Success data placeholder"){
                coEvery{ getStickyLoginUseCase.executeOnBackground() } returns StickyLoginTickerPojo.TickerResponse()
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
            }

            When("Get Sticky Content"){
                homeViewModel.getStickyContent()
            }

            Then("Check data observer"){
                assert(homeViewModel.stickyLogin.value != null && homeViewModel.stickyLogin.value!!.isSuccess())
            }
        }

        Scenario("Get timeout exception sticky content"){
            Given("Error data"){
                coEvery{ getStickyLoginUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
            }

            When("Get Sticky Content"){
                homeViewModel.getStickyContent()
            }

            Then("Check data observer"){
                assert(homeViewModel.stickyLogin.value != null && homeViewModel.stickyLogin.value!!.isError())
            }
        }
    }
})