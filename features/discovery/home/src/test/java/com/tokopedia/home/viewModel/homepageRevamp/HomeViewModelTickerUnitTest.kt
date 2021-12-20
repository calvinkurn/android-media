package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelTickerUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Test Ticker is need to visible`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)

        // Data non ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf()
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).apply {
        }
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        // Expect ticker not show on user screen
        verifyOrder {
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.none { it::class.java == TickerDataModel::class.java }
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Test Ticker is visible`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(ticker),
                        isCache = false
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
        assert(homeViewModel.homeLiveDynamicChannel.value!!.list.filterIsInstance(TickerDataModel::class.java).isNotEmpty())
    }

    @Test
    fun `Test Remove Ticker`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(ticker)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
        homeViewModel.onCloseTicker()

        assert(homeViewModel.homeLiveDynamicChannel.value!!.list.filterIsInstance(TickerDataModel::class.java).isEmpty())
    }

    @Test
    fun `Test Refresh Ticker`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        isCache = false,
                        list = listOf(ticker)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        assert(homeViewModel.homeLiveDynamicChannel.value!!.list.filterIsInstance(TickerDataModel::class.java).isNotEmpty())
    }
}