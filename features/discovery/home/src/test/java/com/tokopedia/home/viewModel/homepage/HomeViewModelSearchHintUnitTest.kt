package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.interactor.GetKeywordSearchUseCase
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelSearchHintUnitTest {
    private val getKeywordSearchUseCase = mockk<GetKeywordSearchUseCase>(relaxed = true)
    private val homeViewModel: HomeViewModel = createHomeViewModel(getKeywordSearchUseCase = getKeywordSearchUseCase)
    @Test
    fun `Get success data search hint`(){
        // Success data placeholder
        coEvery{ getKeywordSearchUseCase.executeOnBackground() } returns KeywordSearchData()

        // Get Search hint
        homeViewModel.getSearchHint(true)

        // Check data observer
        assert(homeViewModel.searchHint.value != null)
    }

    @Test
    fun `Get timeout exception search hint`(){
        // Error data placeholder
        coEvery{ getKeywordSearchUseCase.executeOnBackground() } throws TimeoutException()

        // Get Search hint
        homeViewModel.getSearchHint(true)

        // Check data observer
        assert(homeViewModel.searchHint.value == null)
    }
}