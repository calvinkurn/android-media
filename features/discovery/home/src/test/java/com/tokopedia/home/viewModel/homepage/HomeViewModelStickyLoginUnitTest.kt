package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.helper.isError
import com.tokopedia.home.beranda.helper.isSuccess
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelStickyLoginUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getStickyLoginUseCase = mockk<StickyLoginUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel
    @Test
    fun `Get success data sticky`(){

        // Success data placeholder
        coEvery{ getStickyLoginUseCase.executeOnBackground() } returns StickyLoginTickerPojo.TickerResponse(
                StickyLoginTickerPojo(
                        listOf(
                                StickyLoginTickerPojo.TickerDetail(
                                        layout = StickyLoginConstant.LAYOUT_FLOATING
                                )
                        )
                )
        )

        // Get Sticky Content
        homeViewModel = createHomeViewModel(getStickyLoginUseCase = getStickyLoginUseCase)
        homeViewModel.getStickyContent()

        // Check data observer
        assert(homeViewModel.stickyLogin.value != null && homeViewModel.stickyLogin.value!!.isSuccess())
    }

    @Test
    fun `Get timeout exception sticky content`(){
        // Error data
        coEvery{ getStickyLoginUseCase.executeOnBackground() } throws TimeoutException()

        // Get Sticky Content
        homeViewModel = createHomeViewModel(getStickyLoginUseCase = getStickyLoginUseCase)
        homeViewModel.getStickyContent()

        // Check data observer
        assert(homeViewModel.stickyLogin.value != null && homeViewModel.stickyLogin.value!!.isError())
    }
}