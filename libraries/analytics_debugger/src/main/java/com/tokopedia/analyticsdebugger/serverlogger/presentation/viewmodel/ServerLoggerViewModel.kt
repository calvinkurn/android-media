package com.tokopedia.analyticsdebugger.serverlogger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.serverlogger.domain.usecase.DeleteLoggerListUseCase
import com.tokopedia.analyticsdebugger.serverlogger.domain.usecase.GetLoggerListUseCase
import com.tokopedia.analyticsdebugger.serverlogger.domain.usecase.GetPriorityListUseCase
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.BaseServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerConstants
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ServerLoggerViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val getLoggerPriorityListUseCase: GetPriorityListUseCase,
    private val getLoggerListUseCase: GetLoggerListUseCase,
    private val deleteLoggerListUseCase: DeleteLoggerListUseCase
) : BaseViewModel(coroutineDispatchers.main) {

    private val _serverLoggerPagination = MutableStateFlow(listOf<ServerLoggerUiModel>())
    private val _chips = MutableStateFlow(ServerLoggerPriorityUiModel())

    val uiState: Flow<List<BaseServerLoggerUiModel>> = combine(
        _serverLoggerPagination,
        _chips
    ) { serverLoggerPagination, chips ->
        mutableListOf<BaseServerLoggerUiModel>().apply {
            add(chips)
            addAll(serverLoggerPagination)
        }
    }.flowOn(coroutineDispatchers.computation)

    private val _deleteServerLogger = MutableLiveData<Result<Boolean>>()
    val deleteServerLogger: LiveData<Result<Boolean>>
        get() = _deleteServerLogger


    fun loadInitialData(
        query: String,
        priority: String,
        page: Int = ServerLoggerConstants.FIRST_PAGE
    ) {
        loadPriority()
        loadServerLogger(query, priority, page)
    }

    fun loadServerLogger(query: String, priority: String, page: Int) {
        val offset = ServerLoggerConstants.LIMIT * page
        launchCatchError(block = {
            val serverLoggerResult =
                getLoggerListUseCase.execute(query, priority, ServerLoggerConstants.LIMIT, offset)
            _serverLoggerPagination.value = serverLoggerResult
        }) {

        }
    }

    private fun loadPriority() {
        launchCatchError(block = {
            val priorityResult = getLoggerPriorityListUseCase.execute()
            _chips.value = priorityResult
        }) {

        }
    }

    fun deleteAllServerLogger() {
        launchCatchError(block = {
            val deleteResult = deleteLoggerListUseCase.execute()
            _deleteServerLogger.value = Success(true)
        }) {

        }
    }
}