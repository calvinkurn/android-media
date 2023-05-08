package com.tokopedia.media.picker.ui.publisher

import com.tokopedia.config.GlobalConfig
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.uimodel.MediaUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface PickerEventBus {
    fun subscriber(coroutineScope: CoroutineScope): Flow<EventState>
    fun reset()
    fun dispose()

    fun cameraCaptureEvent(element: MediaUiModel)
    fun addMediaEvent(element: MediaUiModel)
    fun removeMediaEvent(element: MediaUiModel)
    fun notifyDataOnChangedEvent(elements: List<MediaUiModel>)
}

class PickerEventBusImpl @Inject constructor(
    param: PickerCacheManager
) : PickerEventBus {

    private val stateList = mutableMapOf<String, MutableSharedFlow<EventState>>()

    private val key by lazy {
        param.get().generateKey()
    }

    override fun subscriber(coroutineScope: CoroutineScope): Flow<EventState> {
        return state().shareIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(TIMEOUT_IN_MILLIS),
            MAX_REPLAY
        ).onCompletion {
            emit(EventState.Idle)
        }.onEach {
            if (GlobalConfig.isAllowDebuggingTools()) {
                println("MediaPicker: [EVENTBUS] key={$key} state={$it}")
            }
        }
    }

    override fun reset() {
        stateList.remove(key)
    }

    override fun dispose() {
        state().tryEmit(EventState.Idle)
    }

    override fun cameraCaptureEvent(element: MediaUiModel) {
        state().tryEmit(EventPickerState.CameraCaptured(element))
    }

    override fun addMediaEvent(element: MediaUiModel) {
        if (element.file?.exists() == false) return

        state().tryEmit(EventPickerState.SelectionAdded(element))
    }

    override fun removeMediaEvent(element: MediaUiModel) {
        state().tryEmit(EventPickerState.SelectionRemoved(element))
    }

    override fun notifyDataOnChangedEvent(elements: List<MediaUiModel>) {
        state().tryEmit(EventPickerState.SelectionChanged(elements))
    }

    private fun state(): MutableSharedFlow<EventState> {
        stateList[key]?.let { return it }

        val mState = MutableSharedFlow<EventState>(MAX_REPLAY)
        stateList[key] = mState
        return mState
    }

    private fun PickerParam.generateKey(): String {
        return pageSourceName() + subPageSourceName()
    }

    companion object {
        private const val TIMEOUT_IN_MILLIS = 500L
        private const val MAX_REPLAY = 50
    }
}
