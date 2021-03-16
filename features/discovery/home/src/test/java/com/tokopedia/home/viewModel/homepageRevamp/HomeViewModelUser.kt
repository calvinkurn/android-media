package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelUser{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    @Test
    fun `Get user session with non login`(){
        // Set user session data with null
        every { userSessionInterface.userId } returns ""
        homeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)
        // Check data from viewModel
        assert(homeViewModel.getUserId().isEmpty())
    }

    @Test
    fun `Get user session with login user`(){
        // Set user session data with id
        every { userSessionInterface.userId } returns "234"
        homeViewModel = createHomeViewModel(userSessionInterface = userSessionInterface)
        // Check data from viewModel
        assert( homeViewModel.getUserId() == "234")
    }
}