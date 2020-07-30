package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelGeolocationUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeUseCase> (relaxed = true)
    private lateinit var homeViewModel: HomeViewModel
    @Test
    fun `Set geolocation permission true`(){
        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).apply {
            setNeedToShowGeolocationComponent(true)
        }
        // Set data
        homeViewModel.setGeolocationPermission(true)

        // Check must be true
        assert(homeViewModel.hasGeolocationPermission())
    }

    @Test
    fun `Set geolocation permission false`(){
        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).apply {
            setNeedToShowGeolocationComponent(true)
        }

        // Set data
        homeViewModel.setGeolocationPermission(false)

        // Check must be false
        assert(!homeViewModel.hasGeolocationPermission())
    }

    @Test
    fun `Test Geolocation is need to visible`(){
        val geolocation = GeoLocationPromptDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Data with geolocation
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(geolocation)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).apply {
            setNeedToShowGeolocationComponent(true)
        }
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Send geolocation
        homeViewModel.sendGeolocationData()

        // Expect geolocation will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match {
                it.list.isNotEmpty()
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Geolocation is need to visible and close it`(){
        val geolocation = GeoLocationPromptDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Data with geolocation
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(geolocation)
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).apply {
            setNeedToShowGeolocationComponent(true)
        }
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Send geolocation
        homeViewModel.sendGeolocationData()

        // Close geolocation
        homeViewModel.onCloseGeolocation()

        // Expect geolocation will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find{ it is GeoLocationPromptDataModel} != null
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find{ it is GeoLocationPromptDataModel} == null
            })
        }
        confirmVerified(observerHome)
    }
}