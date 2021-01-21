package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.util.HomeCommandProcessor
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 06/11/20.
 */
class HomeViewModelProcessorTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeUseCase> (relaxed = true)
    private val homeCommandProcessor = mockk<HomeCommandProcessor>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel
    @Test
    fun `clear resource home processor`(){
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeProcessor = homeCommandProcessor)
        homeViewModel.onCloseChannel()
        verify (atLeast = 1){ homeCommandProcessor.onClear() }
    }
}