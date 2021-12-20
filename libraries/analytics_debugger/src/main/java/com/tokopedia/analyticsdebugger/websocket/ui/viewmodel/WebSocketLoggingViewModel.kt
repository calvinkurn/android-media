package com.tokopedia.analyticsdebugger.websocket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.DeleteAllWebSocketLogUseCase
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.GetWebSocketLogUseCase
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.action.WebSocketLoggingAction
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.event.WebSocketLoggingEvent
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.state.WebSocketLogPagination
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.state.WebSocketLoggingState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.concurrent.Flow
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
class WebSocketLoggingViewModel @Inject constructor(
    private val getWebSocketLogUseCase: GetWebSocketLogUseCase,
    private val deleteAllWebSocketLogUseCase: DeleteAllWebSocketLogUseCase,
    private val dispatchers: CoroutineDispatchers,
): ViewModel() {

    private val _websocketLogPagination = MutableStateFlow(WebSocketLogPagination())
    private val _loading = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<WebSocketLoggingEvent>()

    val uiState: Flow<WebSocketLoggingState> = combine(
        _websocketLogPagination, _loading
    ) { websocketLogPagination, loading ->
        WebSocketLoggingState(
            webSocketLogPagination = websocketLogPagination,
            loading = loading,
        )
    }

    val uiEvent: Flow<WebSocketLoggingEvent>
        get() = _uiEvent

    fun submitAction(action: WebSocketLoggingAction) {
        when(action) {
            is WebSocketLoggingAction.SearchLogAction -> handleSearch(action.query)
            WebSocketLoggingAction.LoadNextPageAction -> handleLoadNextPage()
            WebSocketLoggingAction.DeleteAllLogAction -> handleDeleteAllLog()
        }
    }

    /**
     * Handling Action
     */
    private fun handleSearch(query: String) {
        viewModelScope.launchCatchError(block = {
            if(!_loading.value) {
                _loading.value = true

                val newPage = 0
                val webSocketLogList = getWebSocketLog(query, newPage)

                _loading.value = false
                _websocketLogPagination.value = _websocketLogPagination.value.copy(
                    webSocketLoggingList = webSocketLogList,
                    query = query,
                    page = newPage
                )
            }
        }) {
            emitMessage(it.message ?: "Something went wrong.")
        }
    }

    private fun handleLoadNextPage() {
        viewModelScope.launchCatchError(block = {
            if(_loading.value) {
                _loading.value = true

                val pagination = _websocketLogPagination.value
                val newPage = pagination.page + 1

                val webSocketLogList = getWebSocketLog(pagination.query, newPage)

                _loading.value = false
                _websocketLogPagination.value = _websocketLogPagination.value.copy(
                    webSocketLoggingList = webSocketLogList,
                    page = newPage
                )
            }
        }) {
            emitMessage(it.message ?: "Something went wrong.")
        }
    }

    private fun handleDeleteAllLog() {
        viewModelScope.launchCatchError(block = {
            deleteAllWebSocketLogUseCase.let {
                it.executeOnBackground()
            }

            emitMessage("Delete All Logs Success")
        }) {
            emitMessage(it.message ?: "Something went wrong.")
        }
    }

    /**
     * Utility
     */
    private suspend fun emitMessage(message: String) {
        _uiEvent.emit(
            WebSocketLoggingEvent.ShowInfoEvent(
                message = message
            )
        )
    }

    private suspend fun getWebSocketLog(query: String, page: Int): List<WebSocketLogUiModel> {
        return getWebSocketLogUseCase.let {
            it.setParam(query, page, PAGINATION_OFFSET)
            it.executeOnBackground()
        }
    }

    private companion object {
        const val PAGINATION_OFFSET = 20
    }
}