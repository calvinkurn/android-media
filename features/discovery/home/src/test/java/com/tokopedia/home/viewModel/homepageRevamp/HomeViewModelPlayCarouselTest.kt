package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomePlayUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 02/08/20.
 */

class HomeViewModelPlayCarouselTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val getPlayUseCase = mockk<HomePlayUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val mockUseCaseReturnTitle = "UseCaseReturnTitle"
    private val mockPlayWidgetState = PlayWidgetState(
        model = PlayWidgetUiModel(
            title = "Title",
            actionAppLink = "ActionApplink",
            actionTitle = "",
            isActionVisible = true,
            config = PlayWidgetConfigUiModel(
                autoRefresh = true,
                autoPlay = true,
                autoPlayAmount = 0,
                autoRefreshTimer = 0L,
                maxAutoPlayCellularDuration = 0,
                maxAutoPlayWifiDuration = 0,
                businessWidgetPosition = 0
            ),
            background = PlayWidgetBackgroundUiModel(
                backgroundUrl = "",
                gradientColors = listOf(),
                overlayImageAppLink = "",
                overlayImageUrl = "",
                overlayImageWebLink = ""
            ),
            items = listOf()
        ),
        isLoading = false
    )

    private val mockPlayWidgetStateUseCaseReturn = PlayWidgetState(
        model = PlayWidgetUiModel(
            title = mockUseCaseReturnTitle,
            actionAppLink = "ActionApplink",
            actionTitle = "",
            isActionVisible = true,
            config = PlayWidgetConfigUiModel(
                autoRefresh = true,
                autoPlay = true,
                autoPlayAmount = 0,
                autoRefreshTimer = 0L,
                maxAutoPlayCellularDuration = 0,
                maxAutoPlayWifiDuration = 0,
                businessWidgetPosition = 0
            ),
            background = PlayWidgetBackgroundUiModel(
                backgroundUrl = "",
                gradientColors = listOf(),
                overlayImageAppLink = "",
                overlayImageUrl = "",
                overlayImageWebLink = ""
            ),
            items = listOf()
        ),
        isLoading = false
    )

    @ExperimentalCoroutinesApi
    @Test
    fun `given user not logged in when shouldUpdatePlayWidgetToggleReminder then trigger livedata with given paramater`(){
        val mockChannel = DynamicHomeChannel.Channels()
        val mockPlayWidgetState = PlayWidgetState(isLoading = true)

        val mockChannelId = "1"
        val mockReminderType = PlayWidgetReminderType.NotReminded
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)
                )
        ))
        coEvery { getUserSession.isLoggedIn } returns false
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, userSessionInterface = getUserSession)
        homeViewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, mockReminderType)
        homeViewModel.playWidgetReminderEvent.observeOnce {
            Assert.assertTrue(it.first == mockChannelId)
            Assert.assertTrue(it.second == mockReminderType)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given homePlayUseCase success when shouldUpdatePlayWidgetToggleReminder then trigger livedata with Result success reminderType`(){
        val mockChannel = DynamicHomeChannel.Channels()
        val mockPlayWidgetState = PlayWidgetState(isLoading = true)

        val mockChannelId = "1"
        val mockReminderType = PlayWidgetReminderType.NotReminded
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)
                )
        ))
        getPlayUseCase.givenOnUpdatePlayToggleReminderReturn(true)
        getPlayUseCase.givenOnGetPlayWidgetUiModelReturn(mockPlayWidgetState)

        coEvery { getUserSession.isLoggedIn } returns true
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = getUserSession,
                homePlayUseCase = getPlayUseCase
        )
        homeViewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, mockReminderType)
        homeViewModel.playWidgetReminderObservable.observeOnce {
            Assert.assertTrue(it.status == Result.Status.SUCCESS)
            Assert.assertTrue(it.data == mockReminderType)
        }
        homeViewModel.homeDataModel.findWidget<CarouselPlayWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(model.widgetState == mockPlayWidgetState)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given homePlayUseCase failed when shouldUpdatePlayWidgetToggleReminder then trigger livedata with Result error reminderType and homeDataModel updated`(){
        val mockChannel = DynamicHomeChannel.Channels()
        val mockPlayWidgetState = PlayWidgetState(isLoading = true)

        val mockChannelId = "1"
        val mockReminderType = PlayWidgetReminderType.NotReminded
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(
                        CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)
                )
        ))
        getPlayUseCase.givenOnUpdatePlayToggleReminderReturn(false)
        getPlayUseCase.givenOnGetPlayWidgetUiModelReturn(mockPlayWidgetState)
        coEvery { getUserSession.isLoggedIn } returns true
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = getUserSession,
                homePlayUseCase = getPlayUseCase
        )
        homeViewModel.shouldUpdatePlayWidgetToggleReminder(mockChannelId, mockReminderType)
        homeViewModel.playWidgetReminderObservable.observeOnce {
            Assert.assertTrue(it.status == Result.Status.ERROR)
        }

        homeViewModel.homeDataModel.findWidget<CarouselPlayWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(model.widgetState == mockPlayWidgetState)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given initial carousel data model when updatePlayWidgetTotalView then homeDataModel need to update carousel model with value from usecase`(){
        val mockChannel = DynamicHomeChannel.Channels()
        val mockChannelId = "1"
        val mockTotalView = "99"

        val mockInitialCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)
        val mockReturnCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetStateUseCaseReturn)

        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(mockInitialCarouselModel)
        ))
        getPlayUseCase.givenOnUpdatePlayTotalViewReturn(mockReturnCarouselModel)
        coEvery { getUserSession.isLoggedIn } returns true
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = getUserSession,
                homePlayUseCase = getPlayUseCase
        )
        homeViewModel.updatePlayWidgetTotalView(mockChannelId, mockTotalView)

        homeViewModel.homeDataModel.findWidget<CarouselPlayWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(model == mockReturnCarouselModel)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given initial carousel data model when onUpdateActionReminder then homeDataModel need to update carousel model with value from usecase`(){
        val mockChannel = DynamicHomeChannel.Channels()
        val mockChannelId = "1"

        val mockInitialCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)
        val mockReturnCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetStateUseCaseReturn)

        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(mockInitialCarouselModel)
        ))
        getPlayUseCase.givenOnUpdateActionReminderReturn(mockReturnCarouselModel)
        coEvery { getUserSession.isLoggedIn } returns true
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = getUserSession,
                homePlayUseCase = getPlayUseCase
        )
        homeViewModel.updatePlayWidgetReminder(mockChannelId, true)

        homeViewModel.homeDataModel.findWidget<CarouselPlayWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(model == mockReturnCarouselModel)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given getPlayWidget usecase success when getPlayWidgetWhenShouldRefresh then homeDataModel need to update carousel model with value from usecase`(){
        val mockChannel = DynamicHomeChannel.Channels()

        val mockInitialCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)
        val mockReturnCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetStateUseCaseReturn)

        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(mockInitialCarouselModel)
        ))
        getPlayUseCase.givenOnGetPlayWidgetWhenShouldRefreshReturn(mockPlayWidgetStateUseCaseReturn)
        coEvery { getUserSession.isLoggedIn } returns true
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = getUserSession,
                homePlayUseCase = getPlayUseCase
        )
        homeViewModel.getPlayWidgetWhenShouldRefresh()

        homeViewModel.homeDataModel.findWidget<CarouselPlayWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(model == mockReturnCarouselModel)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given getPlayWidget usecase failed when getPlayWidgetWhenShouldRefresh then homeDataModel need to remove carousel model`(){
        val mockChannel = DynamicHomeChannel.Channels()

        val mockInitialCarouselModel = CarouselPlayWidgetDataModel(mockChannel, mockPlayWidgetState)

        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(mockInitialCarouselModel)
        ))
        getPlayUseCase.givenOnGetPlayWidgetWhenShouldRefreshError()
        coEvery { getUserSession.isLoggedIn } returns true
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                userSessionInterface = getUserSession,
                homePlayUseCase = getPlayUseCase
        )
        homeViewModel.getPlayWidgetWhenShouldRefresh()

        homeViewModel.homeDataModel.findWidget<CarouselPlayWidgetDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(false)
                },
                actionOnNotFound = {
                    Assert.assertTrue(true)
                }
        )
    }
}