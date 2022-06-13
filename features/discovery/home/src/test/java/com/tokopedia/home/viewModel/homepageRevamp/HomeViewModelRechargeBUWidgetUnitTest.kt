package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRechargeBuWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.WidgetSource
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Resa on 15/12/20.
 */

class HomeViewModelRechargeBUWidgetUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getHomeRechargeBuUseCase = mockk<HomeRechargeBuWidgetUseCase>(relaxed = true)

    @ExperimentalCoroutinesApi
    private lateinit var homeViewModel: HomeRevampViewModel

    val mockInitialChannel = ChannelModel(id = "0", groupId = "0")
    val mockChannel = ChannelModel(id = "1", groupId = "1")

    @ExperimentalCoroutinesApi
    @Test
    fun `When homeRechargeBuUseCase return success on getRechargeBUWidget then homeDataModel should be updated`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                RechargeBUWidgetDataModel(channel = mockInitialChannel)
        )))
        getHomeRechargeBuUseCase.givenOnGetRechargeBuWidgetFromHolderReturn(
                RechargeBUWidgetDataModel(channel = mockChannel)
        )
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeRechargeBuWidgetUseCase = getHomeRechargeBuUseCase
        )
        homeViewModel.getRechargeBUWidget(WidgetSource.FINANCE)
        homeViewModel.homeDataModel.findWidget<RechargeBUWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(
                            model.channel == mockChannel
                    )
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When homeRechargeBuUseCase throws error on getRechargeBUWidget then homeDataModel should not contains rechargeBuModel`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                RechargeBUWidgetDataModel(channel = mockInitialChannel)
        )))
        getHomeRechargeBuUseCase.givenOnGetRechargeBuWidgetFromHolderError()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeRechargeBuWidgetUseCase = getHomeRechargeBuUseCase
        )
        homeViewModel.getRechargeBUWidget(WidgetSource.FINANCE)
        homeViewModel.homeDataModel.findWidget<RechargeBUWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(false)
                },
                actionOnNotFound = {
                    Assert.assertTrue(true)
                }
        )
    }
}

