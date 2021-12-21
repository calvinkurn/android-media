package com.tokopedia.analyticsdebugger.websocket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.DeleteAllWebSocketLogUseCase
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.GetWebSocketLogUseCase
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLog
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPlaceHolder
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.action.WebSocketLoggingAction
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.event.WebSocketLoggingEvent
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.helper.UiString
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.state.WebSocketLogPagination
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.state.WebSocketLoggingState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
class WebSocketLoggingViewModel @Inject constructor(
    private val getWebSocketLogUseCase: GetWebSocketLogUseCase,
    private val deleteAllWebSocketLogUseCase: DeleteAllWebSocketLogUseCase,
    dispatchers: CoroutineDispatchers,
): ViewModel() {

    private val _websocketLogPagination = MutableStateFlow(WebSocketLogPagination())
    private val _loading = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<WebSocketLoggingEvent>()

    val uiState: Flow<WebSocketLoggingState> = combine(
        _websocketLogPagination,
        _loading
    ) { websocketLogPagination, loading ->
        WebSocketLoggingState(
            webSocketLogPagination = websocketLogPagination,
            loading = loading,
        )
    }.flowOn(dispatchers.computation)

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
            if(!isLoading()) {
                setLoading(true)

                val newPage = 0
                val webSocketLogList: MutableList<WebSocketLog> = getWebSocketLog(query, "", newPage).toMutableList()

                if(webSocketLogList.size == PAGINATION_LIMIT) {
                    webSocketLogList += WebSocketLogPlaceHolder
                }

                setLoading(false)
                _websocketLogPagination.value = _websocketLogPagination.value.copy(
                    webSocketLoggingList = webSocketLogList,
                    query = query,
                    page = newPage,
                    isReachMax = false,
                )
            }
        }) {
            setLoading(false)
            emitErrorMessage(it.message)
        }
    }

    private fun handleLoadNextPage() {
        viewModelScope.launchCatchError(block = {
            if(!isLoading() && !isReachMax()) {
                setLoading(true)

                val pagination = _websocketLogPagination.value
                val newPage = pagination.page + 1

                val webSocketLogNextList = getWebSocketLog(pagination.query, "", newPage)

                val oldList: List<WebSocketLog> = pagination.webSocketLoggingList.filterIsInstance(WebSocketLogUiModel::class.java)
                val newList = (oldList + webSocketLogNextList).toMutableList()

                val isReachMax = webSocketLogNextList.size != PAGINATION_LIMIT
                if(!isReachMax) {
                    newList += WebSocketLogPlaceHolder
                }

                setLoading(false)
                _websocketLogPagination.value = _websocketLogPagination.value.copy(
                    webSocketLoggingList = newList,
                    page = newPage,
                    isReachMax = isReachMax
                )
            }
        }) {
            setLoading(false)
            emitErrorMessage(it.message)
        }
    }

    private fun handleDeleteAllLog() {
        viewModelScope.launchCatchError(block = {
            deleteAllWebSocketLogUseCase.executeOnBackground()

            emitMessage(UiString.Resource(R.string.websocket_log_delete_all_message))
            _uiEvent.emit(WebSocketLoggingEvent.DeleteAllLogEvent)
        }) {
            emitErrorMessage(it.message)
        }
    }

    /**
     * Utility
     */
    private fun isReachMax(): Boolean = _websocketLogPagination.value.isReachMax

    private fun isLoading(): Boolean = _loading.value

    private fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    private suspend fun emitErrorMessage(message: String?) {
        emitMessage(
            message?.let {
                UiString.Text(it)
            } ?: UiString.Resource(R.string.websocket_log_default_error_message)
        )
    }

    private suspend fun emitMessage(uiString: UiString) {
        _uiEvent.emit(
            WebSocketLoggingEvent.ShowInfoEvent(
                uiString = uiString
            )
        )
    }

    private suspend fun getWebSocketLog(query: String, source: String, page: Int): List<WebSocketLogUiModel> {
        return getWebSocketLogUseCase.let {
            it.setParam(query, source, page, PAGINATION_LIMIT)
            it.executeOnBackground()
        }
    }

    private companion object {
        const val PAGINATION_LIMIT = 20
    }
}