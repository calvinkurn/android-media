package com.tokopedia.media.preview.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.preview.data.repository.ImageCompressionRepository
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepository
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.uimodel.MediaUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviewViewModel @Inject constructor(
    private val imageCompressor: ImageCompressionRepository,
    private val mediaSaver: SaveToGalleryRepository,
    dispatchers: CoroutineDispatchers,
    private val paramCache: ParamCacheManager
) : ViewModel() {

    private val _files = MutableSharedFlow<List<MediaUiModel>>()

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // get all files
    private val originalFiles: Flow<List<String>> =
        _files.map { files ->
            files
                .map { it.file }
                .map { it?.path.toEmptyStringIfNull() }
        }

    // get files only video comes from camera picker
    private val videoCameraFiles: Flow<List<String>> =
        _files.map { files ->
            files
                .filter { it.file?.isVideo() == true && it.isFromPickerCamera }
                .map { it.file }
                .map { it?.path.toEmptyStringIfNull() }
        }

    // get files only image comes from camera picker
    private val imageCameraFiles: Flow<List<String>> =
        _files.map { files ->
            files
                .filter { it.file?.isImage() == true && it.isFromPickerCamera }
                .map { it.file }
                .map { it?.path.toEmptyStringIfNull() }
    }

    // get compressed images
    private val compressedImages: Flow<List<String>> =
        imageCameraFiles
            .map { imageCompressor.compress(it) }
            .flowOn(dispatchers.computation)

    val result = combine(
        originalFiles,
        videoCameraFiles,
        imageCameraFiles,
        compressedImages
    ) { originalFiles, videoCameraFiles, imageCameraFiles, compressedImages ->
        _isLoading.value = false

        /*
        * dispatch to local device gallery
        * for video and image comes from camera picker
        * */
        if (!paramCache.get().isEditorEnabled()) {
            imageCameraFiles
                .plus(videoCameraFiles)
                .forEach {
                    mediaSaver.dispatch(it)
                }
        }

        PickerResult(
            originalPaths = originalFiles,
            videoFiles = videoCameraFiles,
            compressedImages = compressedImages
        )
    }.shareIn(
        viewModelScope,
        SharingStarted.Lazily
    )

    fun files(files: List<MediaUiModel>) {
        _isLoading.value = true

        viewModelScope.launch {
            _files.emit(files)
        }
    }

}
