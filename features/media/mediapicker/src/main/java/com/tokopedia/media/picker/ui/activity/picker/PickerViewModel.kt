package com.tokopedia.media.picker.ui.activity.picker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.repository.BitmapConverterRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.isUrl
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val deviceInfo: DeviceInfoRepository,
    private val mediaGallery: MediaRepository,
    private val bitmapConverter: BitmapConverterRepository,
    private val param: ParamCacheManager,
    private val dispatchers: CoroutineDispatchers
): ViewModel() {

    private var _medias = MediatorLiveData<List<MediaUiModel>>()
    val medias: LiveData<List<MediaUiModel>> get() = _medias

    private var _includeMedias = MediatorLiveData<List<String?>>()
    val includeMedias: LiveData<List<String?>> get() = _includeMedias

    val uiEvent = EventFlowFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun isDeviceStorageAlmostFull(): Boolean {
        return deviceInfo.execute(
            param.get().minStorageThreshold()
        )
    }

    fun preSelectedMedias() {
        val mIncludeMedias = param.get().includeMedias()
        if (mIncludeMedias.isEmpty()) return

        viewModelScope.launch(dispatchers.io) {
            val mappedMedias = mIncludeMedias.map {
                if (it.isUrl()) {
                    return@map bitmapConverter.convert(it)
                }

                it
            }

            withContext(dispatchers.main) {
                _includeMedias.value = mappedMedias
            }
        }
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
