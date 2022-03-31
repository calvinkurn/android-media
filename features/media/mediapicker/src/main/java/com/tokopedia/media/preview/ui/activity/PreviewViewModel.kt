package com.tokopedia.media.preview.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.isVideoFormat
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviewViewModel @Inject constructor(
    private val imageCompressor: ImageCompressionManager,
    private val mediaSaver: SaveToGalleryManager
) : ViewModel() {

    private val _files = MutableSharedFlow<List<MediaUiModel>>()

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // get all files
    private val originalFiles: SharedFlow<List<String>> =
        _files.transform { files ->
            val paths = files.map { it.path }

            emit(paths)
        }.shareIn(viewModelScope, SharingStarted.Lazily)

    // get files only video comes from camera picker
    private val videoCameraFiles: SharedFlow<List<String>> =
        _files.transform { files ->
            val videos = files
                .filter { isVideoFormat(it.path) && it.isFromPickerCamera }
                .map { it.path }

            emit(videos)
        }.shareIn(viewModelScope, SharingStarted.Lazily)

    // get files only image comes from camera picker
    private val imageCameraFiles: SharedFlow<List<String>> =
        _files.transform { files ->
            val images = files
                .filter { !isVideoFormat(it.path) && it.isFromPickerCamera }
                .map { it.path }

            emit(images)
        }.shareIn(viewModelScope, SharingStarted.Lazily)

    // get compressed images
    private val compressedImages: SharedFlow<List<String>> =
        imageCameraFiles.transform {
            emitAll(imageCompressor.compress(it))
        }.shareIn(viewModelScope, SharingStarted.Lazily)

    val result = combine(
        originalFiles,
        videoCameraFiles,
        compressedImages
    ) { originalFiles, videoCameraFiles, compressedImages ->
        _isLoading.value = false

        /*
        * dispatch to local device gallery
        * for video and image comes from camera picker
        * */
        videoCameraFiles.plus(compressedImages)
            .forEach {
                mediaSaver.dispatch(it)
            }

        PickerResult(
            originalPaths = originalFiles,
            compressedImages = compressedImages
        )
    }

    fun files(files: List<MediaUiModel>) {
        _isLoading.value = true

        viewModelScope.launch {
            _files.emit(files)
        }
    }

}