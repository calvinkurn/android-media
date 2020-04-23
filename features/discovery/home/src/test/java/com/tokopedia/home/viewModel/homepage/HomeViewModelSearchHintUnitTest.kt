package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.interactor.GetKeywordSearchUseCase
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.coEvery
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.TimeoutException

class HomeViewModelSearchHintUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test Search hint"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val getKeywordSearchUseCase by memoized<GetKeywordSearchUseCase>()

        Scenario("Get success data search hint"){
            Given("Success data placeholder"){
                coEvery{ getKeywordSearchUseCase.executeOnBackground() } returns KeywordSearchData()
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
            }

            When("Get Search hint"){
                homeViewModel.getSearchHint(true)
            }

            Then("Check data observer"){
                assert(homeViewModel.searchHint.value != null)
            }
        }

        Scenario("Get timeout exception search hint"){
            Given("Success data placeholder"){
                coEvery{ getKeywordSearchUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
            }

            When("Get Search hint"){
                homeViewModel.getSearchHint(true)
            }

            Then("Check data observer"){
                assert(homeViewModel.searchHint.value == null)
            }
        }
    }
})