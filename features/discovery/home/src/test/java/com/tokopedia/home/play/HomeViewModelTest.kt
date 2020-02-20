package com.tokopedia.home.play


import com.tokopedia.home.beranda.domain.interactor.GetPlayLiveDynamicUseCase
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomeViewModelTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get play data") {
        lateinit var homeViewModel: HomeViewModel
        val mockUserId = "12345"
        val getPlayLiveDynamicUseCase: GetPlayLiveDynamicUseCase = mockk(relaxed = true)
        Scenario("Get play data success and image valid") {

            Given("home presenter") {
                homeViewModel = createPresenter()
            }

            Given("play data returns success"){
//                returns getPlayLiveDynamicUseCase
//
//                every { getPlayLiveDynamicUseCase.execute() } returns flow {
//                    emit(listOf(PlayChannel()))
//                }
            }

            When("presenter load play data"){
//                homeViewModel.loadPlayBannerFromNetwork(PlayCardViewModel(DynamicHomeChannel.Channels()))
            }

            Then("Expect the function called"){
//                verify{ getPlayLiveDynamicUseCase.execute() }
            }
        }
    }
})