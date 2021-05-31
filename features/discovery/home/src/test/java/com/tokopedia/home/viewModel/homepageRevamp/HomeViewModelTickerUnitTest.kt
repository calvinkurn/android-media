package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelTickerUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

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
        }
        homeViewModel.homeLiveData.observeForever(observerHome)

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
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(ticker),
                        isCache = false
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)
        assert(homeViewModel.homeLiveData.value!!.list.filterIsInstance(TickerDataModel::class.java).isNotEmpty())
    }

    @Test
    fun `Test Remove Ticker`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(ticker)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.onCloseTicker()

        assert(homeViewModel.homeLiveData.value!!.list.filterIsInstance(TickerDataModel::class.java).isEmpty())
    }

    @Test
    fun `Test Refresh Ticker`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val ticker = TickerDataModel()
        // Data with ticker
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        isCache = false,
                        list = listOf(ticker)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        assert(homeViewModel.homeLiveData.value!!.list.filterIsInstance(TickerDataModel::class.java).isNotEmpty())
    }
}