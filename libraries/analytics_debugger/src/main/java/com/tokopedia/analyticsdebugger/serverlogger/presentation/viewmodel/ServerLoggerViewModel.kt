package com.tokopedia.analyticsdebugger.serverlogger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.serverlogger.domain.mapper.ServerLoggerMapper
import com.tokopedia.analyticsdebugger.serverlogger.domain.usecase.DeleteLoggerListUseCase
import com.tokopedia.analyticsdebugger.serverlogger.domain.usecase.GetLoggerListUseCase
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.BaseServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerConstants
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ServerLoggerViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getLoggerListUseCase: GetLoggerListUseCase,
    private val deleteLoggerListUseCase: DeleteLoggerListUseCase,
    private val serverLoggerMapper: ServerLoggerMapper
) : ViewModel() {

    private val _baseServerLogger = MutableLiveData<Result<List<BaseServerLoggerUiModel>>>()
    val baseServerLogger: LiveData<Result<List<BaseServerLoggerUiModel>>>
        get() = _baseServerLogger

    private val _serverLoggerUiList = MutableLiveData<Result<List<ItemServerLoggerUiModel>>>()
    val itemServerLoggerUiList: LiveData<Result<List<ItemServerLoggerUiModel>>>
        get() = _serverLoggerUiList

    private val _messageEvent = MutableSharedFlow<String>()

    val messageEvent: Flow<String>
        get() = _messageEvent

    private val _deleteServerLogger = MutableLiveData<Result<Boolean>>()
    val deleteServerLogger: LiveData<Result<Boolean>>
        get() = _deleteServerLogger


    fun loadInitialData(
        query: String,
        priority: String,
        page: Int = ServerLoggerConstants.FIRST_PAGE
    ) {
        viewModelScope.launchCatchError(coroutineDispatchers.main, block = {
            val serverLoggerList =
                withContext(coroutineDispatchers.io) {
                    getLoggerListUseCase.execute(
                        query,
                        priority,
                        ServerLoggerConstants.LIMIT,
                        getOffset(page)
                    )
                }
            val baseServerLoggerList = mutableListOf<BaseServerLoggerUiModel>().apply {
                add(serverLoggerMapper.mapToPriorityList(priority))
                addAll(serverLoggerList)
            }
            _baseServerLogger.value = Success(baseServerLoggerList)
        }, onError = {
            _messageEvent.emit(it.localizedMessage.orEmpty())
        })
    }

    fun loadServerLogger(query: String, priority: String, page: Int) {
        viewModelScope.launchCatchError(coroutineDispatchers.main, block = {
            val serverLoggerResult = withContext(coroutineDispatchers.io) {
                getLoggerListUseCase.execute(
                    query,
                    priority,
                    ServerLoggerConstants.LIMIT,
                    getOffset(page)
                )
            }
            _serverLoggerUiList.value = Success(serverLoggerResult)
        }) {
            _messageEvent.emit(it.localizedMessage.orEmpty())
        }
    }

    fun deleteAllServerLogger() {
        viewModelScope.launchCatchError(coroutineDispatchers.main, block = {
            withContext(coroutineDispatchers.io) {
                deleteLoggerListUseCase.execute()
            }
            _deleteServerLogger.value = Success(true)
        }) {
            _messageEvent.emit(it.localizedMessage.orEmpty())
        }
    }

    private fun getOffset(page: Int) = ServerLoggerConstants.LIMIT * page

}