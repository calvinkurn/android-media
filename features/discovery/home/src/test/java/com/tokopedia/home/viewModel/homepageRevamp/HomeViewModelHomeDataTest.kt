package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelLoadingModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelRetryModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 06/11/20.
 */
class HomeViewModelHomeDataTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `error pagination home data`() {
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(DynamicChannelRetryModel(true), DynamicChannelLoadingModel()),
                        isCache = true
                )
        )
        coEvery { getHomeUseCase.updateHomeData() } returns flow{
            emit(Result.errorPagination(Throwable(), ""))
        }
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        verify { getHomeUseCase.updateHomeData() }
        assert( homeViewModel.homeLiveData.value?.list?.find { it is DynamicChannelLoadingModel } != null )
        assert( homeViewModel.homeLiveData.value?.list?.find { it is DynamicChannelRetryModel } != null )
    }

    @Test
    fun `error pagination but no retry model`() {
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(DynamicChannelLoadingModel()),
                        isCache = true
                )
        )
        coEvery { getHomeUseCase.updateHomeData() } returns flow{
            emit(Result.errorPagination(Throwable(), ""))
        }
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.refreshHomeData()

        verify { getHomeUseCase.updateHomeData() }
        assert( homeViewModel.homeLiveData.value?.list?.find { it is DynamicChannelLoadingModel } != null )
        assert( homeViewModel.homeLiveData.value?.list?.find { it is DynamicChannelRetryModel }== null )
    }
}