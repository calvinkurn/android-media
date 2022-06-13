package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelTickerUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    @ExperimentalCoroutinesApi
    private lateinit var homeViewModel: HomeRevampViewModel
    @ExperimentalCoroutinesApi
    @Test
    fun `When onCloseTicker then homeDataModel should remove ticker data`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        TickerDataModel()
                )
        ))

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.onCloseTicker()
        homeViewModel.homeDataModel.findWidget<TickerDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(false)
                },
                actionOnNotFound = {
                    Assert.assertTrue(true)
                }
        )
    }
}