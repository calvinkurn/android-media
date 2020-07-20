package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelTickerUnitTest {
    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `Test Ticker is need to visible`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Data non ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).apply {
            setNeedToShowGeolocationComponent(true)
        }
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect ticker not show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty()
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Test Ticker is visible`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(ticker)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel()
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect ticker not show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.contains(ticker)
            })
        }
        confirmVerified(observerHome)
    }
}