package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.util.HomeCommandProcessor
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 06/11/20.
 */
class HomeViewModelProcessorTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private val homeCommandProcessor = mockk<HomeCommandProcessor>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    @Test
    fun `clear resource home processor`(){
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeProcessor = homeCommandProcessor)
        homeViewModel.onCloseChannel()
        verify (atLeast = 1){ homeCommandProcessor.onClear() }
    }
}