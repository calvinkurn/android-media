package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.interactor.GetKeywordSearchUseCase
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

@ExperimentalCoroutinesApi
class HomeViewModelSearchHintUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getKeywordSearchUseCase = mockk<GetKeywordSearchUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Get success data search hint`(){
        // Success data placeholder
        coEvery{ getKeywordSearchUseCase.executeOnBackground() } returns KeywordSearchData()

        // Get Search hint
        homeViewModel = createHomeViewModel(getKeywordSearchUseCase = getKeywordSearchUseCase)
        homeViewModel.getSearchHint(true)

        // Check data observer
        assert(homeViewModel.searchHint.value != null)
    }

    @Test
    fun `Get timeout exception search hint`(){
        // Error data placeholder
        coEvery{ getKeywordSearchUseCase.executeOnBackground() } throws TimeoutException()

        // Get Search hint
        homeViewModel = createHomeViewModel(getKeywordSearchUseCase = getKeywordSearchUseCase)
        homeViewModel.getSearchHint(true)

        // Check data observer
        assert(homeViewModel.searchHint.value == null)
    }
}