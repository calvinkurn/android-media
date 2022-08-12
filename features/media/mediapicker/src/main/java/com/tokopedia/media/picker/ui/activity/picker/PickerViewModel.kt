package com.tokopedia.media.picker.ui.activity.picker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.uimodel.MediaUiModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val deviceInfo: DeviceInfoRepository,
    private val mediaGallery: MediaRepository,
    private val param: ParamCacheManager,
    private val dispatchers: CoroutineDispatchers
): ViewModel() {

    private var _medias = MediatorLiveData<List<MediaUiModel>>()
    val medias: LiveData<List<MediaUiModel>> get() = _medias

    val uiEvent = EventFlowFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun isDeviceStorageAlmostFull(): Boolean {
        return deviceInfo.execute(
            param.get().minStorageThreshold()
        )
    }

    fun loadLocalGalleryBy(bucketId: Long) {
        viewModelScope.launch {
            val result = mediaGallery(bucketId)

            withContext(dispatchers.main) {
                _medias.value = mediaToUiModel(result)
            }
        }
    }

}