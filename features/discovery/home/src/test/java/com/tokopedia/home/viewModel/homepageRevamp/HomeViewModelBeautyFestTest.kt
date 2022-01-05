package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBeautyFestUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeViewModelBeautyFestTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
    private val homeBeautyFestUseCase = mockk<HomeBeautyFestUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Get beauty fest use case with success result then show pink header`(){
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel(
            channelModel = ChannelModel(
                id = "129362",
                groupId = "",
                style = ChannelStyle.ChannelHome,
                channelHeader = ChannelHeader(name = "Selalu bisa topup dan Liburan"),
                channelConfig = ChannelConfig(layout = "bu_widget"),
                layout = "bu_widget"
            )
        )

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(businessUnitDataModel)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeBeautyFestUseCase = homeBeautyFestUseCase
        )
        homeBeautyFestUseCase.givenGetBeautyFestUseCaseReturnTrue(homeViewModel.homeDataModel.list)

        homeViewModel.getBeautyFest(homeViewModel.homeDataModel.list)

        val beautyFestEvent = homeViewModel.beautyFestLiveData.value
        Assert.assertEquals(beautyFestEvent, HomeRevampFragment.BEAUTY_FEST_TRUE)
    }

    @Test
    fun `Get beauty fest use case with false result then show green header`(){
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel(
            channelModel = ChannelModel(
                id = "129362",
                groupId = "",
                style = ChannelStyle.ChannelHome,
                channelHeader = ChannelHeader(name = "Selalu bisa topup dan Liburan"),
                channelConfig = ChannelConfig(layout = "bu_widget"),
                layout = "bu_widget"
            )
        )

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(businessUnitDataModel)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeBeautyFestUseCase = homeBeautyFestUseCase
        )
        homeBeautyFestUseCase.givenGetBeautyFestUseCaseReturnFalse(homeViewModel.homeDataModel.list)

        homeViewModel.getBeautyFest(homeViewModel.homeDataModel.list)

        val beautyFestEvent = homeViewModel.beautyFestLiveData.value
        Assert.assertEquals(beautyFestEvent, HomeRevampFragment.BEAUTY_FEST_FALSE)
    }

    @Test
    fun `Get beauty fest use case with not set result then show shimmer`(){
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel(
            channelModel = ChannelModel(
                id = "129362",
                groupId = "",
                style = ChannelStyle.ChannelHome,
                channelHeader = ChannelHeader(name = "Selalu bisa topup dan Liburan"),
                channelConfig = ChannelConfig(layout = "bu_widget"),
                layout = "bu_widget"
            )
        )

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(businessUnitDataModel)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeBeautyFestUseCase = homeBeautyFestUseCase
        )
        homeBeautyFestUseCase.givenGetBeautyFestUseCaseReturnNotSet(homeViewModel.homeDataModel.list)

        homeViewModel.getBeautyFest(homeViewModel.homeDataModel.list)

        val beautyFestEvent = homeViewModel.beautyFestLiveData.value
        Assert.assertEquals(beautyFestEvent, HomeRevampFragment.BEAUTY_FEST_NOT_SET)
    }
}