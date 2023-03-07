package com.tokopedia.content.common.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.util.preference.ProductTagPreference
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class ProductTagViewModelRobot(
    productTagSourceRaw: String = "",
    shopBadge: String = "",
    authorId: String = "",
    authorType: String = "",
    initialSelectedProduct: List<SelectedProductUiModel> = emptyList(),
    productTagConfig: ContentProductTagConfig = mockk(relaxed = true),
    repo: ProductTagRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    sharedPref: ProductTagPreference = mockk(relaxed = true),
    private val dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
) : Closeable {

    private val viewModel = ProductTagViewModel(
        productTagSourceRaw = productTagSourceRaw,
        shopBadge = shopBadge,
        authorId = authorId,
        authorType = authorType,
        initialSelectedProduct = initialSelectedProduct,
        productTagConfig = productTagConfig,
        repo = repo,
        userSession = userSession,
        sharedPref = sharedPref,
    )

    fun setup(fn: suspend ProductTagViewModelRobot.() -> Unit) {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)

        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
    }

    fun recordState(fn: suspend ProductTagViewModelRobot.() -> Unit): ProductTagUiState {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: ProductTagUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordStateAsList(fn: suspend ProductTagViewModelRobot.() -> Unit): List<ProductTagUiState> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiStateList = mutableListOf<ProductTagUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    fun recordEvent(fn: suspend ProductTagViewModelRobot.() -> Unit): List<ProductTagUiEvent> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiEventList = mutableListOf<ProductTagUiEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEventList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEventList
    }

    fun recordStateAndEvent(fn: suspend ProductTagViewModelRobot.() -> Unit): Pair<ProductTagUiState, List<ProductTagUiEvent>> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: ProductTagUiState
        val uiEvents = mutableListOf<ProductTagUiEvent>()
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
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState to uiEvents
    }

    suspend fun submitAction(action: ProductTagAction) = act {
        viewModel.submitAction(action)
    }

    fun getViewModel() = viewModel

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}