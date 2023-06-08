package com.tokopedia.play.broadcaster.robot

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_UNKNOWN
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSocketCredentialUseCase
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastUiState
import com.tokopedia.play.broadcaster.util.TestDoubleModelBuilder
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.TestUriParser
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.yield
import java.io.Closeable

/**
 * Created by jegul on 06/10/21
 */
internal class PlayBroadcastViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    handle: SavedStateHandle = SavedStateHandle(),
    private val mDataStore: PlayBroadcastDataStore = mockk(relaxed = true),
    hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
    sharedPref: HydraSharedPreferences = mockk(relaxed = true),
    getChannelUseCase: GetChannelUseCase = mockk(relaxed = true),
    getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    playBroadcastWebSocket: PlayWebSocket = mockk(relaxed = true),
    playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer(), TestUriParser(), mockk(relaxed = true)),
    playInteractiveMapper: PlayInteractiveMapper = PlayInteractiveMapper(TestHtmlTextTransformer()),
    productMapper: PlayBroProductUiMapper = PlayBroProductUiMapper(),
    channelRepo: PlayBroadcastRepository = mockk(relaxed = true),
    logger: PlayLogger = mockk(relaxed = true),
    broadcastTimer: PlayBroadcastTimer = mockk(relaxed = true),
    playShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig = mockk(relaxed = true),
    remoteConfig: RemoteConfig = mockk(relaxed = true),
) : Closeable {

    private val testDoubleModelBuilder = TestDoubleModelBuilder()

    init {
        coEvery { mDataStore.getSetupDataStore() } returns testDoubleModelBuilder.buildSetupDataStore(dispatcher = dispatchers)
    }

    private val viewModel = PlayBroadcastViewModel(
        handle,
        mDataStore,
        hydraConfigStore,
        sharedPref,
        getChannelUseCase,
        getAddedChannelTagsUseCase,
        getSocketCredentialUseCase,
        dispatchers,
        userSession,
        playBroadcastWebSocket,
        playBroadcastMapper,
        productMapper,
        playInteractiveMapper,
        channelRepo,
        logger,
        broadcastTimer,
        playShortsEntryPointRemoteConfig,
        remoteConfig,
    )

    fun recordState(fn: suspend PlayBroadcastViewModelRobot.() -> Unit): PlayBroadcastUiState {
        return recordStateAsList(fn).last()
    }

    fun recordStateAsList(fn: suspend PlayBroadcastViewModelRobot.() -> Unit): List<PlayBroadcastUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiStateList = mutableListOf<PlayBroadcastUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    fun recordEvent(fn: suspend PlayBroadcastViewModelRobot.() -> Unit): List<PlayBroadcastEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<PlayBroadcastEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvents.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEvents
    }

    fun recordStateAndEvents(fn: suspend PlayBroadcastViewModelRobot.() -> Unit): Pair<PlayBroadcastUiState, List<PlayBroadcastEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<PlayBroadcastEvent>()
        val uiStateList = mutableListOf<PlayBroadcastUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvents.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList.last() to uiEvents
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    fun getAccountConfiguration(authorType: String = TYPE_UNKNOWN) = viewModel.submitAction(PlayBroadcastAction.GetConfiguration(authorType))

    fun uploadTitle(title: String) {
        CoroutineScope(dispatchers.coroutineDispatcher).launch {
            mDataStore.getSetupDataStore().uploadTitle("", "", title)
        }
    }

    fun uploadCover() {
        CoroutineScope(dispatchers.coroutineDispatcher).launch {
            mDataStore.getSetupDataStore().setFullCover(
                PlayCoverUiModel(
                    croppedCover = CoverSetupState.Cropped.Uploaded(
                        localImage = mockk(relaxed = true),
                        coverImage = mockk(relaxed = true),
                        coverSource = CoverSource.None,
                    ),
                    state = SetupDataState.Uploaded
                )
            )
            mDataStore.getSetupDataStore().uploadSelectedCover("", "")
        }
    }

    fun setProduct(products: List<ProductTagSectionUiModel>) {
        viewModel.submitAction(PlayBroadcastAction.SetProduct(products))
    }

    fun setSchedule() {
        viewModel.submitAction(PlayBroadcastAction.SetSchedule(mockk()))
    }

    fun startLive() = viewModel.submitAction(
        PlayBroadcastAction.BroadcastStateChanged(
            PlayBroadcasterState.Started
        )
    )

    fun stopLive() = viewModel.submitAction(
        PlayBroadcastAction.BroadcastStateChanged(
            PlayBroadcasterState.Stopped
        )
    )

    suspend fun setPinned(message: String) = act {
        viewModel.submitAction(PlayBroadcastAction.SetPinnedMessage(message))
    }

    suspend fun editPinned() = act {
        viewModel.submitAction(PlayBroadcastAction.EditPinnedMessage)
    }

    suspend fun cancelEditPinned() = act {
        viewModel.submitAction(PlayBroadcastAction.CancelEditPinnedMessage)
    }

    suspend fun inputQuizOption(order: Int, text: String) = act {
        viewModel.submitAction(PlayBroadcastAction.InputQuizOption(order, text))
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    fun getViewModel() = viewModel

    fun <T> getViewModelPrivateField(name: String): T {
        val field = viewModel.javaClass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(viewModel) as T
    }

    fun executeViewModelPrivateFunction(name: String) {
        val method = viewModel.javaClass.getDeclaredMethod(name)
        method.isAccessible = true
        method.invoke(viewModel)
    }

    override fun close() {
        cancelRemainingTasks()
    }
}
