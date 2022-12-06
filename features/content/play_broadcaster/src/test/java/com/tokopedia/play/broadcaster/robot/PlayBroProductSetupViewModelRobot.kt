package com.tokopedia.play.broadcaster.robot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductSummaryUiState
import com.tokopedia.play.broadcaster.setup.product.model.ProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
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
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class PlayBroProductSetupViewModelRobot(
    creationId: String = "",
    maxProduct: Int = 30,
    productSectionList: List<ProductTagSectionUiModel> = emptyList(),
    handle: SavedStateHandle = SavedStateHandle(),
    isEligibleForPin: Boolean = false,
    channelRepo: PlayBroadcastRepository = mockk(relaxed = true),
    hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
) : Closeable {

    private val viewModel = PlayBroProductSetupViewModel(
        creationId,
        maxProduct,
        productSectionList,
        handle,
        isEligibleForPin,
        channelRepo,
        userSession,
        dispatchers,
    )

    fun recordState(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): ProductChooserUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: ProductChooserUiState
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

    fun recordStateAsList(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): List<ProductChooserUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiStateList = mutableListOf<ProductChooserUiState>()
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

    fun recordStateAndEvent(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): Pair<ProductChooserUiState, List<PlayBroProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: ProductChooserUiState
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

    fun recordStateAsListAndEvent(fn: suspend PlayBroProductSetupViewModelRobot.() -> Unit): Pair<List<ProductChooserUiState>, List<PlayBroProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        var uiStates = mutableListOf<ProductChooserUiState>()
        val uiEvents = mutableListOf<PlayBroProductChooserEvent>()
        scope.launch {
            viewModel.uiState.collect {
                uiStates.add(it)
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
        return uiStates to uiEvents
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

    suspend fun submitAction(action: ProductSetupAction) = act {
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
