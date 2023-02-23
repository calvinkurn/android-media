package com.tokopedia.media.picker.ui.activity.picker

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.R
import com.tokopedia.media.picker.data.FeatureToggleManager
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.repository.BitmapConverterRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaFileRepository
import com.tokopedia.media.picker.ui.publisher.EventState
import com.tokopedia.media.picker.ui.publisher.PickerEventBus
import com.tokopedia.media.picker.utils.flattenFilter
import com.tokopedia.media.picker.utils.internal.NetworkStateManager
import com.tokopedia.media.picker.utils.internal.ResourceManager
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.isUrl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val deviceInfo: DeviceInfoRepository,
    private val mediaFiles: MediaFileRepository,
    private val bitmapConverter: BitmapConverterRepository,
    private val param: PickerCacheManager,
    private val featureToggle: FeatureToggleManager,
    private val networkState: NetworkStateManager,
    private val resources: ResourceManager,
    private val dispatchers: CoroutineDispatchers,
    private val eventBus: PickerEventBus
) : ViewModel() {

    private var _medias = MutableLiveData<List<MediaUiModel>>()
    val medias: LiveData<List<MediaUiModel>> get() = _medias

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _isMediaEmpty = MutableLiveData<Boolean>()
    val isMediaEmpty: LiveData<Boolean> get() = _isMediaEmpty

    private var _includeMedias = MutableLiveData<List<String?>>()
    val includeMedias: LiveData<List<String?>> get() = _includeMedias

    private var _isFetchMediaLoading = MutableLiveData<Boolean>()
    val isFetchMediaLoading: LiveData<Boolean> get() = _isFetchMediaLoading

    private var _pickerParam = MutableLiveData<PickerParam>()
    val pickerParam: LiveData<PickerParam> get() = _pickerParam

    private var _editorParam = MutableLiveData<Pair<PickerResult, EditorParam>>()
    val editorParam: LiveData<Pair<PickerResult, EditorParam>> get() = _editorParam

    private var _connectionIssue = MediatorLiveData<String>()
    val connectionIssue: LiveData<String> get() = _connectionIssue

    val uiEvent: Flow<EventState>
        get() {
            return eventBus
                .subscriber(viewModelScope)
                .flowOn(dispatchers.computation)
        }

    fun navigateToEditorPage(result: PickerResult) {
        viewModelScope.launch {
            val data = Pair(result, param.get().getEditorParam() ?: EditorParam())
            _editorParam.value = data
        }
    }

    fun setPickerParam(pickerParam: PickerParam?) {
        val mPickerParam = pickerParam ?: return

        if (mPickerParam.isEditorEnabled() && featureToggle.isEditorEnabled().not()) {
            mPickerParam.withoutEditor()
        }

        _pickerParam.value = param.set(mPickerParam)
    }

    fun isDeviceStorageAlmostFull(): Boolean {
        return deviceInfo.isDeviceStorageAlmostFull(
            param.get().minStorageThreshold()
        )
    }

    fun preSelectedMedias(param: PickerParam) {
        val mIncludeMedias = param.includeMedias()
        if (mIncludeMedias.isEmpty()) return

        if (networkState.isNetworkConnected().not()) {
            _connectionIssue.value = resources.string(
                R.string.picker_include_medias_connection_error
            )

            return
        }

        val result = mIncludeMedias.flattenFilter { it.isUrl() }

        bitmapConverter.convert(result.first)
            .flowOn(dispatchers.io)
            .onStart { _isLoading.value = true }
            .onCompletion { _isLoading.value = false }
            .map {
                _includeMedias.value = it + result.second
            }
            .launchIn(viewModelScope)
    }

    fun loadMedia(bucketId: Long, start: Int = 0) {
        viewModelScope.launch {
            mediaFiles(bucketId, start)
                .flowOn(dispatchers.io)
                .onStart { _isFetchMediaLoading.value = true }
                .onCompletion { _isFetchMediaLoading.value = false }
                .collect { data ->
                    _medias.value = mediaToUiModel(data)

                    // check if the data is exist or not,
                    // it used for rendering the empty state page
                    if (_isMediaEmpty.value == null) {
                        _isMediaEmpty.value = data.isEmpty()
                    }
                }
        }
    }

}
