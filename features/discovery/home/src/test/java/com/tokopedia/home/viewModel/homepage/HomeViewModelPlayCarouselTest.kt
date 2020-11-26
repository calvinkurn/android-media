package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.isError
import com.tokopedia.home.beranda.helper.isSuccess
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCarouselCardDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.play_common.domain.model.PlayToggleChannelEntity
import com.tokopedia.play_common.domain.model.PlayToggleChannelReminder
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 02/08/20.
 */

class HomeViewModelPlayCarouselTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private val getPlayWidgetUseCase = mockk<GetPlayWidgetUseCase>(relaxed = true)
    private val playToggleChannelReminderUseCase = mockk<PlayToggleChannelReminderUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `Get play data success`() {
        val playDataModel = PlayCarouselCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayBannerCarouselDataModel(title = "title", channelList = listOf(
                PlayBannerCarouselItemDataModel(channelId = "0")
        ))

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        // play data returns success
        getPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturn(playCardHome)

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayBannerUseCase = getPlayWidgetUseCase, playToggleChannelReminderUseCase = playToggleChannelReminderUseCase)

        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.isNotEmpty() == true)
        }

        // Update view triggered
        homeViewModel.updateBannerTotalView("0", "20 Juta")

        // Expect the view updated
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.first() as PlayBannerCarouselItemDataModel).countView == "20 Juta")
        }
    }

    @Test
    fun `Get play data success with invalid position`() {
        val playDataModel = PlayCarouselCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayBannerCarouselDataModel(title = "title", channelList = listOf(
                PlayBannerCarouselItemDataModel(channelId = "0")
        ))

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        // play data returns success
        getPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturn(playCardHome)

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayBannerUseCase = getPlayWidgetUseCase, playToggleChannelReminderUseCase = playToggleChannelReminderUseCase)

        homeViewModel.getPlayBannerCarousel(-1)

        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.isNotEmpty() == true)
        }

        // Update view triggered
        homeViewModel.updateBannerTotalView("0", "20 Juta")

        // Expect the view updated
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.first() as PlayBannerCarouselItemDataModel).countView == "20 Juta")
        }
    }

    @Test
    fun `Get play data error`() {
        val playDataModel = PlayCarouselCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayBannerCarouselDataModel(title = "title", channelList = listOf(
                PlayBannerCarouselItemDataModel(channelId = "0")
        ))

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        // play data returns success
        getPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturnError()

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayBannerUseCase = getPlayWidgetUseCase, playToggleChannelReminderUseCase = playToggleChannelReminderUseCase)
        homeViewModel.getPlayBannerCarousel(-1)
        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find { it::class.java == playDataModel::class.java } == null)
        }
    }

    @Test
    fun `Get play data toggle success`() {
        val playDataModel = PlayCarouselCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayBannerCarouselDataModel(title = "title", channelList = listOf(
                PlayBannerCarouselItemDataModel(channelId = "0")
        ))

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        coEvery { playToggleChannelReminderUseCase.executeOnBackground() } returns PlayToggleChannelEntity(
                playToggleChannelReminder = PlayToggleChannelReminder(
                        PlayToggleChannelReminder.PlayToggleChannelReminderHeader(
                                status = 200
                        )
                )
        )

        // play data returns success
        getPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturn(playCardHome)

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayBannerUseCase = getPlayWidgetUseCase, playToggleChannelReminderUseCase = playToggleChannelReminderUseCase)

        // toggle click
        homeViewModel.setToggleReminderPlayBanner("0", true)

        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.isNotEmpty() == true)
        }

        homeViewModel.reminderPlayLiveData.observeOnce { result ->
            assert(result.isSuccess() && result.data == true)
        }

        // Update view triggered
        homeViewModel.updateBannerTotalView("0", "20 Juta")

        // Expect the view updated
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.first() as PlayBannerCarouselItemDataModel).countView == "20 Juta")
        }
    }

    @Test
    fun `Get play data toggle fail and rollback`() {
        val playDataModel = PlayCarouselCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayBannerCarouselDataModel(title = "title", channelList = listOf(
                PlayBannerCarouselItemDataModel(channelId = "0")
        ))

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        coEvery { playToggleChannelReminderUseCase.executeOnBackground() } throws TimeoutException()

        // play data returns success
        getPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturn(playCardHome)

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayBannerUseCase = getPlayWidgetUseCase, playToggleChannelReminderUseCase = playToggleChannelReminderUseCase)

        // toggle click
        homeViewModel.setToggleReminderPlayBanner("0", true)

        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.isNotEmpty() == true)
        }

        homeViewModel.reminderPlayLiveData.observeOnce { result ->
            assert(result.isError())
        }

        // Update view triggered
        homeViewModel.updateBannerTotalView("0", "20 Juta")

        // Expect the view updated
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(((homeDataModel.list.find { it::class.java == playDataModel::class.java } as? PlayCarouselCardDataModel)?.playBannerCarouselDataModel?.channelList?.first() as PlayBannerCarouselItemDataModel).countView == "20 Juta")
        }
    }
}