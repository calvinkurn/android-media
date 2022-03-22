package com.tokopedia.home.viewModel.homepageRevamp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomePlayLiveDynamicRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelPlayTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @ExperimentalCoroutinesApi
    @Test
    fun `given channel Id when updateBannerTotalView then homeDataModel should update total view of that channel id`(){
        val mockChannelId = "1"
        val mockTotalView = "99"
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        PlayCardDataModel(DynamicHomeChannel.Channels(id = mockChannelId), PlayChannel(channelId = mockChannelId))
                )
        ))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateBannerTotalView(mockChannelId, mockTotalView)
        homeViewModel.homeDataModel.findWidget<PlayCardDataModel>(
                predicate = { it?.channel?.id == mockChannelId },
                actionOnFound = { model, index ->
                    Assert.assertTrue(model.playCardHome?.totalView == mockTotalView)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given null play card when updateBannerTotalView then homeDataModel should not changed`(){
        val mockChannelId = "1"
        val mockTotalView = "99"
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        PlayCardDataModel(DynamicHomeChannel.Channels(id = mockChannelId), null)
                )
        ))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateBannerTotalView(mockChannelId, mockTotalView)
        homeViewModel.homeDataModel.findWidget<PlayCardDataModel>(
                predicate = { it?.channel?.id == mockChannelId },
                actionOnFound = { model, index ->
                    Assert.assertTrue(model.playCardHome == null)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given null channelId when updateBannerTotalView then homeDataModel should not changed`(){
        val mockInitialChannelId = "1"
        val mockInitialTotalView = "0"
        val mockChannelId = null
        val mockTotalView = "99"
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        PlayCardDataModel(DynamicHomeChannel.Channels(id = mockInitialChannelId), PlayChannel(totalView = mockInitialTotalView))
                )
        ))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateBannerTotalView(mockChannelId, mockTotalView)
        homeViewModel.homeDataModel.findWidget<PlayCardDataModel>(
                predicate = { it?.channel?.id == mockInitialChannelId },
                actionOnFound = { model, index ->
                    Assert.assertTrue(model.playCardHome?.totalView == mockInitialTotalView)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given null totalView when updateBannerTotalView then homeDataModel should not changed`(){
        val mockInitialChannelId = "1"
        val mockInitialTotalView = "0"
        val mockChannelId = "1"
        val mockTotalView = null
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        PlayCardDataModel(DynamicHomeChannel.Channels(id = mockInitialChannelId), PlayChannel(totalView = mockInitialTotalView))
                )
        ))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateBannerTotalView(mockChannelId, mockTotalView)
        homeViewModel.homeDataModel.findWidget<PlayCardDataModel>(
                predicate = { it?.channel?.id == mockInitialChannelId },
                actionOnFound = { model, index ->
                    Assert.assertTrue(model.playCardHome?.totalView == mockInitialTotalView)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }
}