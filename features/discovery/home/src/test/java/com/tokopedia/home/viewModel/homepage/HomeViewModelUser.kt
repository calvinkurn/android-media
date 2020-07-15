package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomeViewModelUser : Spek({
    InstantTaskExecutorRuleSpek(this)
    Feature("Test get user session id"){
        lateinit var homeViewModel: HomeViewModel
        val userSessionInterface by memoized<UserSessionInterface>()
        createHomeViewModelTestInstance()

        Scenario("Get user session with non login"){
            Given("Set user session data with null"){
                every { userSessionInterface.userId } returns null
            }

            Then("Check data from viewModel"){
                assert(homeViewModel.getUserId().isEmpty())
            }
        }

        Scenario("Get user session with login user"){
            Given("Set user session data with null"){
                every { userSessionInterface.userId } returns "234"
            }

            Then("Check data from viewModel"){
                assert(homeViewModel.getUserId().isNotEmpty() && homeViewModel.getUserId() == "234")
            }
        }
    }
})