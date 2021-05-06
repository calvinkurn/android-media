package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 02/08/20.
 */

class HomeViewModelPlayCarouselTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val playWidgetTools = mockk<PlayWidgetTools>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val dispatchers = CoroutineTestDispatchersProvider

    @Test
    fun `Get play data from home skeleton`() {
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(CarouselPlayWidgetDataModel(
                                homeChannel = DynamicHomeChannel.Channels(),
                                widgetUiModel = PlayWidgetUiModel.Placeholder
                        )),
                        isProcessingAtf = false,
                        isCache = false
                )
        )

        playWidgetTools.givenPlayWidgetToolsReturn(PlayWidget(), dispatchers)

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, playWidgetTools = playWidgetTools)

        coVerify { playWidgetTools.getWidgetFromNetwork(PlayWidgetUseCase.WidgetType.Home, dispatchers.io) }
    }

    @Test
    fun `Get play data from home skeleton not available`() {
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(),
                        isCache = false
                )
        )

        playWidgetTools.givenPlayWidgetToolsReturn(PlayWidget(), dispatchers)

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, playWidgetTools = playWidgetTools)

        coVerify(exactly = 0) { playWidgetTools.getWidgetFromNetwork(PlayWidgetUseCase.WidgetType.Home, dispatchers.io) }

        confirmVerified(playWidgetTools)
    }
}