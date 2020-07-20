package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.helper.isError
import com.tokopedia.home.beranda.helper.isSuccess
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelStickyLoginUnitTest {
    private val getStickyLoginUseCase = mockk<StickyLoginUseCase>(relaxed = true)
    private val homeViewModel: HomeViewModel = createHomeViewModel(getStickyLoginUseCase = getStickyLoginUseCase)
    @Test
    fun `Get success data sticky`(){

        // Success data placeholder
        coEvery{ getStickyLoginUseCase.executeOnBackground() } returns StickyLoginTickerPojo.TickerResponse()

        // Get Sticky Content
        homeViewModel.getStickyContent()

        // Check data observer
        assert(homeViewModel.stickyLogin.value != null && homeViewModel.stickyLogin.value!!.isSuccess())
    }

    @Test
    fun `Get timeout exception sticky content`(){
        // Error data
        coEvery{ getStickyLoginUseCase.executeOnBackground() } throws TimeoutException()

        // Get Sticky Content
        homeViewModel.getStickyContent()

        // Check data observer
        assert(homeViewModel.stickyLogin.value != null && homeViewModel.stickyLogin.value!!.isError())
    }
}