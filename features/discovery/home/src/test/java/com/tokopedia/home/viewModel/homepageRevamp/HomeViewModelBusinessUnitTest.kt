package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBusinessUnitUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelBusinessUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
    private val homeBusinessUnitUseCase = mockk<HomeBusinessUnitUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Get Tab Data success && bu data success with success data`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val position = 0

        // load tab data return success
        val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel(
            channelModel = ChannelModel(
                id = "2222",
                groupId = "",
                style = ChannelStyle.ChannelHome,
                channelHeader = ChannelHeader(name = "Selalu bisa topup dan Liburan"),
                channelConfig = ChannelConfig(layout = "bu_widget"),
                layout = "bu_widget"
            ),
            tabList = homeWidget.tabBusinessList
        )

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(businessUnitDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeBusinessUnitUseCase = homeBusinessUnitUseCase
        )
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
        homeViewModel.homeDataModel.addWidgetModel(businessUnitDataModel)

        homeBusinessUnitUseCase.givenGetBusinessWidgetTabUseCaseReturn(businessUnitDataModel)

        // load data bu return success
        val listBusinessUnitItem = listOf(
            BusinessUnitItemDataModel(
                content = HomeWidget.ContentItemTab(),
                itemPosition = position,
                tabPosition = position,
                tabName = "Keuangan"
            )
        )
        val listBusinessUnit = listOf(BusinessUnitDataModel(
            tabName = "Keuangan",
            tabPosition = position,
            list = listBusinessUnitItem
        ))
        val resultBuModel = businessUnitDataModel.copy(
            contentsList = listBusinessUnit
        )
        homeBusinessUnitUseCase.givenGetBusinessUnitDataUseCaseReturn(
            position,
            resultBuModel,
            position,
            homeViewModel.homeDataModel,
            businessUnitDataModel,
            position,
            listBusinessUnit[0].tabName
        )

        // viewModel load business tab
        homeViewModel.getBusinessUnitTabData(position)

        // viewModel load business data
        homeViewModel.getBusinessUnitData(position, position, listBusinessUnit[0].tabName)

        homeViewModel.homeDataModel.findWidget<NewBusinessUnitWidgetDataModel>(
                actionOnFound = { widget, index ->
                    Assert.assertNotNull(widget.tabList)
                    Assert.assertNotNull(widget.contentsList)
                    Assert.assertNotNull(widget.contentsList?.first()?.list)
                },
                actionOnNotFound = {}
        )
    }

    @Test
    fun `Get Tab Data success && bu data success with default data`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val tabName = ""
        val position = 0

        // load tab data return success
        val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel(
            channelModel = ChannelModel(
                id = "2222",
                groupId = "",
                style = ChannelStyle.ChannelHome,
                channelHeader = ChannelHeader(name = "Selalu bisa topup dan Liburan"),
                channelConfig = ChannelConfig(layout = "bu_widget"),
                layout = "bu_widget"
            ),
            tabList = homeWidget.tabBusinessList
        )

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(businessUnitDataModel)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeBusinessUnitUseCase = homeBusinessUnitUseCase
        )
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
        homeViewModel.homeDataModel.addWidgetModel(businessUnitDataModel)

        homeBusinessUnitUseCase.givenGetBusinessWidgetTabUseCaseReturn(businessUnitDataModel)

        homeBusinessUnitUseCase.givenGetBusinessUnitDataUseCaseReturn(
            position,
            businessUnitDataModel,
            position,
            homeViewModel.homeDataModel,
            businessUnitDataModel,
            position,
            tabName
        )

        // viewModel load business tab
        homeViewModel.getBusinessUnitTabData(position)

        // viewModel load business data
        homeViewModel.getBusinessUnitData(position, position, tabName)

        homeViewModel.homeDataModel.findWidget<NewBusinessUnitWidgetDataModel>(
                actionOnFound = { widget, index ->
                    Assert.assertEquals(widget, businessUnitDataModel)
                },
                actionOnNotFound = {

                }
        )
    }
}