package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.newatf.HomeAtfUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeMissionWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRemoteConfigController
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by dhaba
 */
@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModelMissionWidgetUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val homeMissionWidgetUseCase = mockk<HomeMissionWidgetUseCase>(relaxed = true)
    private val homeRemoteConfigController = mockk<HomeRemoteConfigController>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val mockChannelModel = ChannelModel(id = "1", groupId = "2")
    private val mockFailedMissionWidget = MissionWidgetListDataModel(
        id = "123",
        status = MissionWidgetListDataModel.STATUS_ERROR,
        source = MissionWidgetListDataModel.SOURCE_DC,
    )
    private val mockSuccessMissionWidget = MissionWidgetListDataModel(
        id = "123",
        status = MissionWidgetListDataModel.STATUS_SUCCESS,
        source = MissionWidgetListDataModel.SOURCE_DC,
    )
    private val mockFailedMissionWidgetAtf = MissionWidgetListDataModel(
        id = "123",
        status = MissionWidgetListDataModel.STATUS_ERROR,
        source = MissionWidgetListDataModel.SOURCE_ATF,
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given failed mission widget when refresh data mission widget then get success data mission widget`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockFailedMissionWidget)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeMissionWidgetUseCase = homeMissionWidgetUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val isContainsMissionWidgetFailed =
            (homeViewModel.homeDataModel.list.find { it is MissionWidgetListDataModel } as MissionWidgetListDataModel).status == MissionWidgetListDataModel.STATUS_ERROR
        Assert.assertTrue(isContainsMissionWidgetFailed)

        homeMissionWidgetUseCase.givenOnMissionWidgetReturn(mockFailedMissionWidget, mockSuccessMissionWidget)
        homeViewModel.getMissionWidgetRefresh()

        val isContainsMissionWidgetSuccess =
            (homeViewModel.homeDataModel.list.find { it is MissionWidgetListDataModel } as MissionWidgetListDataModel).status == MissionWidgetListDataModel.STATUS_SUCCESS
        Assert.assertTrue(isContainsMissionWidgetSuccess)
    }

    @Test
    fun `given failed mission widget when refresh data mission widget when using new atf mechanism then get success data mission widget`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val homeAtfUseCase = mockk<HomeAtfUseCase>(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockFailedMissionWidgetAtf)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeMissionWidgetUseCase = homeMissionWidgetUseCase,
            homeRemoteConfigController = homeRemoteConfigController,
            homeAtfUseCase = homeAtfUseCase,
        )
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val isContainsMissionWidgetFailed =
            (homeViewModel.homeDataModel.list.find { it is MissionWidgetListDataModel } as MissionWidgetListDataModel).status == MissionWidgetListDataModel.STATUS_ERROR
        Assert.assertTrue(isContainsMissionWidgetFailed)

        homeViewModel.getMissionWidgetRefresh()

        coVerify { homeAtfUseCase.refreshData(mockFailedMissionWidgetAtf.id) }
    }
}
