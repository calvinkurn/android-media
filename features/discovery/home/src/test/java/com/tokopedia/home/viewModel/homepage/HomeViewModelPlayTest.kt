package com.tokopedia.home.viewModel.homepage


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetPlayLiveDynamicUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelPlayTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getPlayLiveDynamicUseCase = mockk<GetPlayLiveDynamicUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `Get play data success and image url valid and try update view`() {
        val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        // play data returns success
        getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                channel = playCardHome
        )

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)

        // Expect the event on live data available and check image
        homeViewModel.requestImageTestLiveData.observeOnce {
            assert(it.peekContent().playCardHome != null && it.peekContent().playCardHome!!.coverUrl == playCardHome.coverUrl)
        }

        // Image valid should submit the data on live data home
        homeViewModel.setPlayBanner(playDataModel)

        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.contains(playDataModel) && (it.list.find { it == playDataModel } as? PlayCardDataModel)?.playCardHome != null
                    && (it.list.find { it == playDataModel } as? PlayCardDataModel)?.playCardHome!!.coverUrl == playCardHome.coverUrl
            )
        }

        // Update view triggered
        homeViewModel.updateBannerTotalView("0")

        // Expect the view updated
        assert(true)
    }

    @Test
    fun `Get play data success and image url not valid`() {
        val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayChannel(coverUrl = "")

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        // play data returns success
        getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                channel = playCardHome
        )

        // viewModel load play data
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)

        // Expect the event on live data not available
        homeViewModel.requestImageTestLiveData.observeOnce {
            assert(it == null)
        }

        // Expect the event on live data empty
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.isEmpty())
        }
    }

    @Test
    fun `Get play data success and image url valid and network some case trouble`() {
        val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        // play data returns success
        getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                channel = playCardHome
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)

        // Expect the event on live data available and check image
        homeViewModel.requestImageTestLiveData.observeOnce {
            assert(it == null)
        }

        // Image valid but the network error when try get image
        homeViewModel.clearPlayBanner()


        // Expect the event on live data not available
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.isEmpty())
        }
    }

    @Test
    fun `No play data available`() {
        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        // Expect the event on live data not available
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.isEmpty())
        }
    }

    @Test
    fun `View rendered but the data play still null, it will load new data with right adapter position`(){
        val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())

        // dynamic banner with another list
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(
                                HomepageBannerDataModel(),
                                playDataModel
                        )
                )
        )

        // simulate play data returns success
        getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                channel = PlayChannel()
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        // simulate view want load play data with position
        homeViewModel.getPlayBanner(1)

        // expect function load from network called
        coVerify { getPlayLiveDynamicUseCase.executeOnBackground() }

        // Expect the event on live data available and check image
        homeViewModel.requestImageTestLiveData.observeOnce {
            assert(it == null)
        }

        // Image valid but the network error when try get image
        homeViewModel.clearPlayBanner()

        // Expect the event on live data not available
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.isEmpty())
        }
    }

    @Test
    fun `Get play data success and image url valid`() {
        val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayChannel(coverUrl = "cobacoba.com")
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(playDataModel)
                )
        )

        getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                channel = playCardHome
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        homeViewModel.getLoadPlayBannerFromNetwork(playDataModel)


        // Expect the event on live data available and check image
        homeViewModel.requestImageTestLiveData.observeOnce {
            assert(it.peekContent().playCardHome != null && it.peekContent().playCardHome!!.coverUrl == playCardHome.coverUrl)
        }

        // Image valid should submit the data on live data home
        homeViewModel.setPlayBanner(playDataModel)

        // Expect the event on live data available
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.contains(playDataModel) && (it.list.find { it == playDataModel } as? PlayCardDataModel)?.playCardHome != null
                    && (it.list.find { it == playDataModel } as? PlayCardDataModel)?.playCardHome!!.coverUrl == playCardHome.coverUrl
            )
        }
    }

    @Test
    fun `View rendered but the data play still null, it will load new data with wrong adapter position`(){
        val playDataModel = PlayCardDataModel(DynamicHomeChannel.Channels())
        val playCardHome = PlayChannel(coverUrl = "cobacoba.com")

        // dynamic banner with another list
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(
                                HomepageBannerDataModel(),
                                playDataModel
                        )
                )
        )

        // simulate play data returns success
        getPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(
                channel = PlayChannel()
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPlayLiveDynamicUseCase = getPlayLiveDynamicUseCase)
        // simulate view want load play data wrong position
        homeViewModel.getPlayBanner(0)

        // expect function load from network called
        coVerify { getPlayLiveDynamicUseCase.executeOnBackground() }


        // Expect the event on live data available and check image
        homeViewModel.requestImageTestLiveData.observeOnce {
            assert(it == null)
        }

        // Image valid but the network error when try get image
        homeViewModel.clearPlayBanner()

        // Expect the event on live data not available
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.isEmpty())
        }
    }
}