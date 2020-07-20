package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelUser{
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val homeViewModel: HomeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)
    @Test
    fun `Get user session with non login`(){
        // Set user session data with null
        every { userSessionInterface.userId } returns null

        // Check data from viewModel
        assert(homeViewModel.getUserId().isEmpty())
    }

    @Test
    fun `Get user session with login user`(){
        // Set user session data with id
        every { userSessionInterface.userId } returns "234"

        // Check data from viewModel
        assert(homeViewModel.getUserId().isNotEmpty() && homeViewModel.getUserId() == "234")
    }
}