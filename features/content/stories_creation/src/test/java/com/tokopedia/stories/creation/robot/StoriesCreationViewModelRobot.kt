package com.tokopedia.stories.creation.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import com.tokopedia.stories.creation.view.viewmodel.StoriesCreationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.yield
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class StoriesCreationViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    repo: StoriesCreationRepository = mockk(relaxed = true),
    creationUploader: CreationUploader = mockk(relaxed = true),
) : Closeable {

    private val viewModel = StoriesCreationViewModel(
        repo = repo,
        creationUploader = creationUploader,
    )

    val maxStoriesConfig: StoriesCreationConfiguration.MaxStoriesConfig
        get() = viewModel.maxStoriesConfig

    val storyId: String
        get() = viewModel.storyId

    val selectedAccount: ContentAccountUiModel
        get() = viewModel.selectedAccount

    val productTag: List<ProductTagSectionUiModel>
        get() = viewModel.productTagSection

    val maxProductTag: Int
        get() = viewModel.maxProductTag

    val minVideoDuration: Int
        get() = viewModel.minVideoDuration

    val maxVideoDuration: Int
        get() = viewModel.maxVideoDuration

    val mediaFilePath: String
        get() = viewModel.mediaFilePath

    fun recordState(fn: suspend StoriesCreationViewModelRobot.() -> Unit): StoriesCreationUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: StoriesCreationUiState
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

    fun recordEvent(fn: suspend StoriesCreationViewModelRobot.() -> Unit): List<StoriesCreationUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<StoriesCreationUiEvent>()
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

    fun recordStateAndEvent(fn: suspend StoriesCreationViewModelRobot.() -> Unit): Pair<StoriesCreationUiState, List<StoriesCreationUiEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: StoriesCreationUiState
        val uiEvents = mutableListOf<StoriesCreationUiEvent>()
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

    suspend fun submitAction(action: StoriesCreationAction) = act {
        viewModel.submitAction(action)
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    private fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}
