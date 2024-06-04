package com.tokopedia.content.product.picker.robot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.uimodel.PlayBroProductSummaryUiState
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserEvent
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserUiState
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSellerViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.yield
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class ContentProductPickerSellerViewModelRobot(
    creationId: String = "",
    maxProduct: Int = 30,
    productSectionList: List<ProductTagSectionUiModel> = emptyList(),
    handle: SavedStateHandle = SavedStateHandle(),
    isEligibleForPin: Boolean = false,
    repo: ContentProductPickerSellerRepository = mockk(relaxed = true),
    commonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    isNumerationShown: Boolean = true,
    fetchCommissionProduct: Boolean = false,
    selectedAccount: ContentAccountUiModel = ContentAccountUiModel.Empty,
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers
) : Closeable {

    private val viewModel = ContentProductPickerSellerViewModel(
        creationId,
        maxProduct,
        productSectionList,
        handle,
        isNumerationShown,
        isEligibleForPin,
        fetchCommissionProduct,
        selectedAccount,
        repo,
        commonRepo,
        userSession,
        dispatchers
    )

    fun recordState(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): ProductChooserUiState {
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

    fun recordStateAsList(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): List<ProductChooserUiState> {
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

    fun recordSummaryState(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): PlayBroProductSummaryUiState {
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

    fun recordSummaryStateAsList(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): List<PlayBroProductSummaryUiState> {
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

    fun recordEvent(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): List<ProductChooserEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<ProductChooserEvent>()
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

    fun recordStateAndEvent(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): Pair<ProductChooserUiState, List<ProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: ProductChooserUiState
        val uiEvents = mutableListOf<ProductChooserEvent>()
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

    fun recordStateAsListAndEvent(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): Pair<List<ProductChooserUiState>, List<ProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        var uiStates = mutableListOf<ProductChooserUiState>()
        val uiEvents = mutableListOf<ProductChooserEvent>()
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

    fun recordSummaryStateAndEvent(fn: suspend ContentProductPickerSellerViewModelRobot.() -> Unit): Pair<PlayBroProductSummaryUiState, List<ProductChooserEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroProductSummaryUiState
        val uiEvents = mutableListOf<ProductChooserEvent>()
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
