package com.tokopedia.play.broadcaster.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductSummaryUiState
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class PlayBroProductSetupViewModelRobot(
    productSectionList: List<ProductTagSectionUiModel> = emptyList(),
    channelRepo: PlayBroadcastRepository = mockk(relaxed = true),
    hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
) : Closeable {

    private val viewModel = PlayBroProductSetupViewModel(
        productSectionList,
        channelRepo,
        hydraConfigStore,
        userSession,
        dispatchers,
    )

    fun recordState(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): PlayBroProductChooserUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroProductChooserUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordStateAsList(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): List<PlayBroProductChooserUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiStateList = mutableListOf<PlayBroProductChooserUiState>()
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

    fun recordSummaryState(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): PlayBroProductSummaryUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroProductSummaryUiState
        scope.launch {
            viewModel.summaryUiState.collect {
                uiState = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordSummaryStateAsList(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): List<PlayBroProductSummaryUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiStateList = mutableListOf<PlayBroProductSummaryUiState>()
        scope.launch {
            viewModel.summaryUiState.collect {
                uiStateList.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    fun recordEvent(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): List<PlayBroProductChooserEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<PlayBroProductChooserEvent>()
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

    fun recordStateAndEvent(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): Pair<PlayBroProductChooserUiState, List<PlayBroProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroProductChooserUiState
        val uiEvents = mutableListOf<PlayBroProductChooserEvent>()
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
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
        return uiState to uiEvents
    }

    fun recordSummaryStateAndEvent(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): Pair<PlayBroProductSummaryUiState, List<PlayBroProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroProductSummaryUiState
        val uiEvents = mutableListOf<PlayBroProductChooserEvent>()
        scope.launch {
            viewModel.summaryUiState.collect {
                uiState = it
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
        return uiState to uiEvents
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    suspend fun submitAction(action: ProductSetupAction) {
        viewModel.submitAction(action)
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