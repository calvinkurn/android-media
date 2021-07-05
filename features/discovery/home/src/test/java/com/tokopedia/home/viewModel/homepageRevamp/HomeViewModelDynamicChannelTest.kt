package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelDynamicChannelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getDynamicChannelsUseCase = mockk<GetDynamicChannelsUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Get dynamic channel data success with single data`() {
        val dataModel = DynamicChannelDataModel()
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")
        val dynamicChannel = DynamicHomeChannel.Channels(id = "2")
        val dynamicChannelViewModel = DynamicChannelDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        dynamicChannelViewModel.channel = dynamicChannel
        // dynamic banner almost expired time
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // dynamic data returns success
        getHomeUseCase.givenGetDynamicChannelsUseCase(
                dynamicChannelDataModels = listOf(dynamicChannelViewModel)
        )

        homeViewModel = createHomeViewModel( getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // viewModel load request update dynamic channel data
        homeViewModel.getDynamicChannelData(dataModel, 0)

        homeViewModel.homeDataModel.findWidgetList<DynamicChannelDataModel> {
            Assert.assertEquals(it.size, 1)
            Assert.assertEquals("2", it[0].channel!!.id)
        }
    }

    @Test
    fun `Get dynamic channel data success with multiple data`() {
        val dataModel = DynamicChannelDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")
        val dynamicChannel = DynamicHomeChannel.Channels(id = "2")
        val dynamicChannel2 = DynamicHomeChannel.Channels(id = "3")
        val dynamicChannelViewModel1 = DynamicChannelDataModel()
        val dynamicChannelViewModel2 = DynamicChannelDataModel()
        dynamicChannelViewModel1.channel = dynamicChannel
        dynamicChannelViewModel2.channel = dynamicChannel2

        // dynamic banner almost expired time
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // dynamic data returns success
        getHomeUseCase.givenGetDynamicChannelsUseCase(
                dynamicChannelDataModels = listOf(dynamicChannelViewModel1, dynamicChannelViewModel2)
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // viewModel load request update dynamic channel data") {
        homeViewModel.getDynamicChannelData(dataModel, 0)

        homeViewModel.homeDataModel.findWidgetList<DynamicChannelDataModel> {
            Assert.assertEquals(2, it.size)
            Assert.assertEquals("2", it[0].channel!!.id)
            Assert.assertEquals("3", it[1].channel!!.id)
        }
    }

    @Test
    fun `Get dynamic channel data success with empty data`() {
        val dataModel = DynamicChannelDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")

        // dynamic banner almost expired time
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // dynamic data returns success
        getHomeUseCase.givenGetDynamicChannelsUseCase(
                dynamicChannelDataModels = listOf()
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // viewModel load request update dynamic channel data
        homeViewModel.getDynamicChannelData(dataModel, 0)

        assert(homeViewModel.homeDataModel.list.find {it::class.java == DynamicChannelDataModel::class.java} == null)
    }

    @Test
    fun `Get dynamic channel data with visitable but data empty`() {
        val dataModel = DynamicChannelDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")

        // dynamic banner almost expired time
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // dynamic data returns success
        getHomeUseCase.givenGetDynamicChannelsUseCase(
                dynamicChannelDataModels = listOf()
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // viewModel load request update dynamic channel data
        homeViewModel.homeLiveData.value?.list?.find { it::class.java == dataModel::class.java }?.let {
            homeViewModel.getDynamicChannelData(it, ChannelModel(groupId = "1", id="1"), 0)
        }

        assert(homeViewModel.homeDataModel.list.find {it::class.java == DynamicChannelDataModel::class.java} == null)
    }

    @Test
    fun `Get dynamic channel data error`() {
        val dataModel = DynamicChannelDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        dataModel.channel = DynamicHomeChannel.Channels(id = "1")

        // dynamic banner almost expired time
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(dataModel)
                )
        )

        // dynamic data returns success
        getDynamicChannelsUseCase.givenGetDynamicChannelsUseCaseThrowReturn()

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // viewModel load request update dynamic channel data
        homeViewModel.getDynamicChannelData(dataModel, 0)

        homeViewModel.trackingLiveData.observeForever { assert(it != null) }

        assert(homeViewModel.homeDataModel.list.find {it::class.java == DynamicChannelDataModel::class.java} == null)
    }
}